package com.solo.community.member.mapper;

import com.solo.community.member.dto.MemberPostDto;
import com.solo.community.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member memberPostDtoToMember(MemberPostDto memberPostDto){
        return Member.builder()
                .memberId(0L)
                .name(memberPostDto.getName())
                .email(memberPostDto.getEmail())
                .password(memberPostDto.getPassword())
                .phone(memberPostDto.getPhone())
                .nickname(memberPostDto.getNickname())
                .build();
    }
}
