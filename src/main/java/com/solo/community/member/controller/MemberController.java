package com.solo.community.member.controller;

import com.solo.community.member.dto.MemberPostDto;
import com.solo.community.member.entity.Member;
import com.solo.community.member.mapper.MemberMapper;
import com.solo.community.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity create(@RequestBody MemberPostDto memberPostDto){
        Member member = memberMapper.memberPostDtoToMember(memberPostDto);
        Member savedMember = memberService.create(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
