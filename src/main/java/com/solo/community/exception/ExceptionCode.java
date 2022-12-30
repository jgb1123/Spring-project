package com.solo.community.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    BOARD_NOT_FOUND(404, "Board not found"),
    CANNOT_CHANGE_BOARD(403, "Board can not change"),
    CANNOT_CREATE_BOARD(403, "Board can not create"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    CANNOT_CHANGE_COMMENT(403, "Comment can not change"),
    CANNOT_CREATE_COMMENT(403, "Comment can not create");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
