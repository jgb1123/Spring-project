package com.solo.community.member.mapper;

import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberPostDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberMapper {
    public Member memberPostDtoToMember(MemberPostDto memberPostDto) {
        return Member.builder()
                .memberId(0L)
                .name(memberPostDto.getName())
                .email(memberPostDto.getEmail())
                .password(memberPostDto.getPassword())
                .phone(memberPostDto.getPhone())
                .nickname(memberPostDto.getNickname())
                .build();
    }

    public Member memberPatchDtoToMember(MemberPatchDto memberPatchDto) {
        return Member.builder()
                .name(memberPatchDto.getName())
                .email(memberPatchDto.getEmail())
                .password(memberPatchDto.getPassword())
                .phone(memberPatchDto.getPhone())
                .nickname(memberPatchDto.getNickname())
                .build();
    }

    public MemberResponseDto memberToMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
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
