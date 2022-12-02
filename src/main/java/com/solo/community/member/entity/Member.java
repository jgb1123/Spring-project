package com.solo.community.member.entity;

import com.solo.community.basetime.BaseTimeEntity;
import com.solo.community.member.dto.MemberPatchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String nickname;

    public void changeInfo(Member modifiedMember) {
        if(modifiedMember.getName() != null) this.name = modifiedMember.getName();
        if(modifiedMember.getEmail() != null) this.email = modifiedMember.getEmail();
        if(modifiedMember.getPassword() != null) this.password = modifiedMember.getPassword();
        if(modifiedMember.getPhone() != null) this.phone = modifiedMember.getPhone();
        if(modifiedMember.getNickname() != null) this.nickname = modifiedMember.getNickname();
    }
}
