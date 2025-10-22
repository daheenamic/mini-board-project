package org.example.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.board.dto.BoardRequestDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 게시판(Board) 도메인 클래스
 *
 * @author jeongdahee
 * @since 2025-10-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("board")
public class Board {
    @Id
    private Long id;
    private String name;
    private String title;
    private String password;
    private String content;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;

    public boolean isPasswordMismatch(String inputPassword) {
        if (this.password == null || inputPassword == null) return true;
        return !this.password.equals(inputPassword);
    }

    public void update(String name, String title, String content) {
        this.name = name;
        this.title = title;
        this.content = content;
    }

    public static Board from(BoardRequestDto dto) {
        Board board = new Board();
        board.name = dto.getName();
        board.title = dto.getTitle();
        board.password = dto.getPassword();
        board.content = dto.getContent();
        return board;
    }
}
