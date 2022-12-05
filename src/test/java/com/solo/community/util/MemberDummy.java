package com.solo.community.util;

import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberPostDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;

public interface MemberDummy {
    static Member createMember1() {
        return new Member(1L, "홍길동", "hgd@gmail.com", "12345678", "010-1234-5678", "hgb123");
    }

    static Member createMember2() {
        return new Member(2L, "이순신", "lss@gmail.com", "87654321", "010-8765-4321", "lss12");
    }

    static MemberPostDto createPostDto() {
        return new MemberPostDto("홍길동", "hgd@gmail.com", "12345678", "010-1234-5678", "hgb123");
    }

    static MemberPatchDto createPatchDto() {
        return new MemberPatchDto("홍길동", "hgd@gmail.com", "12345678", "010-1234-5678", "hgb123");
    }

    static MemberResponseDto createdResponseDto1() {
        return new MemberResponseDto(1L, "홍길동", "hgd@gmail.com", "12345678", "010-1234-5678", "hgb123");
    }

    static MemberResponseDto createdResponseDto2() {
        return new MemberResponseDto(2L, "이순신", "lss@gmail.com", "87654321", "010-8765-4321", "lss12");
    }


}
