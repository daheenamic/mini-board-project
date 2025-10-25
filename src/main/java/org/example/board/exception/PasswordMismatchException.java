package org.example.board.exception;

import org.example.board.support.ErrorCode;

/**
 * 암호가 일치하지 않을 때 발생하는 예외
 *
 * @author dahee
 * @since 2025-10-21
 */
public class PasswordMismatchException extends RuntimeException {
    private final ErrorCode errorCode;

    public PasswordMismatchException(ErrorCode code, Object... args) {
        super(code.format(args));
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode.code();
    }
}
