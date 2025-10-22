package org.example.board.support;

import lombok.Getter;

/**
 * 게시글 검색 타입 (SearchType)
 *
 * @author jeongdahee
 * @since 2025-10-21
 */
@Getter
public enum SearchType {
    TITLE("title"),
    NAME("name");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public static SearchType from(String type) {
        for (SearchType t : values()) {
            if (t.getValue().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid search type: " + type);
    }
}
