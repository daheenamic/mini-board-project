package org.example.board.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.Board;
import org.example.board.dto.BoardRequestDto;
import org.example.board.dto.BoardResponseDto;
import org.example.board.exception.BoardNotFoundException;
import org.example.board.exception.PasswordMismatchException;
import org.example.board.repository.BoardRepository;
import org.example.board.support.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시판(Board) 서비스
 *
 * @author dahee
 * @since 2025-10-21
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<BoardResponseDto> getBoardList(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(board -> BoardResponseDto.from(board, false));
    }

    public Page<BoardResponseDto> searchByTitle(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContaining(keyword, pageable);
        return boards.map(b -> BoardResponseDto.from(b, false));
    }

    public Page<BoardResponseDto> searchByName(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByNameContaining(keyword, pageable);
        return boards.map(b -> BoardResponseDto.from(b, false));
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository
                .findById(id)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND, id));

        return BoardResponseDto.from(board, true);
    }

    @Transactional
    public Long createBoard(BoardRequestDto dto) {
        Board board = Board.from(dto);
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }

    @Transactional
    public void updateBoard(Long id, BoardRequestDto dto) {
        Board board = boardRepository
                .findById(id)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND, id));

        if (board.isPasswordMismatch(dto.getPassword())) {
            throw new PasswordMismatchException(ErrorCode.PASSWORD_MISMATCH, id);
        }

        board.update(dto.getName(), dto.getTitle(), dto.getContent());
        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long id, String password) {
        Board board = boardRepository
                .findById(id)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND, id));

        if (board.isPasswordMismatch(password)) {
            throw new PasswordMismatchException(ErrorCode.PASSWORD_MISMATCH, id);
        }

        boardRepository.deleteById(id);
    }
}
