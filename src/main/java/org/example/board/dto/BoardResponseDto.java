package org.example.board.dto;

import lombok.Builder;
import lombok.Data;
import org.example.board.domain.Board;

import java.time.format.DateTimeFormatter;

/**
 * 게시글 응답 DTO
 *
 * @author dahee
 * @since 2025-10-21
 */
@Data
@Builder
public class BoardResponseDto {

    private Long id;
    private String name;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    public static BoardResponseDto from(Board board, boolean isDetail) {
        DateTimeFormatter formatter = isDetail
                ? DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                : DateTimeFormatter.ofPattern("yyyy/MM/dd");

        return BoardResponseDto.builder()
                .id(board.getId())
                .name(board.getName())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt().format(formatter))
                .updatedAt(board.getUpdatedAt().format(formatter))
                .build();
    }
}
