package com.solo.community.member;

import com.google.gson.Gson;
import com.solo.community.member.controller.MemberController;
import com.solo.community.member.dto.MemberPatchDto;
import com.solo.community.member.dto.MemberResponseDto;
import com.solo.community.member.entity.Member;
import com.solo.community.member.mapper.MemberMapper;
import com.solo.community.member.service.MemberService;
import com.solo.community.security.config.SecurityConfig;
import com.solo.community.security.jwt.JwtTokenizer;
import com.solo.community.security.utils.CustomAuthorityUtils;
import com.solo.community.util.MemberDummy;
import com.solo.community.util.WithAuthMember;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class})
@AutoConfigureRestDocs
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper memberMapper;

    @Autowired
    private Gson gson;

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN", "USER"})
    void getMemberTest() throws Exception {
        MemberResponseDto memberResponseDto = MemberDummy.createResponseDto1();
        given(memberService.findMember(Mockito.anyString()))
                .willReturn(new Member());
        given(memberMapper.memberToMemberResponseDto(Mockito.any(Member.class)))
                .willReturn(memberResponseDto);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/member")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(memberResponseDto.getName()))
                .andExpect(jsonPath("$.data.email").value(memberResponseDto.getEmail()))
                .andExpect(jsonPath("$.data.phone").value(memberResponseDto.getPhone()))
                .andExpect(jsonPath("$.data.nickname").value(memberResponseDto.getNickname()))
                .andDo(document("get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                List.of(
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("별명")
                                )
                        )
                ));
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN", "USER"})
    void patchMemberTest() throws Exception {
        MemberPatchDto memberPatchDto = MemberDummy.createPatchDto();
        String content = gson.toJson(memberPatchDto);

        given(memberMapper.memberPatchDtoToMember(Mockito.any(MemberPatchDto.class)))
                .willReturn(new Member());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/member")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andDo(document("patch-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("별명")
                                )
                        )
                ));
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN", "USER"})
    void deleteMemberTest() throws Exception {
        doNothing().when(memberService).deleteMember(Mockito.anyString());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/member")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
