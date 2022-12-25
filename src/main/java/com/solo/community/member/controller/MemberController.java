package com.solo.community.member.controller;

import com.solo.community.dto.MultiResponseDto;
import com.solo.community.dto.SingleResponseDto;
import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;
import com.solo.community.member.mapper.MemberMapper;
import com.solo.community.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity getMember(@PathVariable Long memberId) {
        Member foundMember = memberService.findMember(memberId);
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(foundMember);
        return new ResponseEntity<>(new SingleResponseDto<>(memberResponseDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "10") int size) {
        Page<Member> pageMembers = memberService.findMembers(page, size);
        List<Member> members = pageMembers.getContent();
        List<MemberResponseDto> memberResponseDtos = memberMapper.membersToMemberResponseDtos(members);
        return new ResponseEntity<>(new MultiResponseDto<>(memberResponseDtos, pageMembers), HttpStatus.OK);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity patchMember(@PathVariable Long memberId,
                                      @RequestBody MemberPatchDto memberPatchDto) {
        Member modifiedMember = memberMapper.memberPatchDtoToMember(memberPatchDto);
        memberService.updateMember(memberId, modifiedMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
