package com.solo.community.util;

import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;

import java.util.List;

public interface MemberDummy {
    static Member createMember1() {
        return Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgd@gmail.com")
                .phone("010-1234-5678")
                .nickname("hgd123")
                .roles(List.of("ROLE_USER"))
                .build();
    }

    static Member createMember2() {
        return Member.builder()
                .memberId(2L)
                .name("이순신")
                .email("lss@gmail.com")
                .phone("010-8765-4321")
                .nickname("lss123")
                .roles(List.of("ROLE_USER"))
                .build();
    }

    static MemberPatchDto createPatchDto() {
        return MemberPatchDto.builder()
                .phone("010-1234-5678")
                .nickname("hgd123")
                .build();
    }

    static MemberResponseDto createResponseDto1() {
        return MemberResponseDto.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgd@gmail.com")
                .phone("010-1234-5678")
                .nickname("hgd123")
                .build();
    }
}
