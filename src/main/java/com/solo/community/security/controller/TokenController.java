package com.solo.community.security.controller;

import com.solo.community.member.entity.Member;
import com.solo.community.member.service.MemberService;
import com.solo.community.security.jwt.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    @GetMapping("/api/v1/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");
        Map<String, Object> claims = jwtTokenizer.getClaims(token, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())).getBody();
        String email = (String) claims.get("email");

        Member member = memberService.findVerifiedMember(email);
        List<String> roles = member.getRoles();
        Map<String, Object> newClaims = new HashMap<>();
        claims.put("email", email);
        claims.put("roles", roles);
        String subject = email;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(newClaims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }
}
