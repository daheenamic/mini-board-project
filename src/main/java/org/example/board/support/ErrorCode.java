package org.example.board.support;

/**
 * 에러 코드 정의(enum)
 *
 * @author jeongdahee
 * @since 2025-10-21
 */
public enum ErrorCode {
    BOARD_NOT_FOUND("BOARD_NOT_FOUND", "Board not found: %d"),
    PASSWORD_MISMATCH("PASSWORD_MISMATCH", "Password does not match for board id=%d");

    private final String code;
    private final String template;

    ErrorCode(String code, String template) {
        this.code = code; this.template = template;
    }

    public String code() {
        return code;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
