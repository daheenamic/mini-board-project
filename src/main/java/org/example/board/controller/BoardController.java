package org.example.board.controller;

import lombok.RequiredArgsConstructor;
import org.example.board.dto.BoardRequestDto;
import org.example.board.exception.PasswordMismatchException;
import org.example.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.example.board.dto.BoardResponseDto;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.example.board.support.SearchType;

/**
 * 게시판(Board) Controller
 *
 * @author jeongdahee
 * @since 2025-10-21
 */
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 목록
    @GetMapping("/list")
    public String list(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<BoardResponseDto> boards = boardService.getBoardList(pageable);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    // 게시글 검색
    @GetMapping("/search")
    public String search(@RequestParam String type,
                         @RequestParam String keyword,
                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        Page<BoardResponseDto> results;

        try {
            SearchType searchType = SearchType.from(type);
            switch (searchType) {
                case TITLE -> results = boardService.searchByTitle(keyword, pageable);
                case NAME -> results = boardService.searchByName(keyword, pageable);
                default -> results = boardService.getBoardList(pageable);
            }
            model.addAttribute("searchType", searchType.getValue());
        } catch (IllegalArgumentException e) {
            results = boardService.getBoardList(pageable);
            model.addAttribute("searchType", "all");
        }

        model.addAttribute("boards", results);
        model.addAttribute("keyword", keyword);
        return "board/list";
    }

    // 게시글 상세보기
    @GetMapping("/view")
    public String view(@RequestParam Long id, Model model) {
        BoardResponseDto board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/view";
    }

    // 게시글 등록 폼
    @GetMapping("/writeForm")
    public String writeForm(Model model) {
        if (!model.containsAttribute("board")) {
            model.addAttribute("board", new BoardRequestDto());
        }
        return "board/writeForm";
    }

    // 게시글 등록
    @PostMapping("/write")
    public String write(@Valid @ModelAttribute("board") BoardRequestDto dto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("board", dto);
            return "board/writeForm";
        }

        Long savedId = boardService.createBoard(dto);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 등록되었습니다.");
        return "redirect:/board/view?id=" + savedId;
    }

    // 게시글 수정 폼
    @GetMapping("/updateForm")
    public String updateForm(@RequestParam Long id, Model model) {
        BoardResponseDto detail = boardService.getBoard(id);
        BoardRequestDto formDto = BoardRequestDto.from(detail);

        model.addAttribute("board", formDto);
        model.addAttribute("boardId", id);

        return "board/updateForm";
    }

    // 게시글 수정
    @PostMapping("/update")
    public String update(@RequestParam Long id, @Valid @ModelAttribute("board") BoardRequestDto dto,
                         BindingResult result, RedirectAttributes redirectAttributes,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("board", dto);
            model.addAttribute("boardId", id);
            return "board/updateForm";
        }

        try {
            boardService.updateBoard(id, dto);
            redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
            return "redirect:/board/view?id=" + id;
        } catch (PasswordMismatchException e) {
            model.addAttribute("board", dto);
            model.addAttribute("boardId", id);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "board/updateForm";
        }
    }

    // 게시글 삭제 폼
    @GetMapping("/deleteForm")
    public String deleteForm (@RequestParam Long id, Model model) {
        model.addAttribute("boardId", id);
        return "board/deleteForm";
    }

    // 게시글 삭제
    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam String password,
                         RedirectAttributes redirectAttributes, Model model) {
        try {
            boardService.deleteBoard(id, password);
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
            return "redirect:/board/list";
        } catch (PasswordMismatchException e) {
            model.addAttribute("boardId", id);
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "board/deleteForm";
        }
    }
}
