package org.example.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.example.board.dto.BoardRequestDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 게시판(Board) 도메인 클래스
 *
 * @author dahee
 * @since 2025-10-21
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "board")
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String password;
    private String content;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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
