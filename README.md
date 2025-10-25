

# 🧾 Mini Board Project

간단한 CRUD 게시판 프로젝트로, Spring Boot 기반 MVC 구조와 Thymeleaf 템플릿 엔진을 사용하여 구현했습니다.  
게시글 **등록/조회/수정/삭제**, **검색(제목/작성자)**, **페이징**, **전역 예외 처리** 등을 포함합니다.

---

## 📦 기술 스택

| 항목 | 내용                   |
|---|----------------------|
| JDK | 21                   |
| Spring Boot | 3.5.6                |
| Persistence | **Spring Data JPA (Hibernate)** |
| View | Thymeleaf            |
| DB | MySQL 8.0.43         |
| Build | Gradle               |
| Dependencies | Spring Web, Thymeleaf, Spring Data JPA, Lombok, MySQL Connector |
| IDE | IntelliJ IDEA        |

---

## 📁 패키지 구조

- `JdbcConfig.java` 삭제됨
- `JpaConfig.java` 새로 추가됨
- `BoardRepository.java`는 이제 `JpaRepository` 상속으로 변경

```
src
├── main
│   ├── java
│   │   └── org
│   │       └── example
│   │           └── board
│   │               ├── BoardApplication.java              # 메인 실행 클래스
│   │               ├── config
│   │               │   └── JpaConfig.java                 # JPA Auditing 설정
│   │               ├── controller
│   │               │   └── BoardController.java           # 요청 처리 (목록/등록/수정/삭제/검색)
│   │               ├── domain
│   │               │   └── Board.java                     # 게시글 엔티티
│   │               ├── dto
│   │               │   ├── BoardRequestDto.java           # 등록·수정용 DTO
│   │               │   └── BoardResponseDto.java          # 응답용 DTO
│   │               ├── exception
│   │               │   ├── BoardNotFoundException.java    # 게시글 없음 예외
│   │               │   ├── PasswordMismatchException.java # 비밀번호 불일치 예외
│   │               │   └── GlobalExceptionHandler.java    # 전역 예외 처리 (@ControllerAdvice)
│   │               ├── repository
│   │               │   └── BoardRepository.java           # Spring Data JPA 인터페이스
│   │               ├── service
│   │               │   └── BoardService.java              # 비즈니스 로직 (CRUD/검색/페이징)
│   │               └── support
│   │                   ├── ErrorCode.java                 # 에러 코드 Enum
│   │                   └── SearchType.java                # 검색 타입 Enum
│   │
│   └── resources
│       ├── application.yml                                # DB, 로깅, 페이징 설정
│       ├── static
│       │   ├── css
│       │   │   ├── board.css                              # 게시판 페이지 스타일
│       │   │   └── error.css                              # 에러 페이지 스타일
│       │   └── favicon.ico
│       └── templates
│           ├── board
│           │   ├── list.html                              # 목록 페이지
│           │   ├── view.html                              # 상세보기
│           │   ├── writeForm.html                         # 글 등록 폼
│           │   ├── updateForm.html                        # 글 수정 폼
│           │   └── deleteForm.html                        # 글 삭제 폼
│           └── error
│               ├── 400.html                               # 400 에러 페이지
│               ├── 404.html                               # 404 에러 페이지
│               ├── 500.html                               # 500 에러 페이지
│               └── auth.html                              # 권한 에러 페이지
└── docs
    └── sql
        ├── schema.sql          # DDL (테이블 생성문)
        └── data.sql            # DML (기본 데이터 insert)
```

---

## 📄 SQL 문서 관리
프로젝트의 DDL/DML SQL 스크립트는 `docs/sql/` 폴더에서 관리합니다.
- `schema.sql`: 데이터베이스 및 테이블 생성문 (DDL)
- `data.sql`: 초기 더미 데이터 삽입문 (DML)
이 폴더는 **문서 및 버전 관리용**이며, 애플리케이션 실행 시 자동 실행되지 않습니다.

## 🧠 핵심 도메인 & DTO

### Board (domain)
- 게시글 JPA 엔티티 (`@Entity`)
- 주요 메서드:
    - `static Board from(BoardRequestDto dto)` : DTO → 엔티티 변환 (생성용)
    - `void update(String name, String title, String content)` : 게시글 수정
    - `boolean isPasswordMismatch(String inputPassword)` : 비밀번호 검증
    - `void increaseViewCount()` : 조회수 증가
- `@CreatedDate`, `@LastModifiedDate` 로 작성일·수정일 자동 관리

### BoardRequestDto (dto)
- **등록/수정 요청용** DTO.
- 검증 어노테이션 사용: `@NotBlank`, `@Size` 등.
- 수정 화면 초기화를 위한 헬퍼: `static BoardRequestDto from(BoardResponseDto dto)`
  - 비밀번호는 **폼에서 다시 입력받기 위해 세팅하지 않음**.

### BoardResponseDto (dto)
- **응답용** DTO.
- 포맷팅 포함:
  - 목록: `yyyy/MM/dd`
  - 상세: `yyyy/MM/dd HH:mm`
- 정적 팩토리: `static BoardResponseDto from(Board board, boolean includeTime)`

---

## 🔎 검색 & 페이징

### 검색
- 지원 타입: `SearchType` enum (`TITLE`, `NAME`).
- Repository 메서드 예) `findByTitleContaining(...)`, `findByNameContaining(...)`.
- 잘못된 검색 타입은 `IllegalArgumentException` → 400 페이지로 안내.

### 페이징
- Spring Data `Page<T>` 사용.
- `application.yml`에서 `one-indexed-parameters: true` 설정으로 **1페이지부터** 동작.
- 번호 표시:
  - 일반 모드: `boards.number * boards.size + stat.count`
  - 역순(최신이 위): `boards.totalElements - (boards.number * boards.size + stat.index)`
- 목록 상단: **5/10/20/50개씩 보기 셀렉트** 제공.
- 하단: `< 1 2 3 ... >` 단순 페이지네이션.

---

## ⚙️ 설정(application.yml)

```yaml
spring:
  application:
    name: miniBoardProject
  thymeleaf:
    cache: false

  datasource:
    url: jdbc:mysql://localhost:3306/boarddb
    username: board
    password: board1234
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    open-in-view: false

  data:
    web:
      pageable:
        one-indexed-parameters: true

logging:
  level:
    org.hibernate.sql: debug
    org.hibernate.orm.jdbc.bind: trace
    org.hibernate.orm.jdbc.extract: trace
```
---

## 🧩 JPA Auditing 설정

`JpaConfig`에서 `@EnableJpaAuditing` 활성화 및 `AuditingEntityListener` 등록.

```java
import java.awt.datatransfer.Clipboard;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
```

- 엔티티(Clipboard)에 다음과 같이 적용

```java
@EntityListeners(AuditingEntityListener.class)
public class Board {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

---

## 🧾 예외 처리

- `@ControllerAdvice`로 전역 예외 매핑
- `BoardNotFoundException` → **404**(error/404.html)
- `PasswordMismatchException` → **인증 오류 페이지**(error/auth.html)
- `IllegalArgumentException` → **400**(error/400.html)
- `Exception` → **500**(error/500.html)

---

## 🚏라우팅 요약

|HTTP |URL                                            |설명 |
|---|------------------------------------------------|---|
|GET | `/board/list`                                  |목록 조회(정렬/페이징) |
|GET | `/board/view?id={id}`                          |상세 조회 |
|GET | `/board/writeForm`                             |등록 폼 |
|POST | `/board/write`                                 |등록 처리(`@Valid BoardRequestDto`) |
|GET | `/board/updateForm?id={id}`                    |수정 폼 |
|POST | `/board/update`                                |수정 처리(비번 검증) |
GET | `/board/deleteForm?id={id}`                    |삭제 폼 |
|POST | `/board/delete`                                |삭제 처리(비번 검증) |
|GET | `/board/search?type={title\|name}&keyword=...` |검색 +페이징 |

---

## 🧪 실행 방법

1) **DB 생성**및 테이블 생성
```sql
-- 데이터베이스 생성
CREATE database
boarddb;

-- 사용자 생성
CREATE USER 'board'@'%'
IDENTIFIED by 'board1234';

-- 권한 부여
GRANT ALL
PRIVILEGES ON
boarddb .*TO 'board'@'%';
FLUSH PRIVILEGES
      
-- 위의 CREATE TABLE 실행
```

2) **환경 설정** (`application.yml` 수정) → DB 접속 정보 입력

3) **실행**
```bash
./gradlew bootRun
```
접속: http://localhost:8080/board/list

---

## 🧭 향후 개선 계획

- [ ] 로그인/회원 관리, 작성자 식별
- [ ] 댓글 기능
- [ ] 조회수 카운트
- [ ] 파일 업로드
- [ ] 정렬 옵션(최신/이름/조회수)

---

## 👩‍💻 Author
### 멋쟁이사자처럼 백엔드 19기 정다희 (Jeong Dahee)
📅 2025-10-21  
학습용 미니 프로젝트
