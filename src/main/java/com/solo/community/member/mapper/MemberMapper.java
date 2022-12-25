package com.solo.community.member.mapper;

import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberMapper {
    public Member memberPatchDtoToMember(MemberPatchDto memberPatchDto) {
        return Member.builder()
                .phone(memberPatchDto.getPhone())
                .nickname(memberPatchDto.getNickname())
                .build();
    }

    public MemberResponseDto memberToMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .nickname(member.getNickname())
                .build();
    }

    public List<MemberResponseDto> membersToMemberResponseDtos(List<Member> members) {
        return members
                .stream()
                .map(member -> memberToMemberResponseDto(member))
                .collect(Collectors.toList());
    }
}
