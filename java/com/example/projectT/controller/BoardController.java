package com.example.projectT.controller;

import com.example.projectT.dto.BoardDto;
import com.example.projectT.dto.CommentDto;
import com.example.projectT.dto.UserDto;
import com.example.projectT.repository.BoardRepository;
import com.example.projectT.repository.CommentRepository;
import com.example.projectT.service.BoardService;
import com.example.projectT.service.CommentService;
import com.example.projectT.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/page/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    @RequestMapping("/")
    public String mainPage(HttpServletRequest req, RedirectAttributes redirectAttributes) {

        if (req.getQueryString() == null) {

            // URL에 GET(get-posts)에 사용할 쿼리가 없는 경우 쿼리를 추가하여 Redirect
            redirectAttributes.addAttribute("mode", 0);
            redirectAttributes.addAttribute("page", 0);
            redirectAttributes.addAttribute("postCnt", 10);
            redirectAttributes.addAttribute("keyword", "");
            redirectAttributes.addAttribute("searchType", 0);

            return "redirect:/page/board/";
        }

        return "/page/board/main";
    }

    //게시글 리스트 가져오기(검색기능 포함)
    @GetMapping("/getBoards")
    @ResponseBody
    public Map<String, Object> notice(@RequestParam int mode,
                                @RequestParam int page,
                                @RequestParam int postCnt,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "2") int searchType
                                ) {

        Map<String, Object> boardInfo = new HashMap<>();

        // {mode}의 글 중에서 한 페이지당 {postCnt}로 지정하여 {page}번째 페이지 리스트 가져오기
//        List<BoardDto> boardDtos = new ArrayList<>();
//        PageRequest pageRequest = PageRequest.of(page, postCnt, Sort.Direction.ASC, "id");
//            var boards = boardRepository.findByMode(mode, pageRequest).getContent();
//            boards.forEach(boardEntity -> {
//                boardDtos.add(boardService.entityToDto(boardEntity));
//            });
//            return boardDtos;
//        String keyword = null;
//        keyword, searchType ajax추가
//        searchType = 0,1,default

        boardInfo.put("boards", boardService.searchBoards(mode,page,postCnt,keyword,searchType));
        boardInfo.put("totalPageNum", boardRepository.findAllByMode(mode).size()/postCnt + 1);
        return boardInfo;
    }

    //게시글 페이지 이동
    @RequestMapping("/board.html")
    public String getBoard(@RequestParam Long boardId, Model model) {

        model.addAttribute("boardId", boardId);

        // 조회수 +1
        BoardDto boardDto = boardService.entityToDto(boardRepository.findById(boardId).get());
        int beforeHitNum = boardDto.getHitNum();
        boardDto.setHitNum(beforeHitNum + 1);
        boardService.update(boardDto);

        return "/page/board/boardView";
    }

    //게시글 가져오기
    @GetMapping("/getBoard")
    @ResponseBody
    public Map<String, Object> board(@RequestParam Long boardId) {

        var board = boardRepository.findById(boardId).get();
        var user = board.getUser();

        Map<String, Object> boardInfo = new HashMap<>();
        boardInfo.put("board", boardService.entityToDto(board));
        boardInfo.put("user", userService.entityToDto(user));

        return boardInfo;
    }

    //댓글 저장
    @PostMapping("/saveComment/{boardId}/{parentId}")
    public String saveComment(CommentDto commentDto,
                              @PathVariable Long boardId,
                              @PathVariable Long parentId,
                              @SessionAttribute(name = "userInfo") UserDto userDto) {

        commentDto.setBoardId(boardId);
        commentDto.setUserId(userDto.getId());
        commentDto.setParentsId(parentId);
        log.info("commentDto = {}", commentDto);
        commentService.registerCommentAtBoard(commentDto);

        return "redirect:/page/board/board.html?boardId="+boardId;
    }

    //댓글 불러오기
    @GetMapping("/getComments/{parentId}")
    @ResponseBody
    public List<CommentDto> getComments(@RequestParam Long boardId, @PathVariable int parentId) {

        List<CommentDto> returnComments = new ArrayList<>();
        var board = boardRepository.findById(boardId).get();

        commentRepository.findByBoard(board).get().forEach(comment -> {
            //parentId가 일치하는 경우만 리스트에 포함
            if (comment.getParentId() == parentId) {
                returnComments.add(commentService.entityToDto(comment));
            }
        });

        return returnComments;
    }

    //댓글 유저 닉네임 불러오기
    @GetMapping("/getUserFromCommentId")
    @ResponseBody
    public UserDto getUserFromCommentId(@RequestParam Long commentId) {

        var comment = commentRepository.findById(commentId).get();
        var user = userService.entityToDto(comment.getUser());

        return user;
    }

    @PostMapping("/moveWriteBoardPage")
    @ResponseBody
    public String moveWriteBoardPage(@SessionAttribute(name = "userInfo") UserDto userDto) {
        return "/page/board/write.html";
    }

    //글쓰기 페이지
    @RequestMapping("/write.html")
    public void write() {}

    //글 작성 -> DB에 등록
    @PostMapping("/registBoard")
    @ResponseBody
    public String registBoard(BoardDto boardDto,
                              @SessionAttribute(name = "userInfo") UserDto userDto) {

        boardDto.setUserId(userDto.getId());
        boardService.registerBoard(boardDto);

        var href = UriComponentsBuilder.newInstance()
                .queryParam("mode", boardDto.getMode())
                .queryParam("page", 0)
                .queryParam("postCnt", 10)
                .build().toUri().toString();

        return "/page/board/" + href;
    }
}