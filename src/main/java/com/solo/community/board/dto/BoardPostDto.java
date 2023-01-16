package com.solo.community.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostDto {
    @NotBlank
    @Size(min = 4, max = 50, message = "글 제목은 4자 이상 50자 이하여야 합니다")
    private String boardTitle;
    @NotBlank
    @Size(min = 4, max = 2000, message = "글 내용은 4자 이상 2000자 이하여야 합니다.")
    private String boardContent;
}
