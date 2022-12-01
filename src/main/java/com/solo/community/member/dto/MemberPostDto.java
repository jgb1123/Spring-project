package com.solo.community.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPostDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String nickname;
}
