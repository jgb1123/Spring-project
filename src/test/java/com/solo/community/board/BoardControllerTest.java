package com.solo.community.board;

import com.google.gson.Gson;
import com.solo.community.board.controller.BoardController;
import com.solo.community.board.dto.BoardPatchDto;
import com.solo.community.board.dto.BoardPostDto;
import com.solo.community.board.dto.BoardResponseDto;
import com.solo.community.board.entity.Board;
import com.solo.community.board.mapper.BoardMapper;
import com.solo.community.board.service.BoardService;
import com.solo.community.util.BoardDummy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardMapper boardMapper;

    @Autowired
    private Gson gson;

    @Test
    @WithMockUser
    public void postBoardTest() throws Exception {
        BoardPostDto boardPostDto = BoardDummy.createPostDto();
        String content = gson.toJson(boardPostDto);
        given(boardMapper.boardPostDtoToBoard(Mockito.any(BoardPostDto.class)))
                .willReturn(new Board());
        given(boardService.createBoard(Mockito.anyString(), Mockito.any(Board.class)))
                .willReturn(new Board());

        ResultActions actions = mockMvc.perform(
                post("/api/v1/board")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
        );

        actions
                .andExpect(status().isCreated())
                .andDo(document("post-board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("boardTitle").type(JsonFieldType.STRING).description("글 제목"),
                                        fieldWithPath("boardContent").type(JsonFieldType.STRING).description("글 내용")
                                )
                        )
                ));
    }

    @Test
    @WithMockUser
    public void getBoardTest() throws Exception {
        Long boardId = 1L;
        BoardResponseDto boardResponseDto = BoardDummy.createdResponseDto1();
        given(boardService.findBoard(boardId))
                .willReturn(new Board());
        given(boardMapper.boardToBoardResponseDto(Mockito.any(Board.class)))
                .willReturn(boardResponseDto);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/board/{boardId}", boardId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.boardId").value(boardResponseDto.getBoardId()))
                .andExpect(jsonPath("$.data.boardTitle").value(boardResponseDto.getBoardTitle()))
                .andExpect(jsonPath("$.data.boardContent").value(boardResponseDto.getBoardContent()))
                .andExpect(jsonPath("$.data.nickname").value(boardResponseDto.getNickname()))
                .andDo(document("get-board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("data.boardTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data.boardContent").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시글 작성일"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("게시글 수정일"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 별명")
                                )
                        )
                ));
    }

    @Test
    @WithMockUser
    public void getBoardsTest() throws Exception {
        int page = 1;
        int size = 10;
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        Board board1 = BoardDummy.createBoard1();
        Board board2 = BoardDummy.createBoard2();
        BoardResponseDto boardResponseDto1 = BoardDummy.createdResponseDto1();
        BoardResponseDto boardResponseDto2 = BoardDummy.createdResponseDto2();
        List<BoardResponseDto> responses = List.of(boardResponseDto1, boardResponseDto2);
        Page<Board> boardPage = new PageImpl<>(List.of(board1, board2), PageRequest.of(page - 1, size,
                Sort.by("boardId").ascending()), 2);
        given(boardService.findBoards(Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(boardPage);
        given(boardMapper.boardsToBoardResponseDtos(Mockito.anyList()))
                .willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/board")
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-boards",
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
                                        fieldWithPath("data[].boardId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("data[].boardTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data[].boardContent").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("게시글 생성일"),
                                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("게시글 수정일"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("작성자 별명"),

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
    public void patchBoardTest() throws Exception {
        Long boardId = 1L;
        BoardPatchDto boardPatchDto = BoardDummy.createPatchDto();
        String content = gson.toJson(boardPatchDto);
        given(boardMapper.boardPatchDtoToBoard(Mockito.any(BoardPatchDto.class)))
                .willReturn(new Board());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/board/{boardId}", boardId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content)
        );

        actions.andExpect(status().isOk())
                .andDo(document("patch-board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("boardTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("boardContent").type(JsonFieldType.STRING).description("게시글 내용")
                                )
                        )
                ));
    }

    @Test
    @WithMockUser
    public void deleteBoardTest() throws Exception {
        Long boardId = 1L;
        doNothing().when(boardService).deleteBoard(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/board/{boardId}", boardId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 식별자")
                        )
                ));
    }
}
