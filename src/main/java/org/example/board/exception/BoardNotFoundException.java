package org.example.board.exception;

import org.example.board.support.ErrorCode;

/**
 * 게시글을 찾을 수 없을 때 발생하는 예외
 *
 * @author dahee
 * @since 2025-10-21
 */
public class BoardNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public BoardNotFoundException(ErrorCode code, Object... args) {
        super(code.format(args));
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode.code();
    }
}
