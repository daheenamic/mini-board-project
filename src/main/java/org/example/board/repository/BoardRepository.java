package org.example.board.repository;

import org.example.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시판(Board) 저장소
 *
 * @author dahee
 * @since 2025-10-21
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    Page<Board> findByNameContaining(String keyword, Pageable pageable);
}
