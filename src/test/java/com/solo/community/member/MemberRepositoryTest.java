package com.solo.community.member;

import com.solo.community.member.entity.Member;
import com.solo.community.member.repository.MemberRepository;
import com.solo.community.util.MemberDummy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        //when
        Member savedMember = memberRepository.save(member1);
        //then
        assertThat(member1.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    void findMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member member2 = MemberDummy.createMember2();
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        //when
        Member foundMember1 = memberRepository.findById(savedMember1.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find Member"));
        Member foundMember2 = memberRepository.findById(savedMember2.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find Member"));
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(foundMember1.getEmail()).isEqualTo(member1.getEmail());
        assertThat(foundMember2.getEmail()).isEqualTo(member2.getEmail());
    }

    @Test
    void updateMembers() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member modifiedMember = MemberDummy.createMember2();
        Member savedMember = memberRepository.save(member1);
        Member foundMember = memberRepository.findById(savedMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));
        //when
        foundMember.changeInfo(modifiedMember);
        Member changedMember = memberRepository.findById(foundMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));
        //then
        assertThat(changedMember.getNickname()).isEqualTo(modifiedMember.getNickname());
    }

    @Test
    void deleteMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member1);
        //when
        memberRepository.deleteById(savedMember.getMemberId());
        //then
        assertThat(memberRepository.findById(savedMember.getMemberId()).isPresent()).isFalse();
    }
}
