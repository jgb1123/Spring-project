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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    public void getMemberTest() throws Exception {
        Long memberId = 1L;
        MemberResponseDto responseDto =
                new MemberResponseDto(
                        memberId,
                        "홍길동",
                        "hgd@gmail.com",
                        "010-1234-5678",
                        "hgd123"
                );

        given(memberService.findMember(memberId)).willReturn(new Member());
        given(memberMapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(responseDto);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/member/{memberId}", memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(responseDto.getMemberId()))
                .andExpect(jsonPath("$.data.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.data.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.data.phone").value(responseDto.getPhone()))
                .andExpect(jsonPath("$.data.nickname").value(responseDto.getNickname()))
                .andDo(document("get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 식별자")
                        ),
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
    public void getMembersTest() throws Exception {
        int page = 1;
        int size = 10;
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        Member member1 = MemberDummy.createMember1();
        Member member2 = MemberDummy.createMember2();
        MemberResponseDto responseDto1 = MemberDummy.createdResponseDto1();
        MemberResponseDto responseDto2 = MemberDummy.createdResponseDto2();
        List<MemberResponseDto> responses = List.of(responseDto1, responseDto2);
        Page<Member> memberPage = new PageImpl<>(List.of(member1, member2), PageRequest.of(page - 1, size,
                Sort.by("nickname").descending()), 2);

        given(memberService.findMembers(Mockito.anyInt(), Mockito.anyInt())).willReturn(memberPage);
        given(memberMapper.membersToMemberResponseDtos(Mockito.anyList())).willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/member")
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-members",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Page 크기")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data[].email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data[].phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("별명"),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수")
                                )
                        )
                ));
    }
    @Test
    @WithMockUser
    public void patchMemberTest() throws Exception {
        Long memberId = 1L;
        MemberPatchDto patchDto = new MemberPatchDto(
                "010-1234-5678",
                "hgd123"
        );
        String content = gson.toJson(patchDto);

        given(memberMapper.memberPatchDtoToMember(Mockito.any(MemberPatchDto.class))).willReturn(new Member());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/member/{memberId}", memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andDo(document("patch-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("별명")
                                )
                        )
                ));
    }

    @Test
    @WithMockUser
    public void deleteMemberTest() throws Exception {
        Long memberId = 1L;
        doNothing().when(memberService).deleteMember(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/member/{memberId}", memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 식별자")
                        )
                ));
    }
}
