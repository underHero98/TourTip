package com.example.projectT.service;

import com.example.projectT.domain.entity.Board;
import com.example.projectT.dto.BoardDto;
import com.example.projectT.repository.BoardRepository;
import com.example.projectT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void registerBoard(BoardDto boardDto) {
        var board = dtoToEntity(boardDto);
        board.setUser(userRepository.findById(boardDto.getUserId()).get());
        entityToDto(boardRepository.save(board));
    }

    public Board dtoToEntity(BoardDto boardDto) {
        var entity = Board.builder()
//              notice or qna or free
                .mode(boardDto.getMode())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .hitNum(boardDto.getHitNum())
//               if qna need
                .qnaCategories(boardDto.getQnaCategories())

                .build();
        return entity;
    }

    public BoardDto entityToDto(Board board) {
        var dto = BoardDto.builder()
                .id(board.getId())
                .mode(board.getMode())
                .title(board.getTitle())
                .content(board.getContent())
                .hitNum(board.getHitNum())
                .qnaCategories(board.getQnaCategories())
                .userId(board.getUser().getId())
                .createAt(board.getCreateAt())
                .build();
        return dto;
    }

    public void update(BoardDto boardDto){

        var boardCheck = boardRepository.findById(boardDto.getId());
        if(boardCheck.isPresent()){
            boardCheck.ifPresent(e->{
                e.setTitle(boardDto.getTitle());
                e.setContent(boardDto.getContent());
                e.setHitNum(boardDto.getHitNum());
                boardRepository.save(e);
            });
            System.out.println("글이 수정되었습니다.");

        }else{
            System.out.println("ERROR");
        }
    }

    //user delete
    public void delete(BoardDto boardDto){
        var boardCheck = boardRepository.findById(boardDto.getId());

        if(boardCheck.isPresent()){
            boardCheck.ifPresent(e->{
                boardRepository.delete(e);
            });

            System.out.println("글이 삭제되었습니다.");

        }else{
            System.out.println("비밀번호 또는 회원정보가 일치하지 않습니다.");
        }
    }

    public List<BoardDto> searchBoards(int mode, int page, int postCnt, String keyword, int searchType){
        List<BoardDto> boardDtos = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, postCnt, Sort.Direction.ASC, "id");

        if ((keyword==null) || (keyword=="")) {
            var boards = boardRepository.findByMode(mode, pageRequest).getContent();
            boards.forEach(boardEntity -> {
                boardDtos.add(entityToDto(boardEntity));
            });
            return boardDtos;
        } else {
            switch (searchType){
                case 1:
                    var boards2 = boardRepository.findByModeAndContentContaining(mode,keyword,pageRequest);
                    boards2.forEach(boardEntity->{
                        boardDtos.add(entityToDto(boardEntity));
                    });
                    break;

                case 2:
                    var boards3 = boardRepository.findByUserNickNameContaining(mode,"%"+keyword+"%",pageRequest);
                    boards3.forEach(boardEntity->{
                        boardDtos.add(entityToDto(boardEntity));
                    });
                    break;

                default:
                    var boards1 = boardRepository.findByModeAndTitleContaining(mode,keyword,pageRequest);
                    boards1.forEach(boardEntity->{
                        boardDtos.add(entityToDto(boardEntity));
                    });
                    break;
            }
            return boardDtos;
        }
    }
}
