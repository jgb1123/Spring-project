package com.solo.community.member.entity;

import com.solo.community.basetime.BaseTimeEntity;
import com.solo.community.member.dto.MemberPatchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void changeInfo(Member modifiedMember) {
        if(modifiedMember.getPhone() != null) this.phone = modifiedMember.getPhone();
        if(modifiedMember.getNickname() != null) this.nickname = modifiedMember.getNickname();
    }

    public void changeSignUpInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public void changeRoles(List<String> roles) {
        this.roles = roles;
    }
}
