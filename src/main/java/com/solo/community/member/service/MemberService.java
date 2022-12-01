package com.solo.community.member.service;

import com.solo.community.member.entity.Member;
import com.solo.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member create(Member member) {
        return memberRepository.save(member);
    }
}
