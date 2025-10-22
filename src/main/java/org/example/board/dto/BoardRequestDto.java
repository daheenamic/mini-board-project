package org.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 게시글 작성 요청 DTO
 *
 * @author jeongdahee
 * @since 2025-10-21
 */
@Data
public class BoardRequestDto {

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 4, max = 10)
    private String password;

    @NotBlank
    private String content;

    public static BoardRequestDto from(BoardResponseDto dto) {
        BoardRequestDto req = new BoardRequestDto();
        req.setName(dto.getName());
        req.setTitle(dto.getTitle());
        req.setContent(dto.getContent());
        return req;
    }
}
