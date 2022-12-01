package com.solo.community.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String Name;
    private String email;
    private String password;
    private String phone;
    private String nickname;
}
