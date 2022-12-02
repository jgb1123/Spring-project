package com.solo.community.member.service;

import com.solo.community.exception.BusinessLogicException;
import com.solo.community.exception.ExceptionCode;
import com.solo.community.member.entity.Member;
import com.solo.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("nickname").ascending()));
    }

    public void updateMember(Long memberId, Member modifiedMember) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        foundMember.changeInfo(modifiedMember);
    }

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
