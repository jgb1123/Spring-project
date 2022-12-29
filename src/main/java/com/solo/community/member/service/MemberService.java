package com.solo.community.member.service;

import com.solo.community.exception.BusinessLogicException;
import com.solo.community.exception.ExceptionCode;
import com.solo.community.member.entity.Member;
import com.solo.community.member.repository.MemberRepository;
import com.solo.community.security.jwt.JwtTokenizer;
import com.solo.community.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils customAuthorityUtils;

    public Member createMember(Member member) {
        List<String> roles = customAuthorityUtils.createRoles(member.getEmail());
        member.changeRoles(roles);
        return memberRepository.save(member);
    }

    public Member findMember(String email) {
        return findVerifiedMember(email);
    }

    public Member updateMember(String email, Member modifiedMember) {
        Member foundMember = findVerifiedMember(email);
        foundMember.changeInfo(modifiedMember);
        return foundMember;
    }

    public void deleteMember(String email) {
        Member foundMember = findVerifiedMember(email);
        memberRepository.delete(foundMember);
    }

    public Member findVerifiedMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public boolean existsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }
}
