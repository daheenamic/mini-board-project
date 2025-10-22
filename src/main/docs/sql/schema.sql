-- 기존 테이블 삭제 (필요 시)
DROP TABLE IF EXISTS board;

-- Board 테이블 생성
CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 인덱스 생성 (성능 최적화)
CREATE INDEX idx_board_created_at ON board(created_at DESC);