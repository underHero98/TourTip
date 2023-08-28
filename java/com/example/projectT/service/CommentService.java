package com.example.projectT.service;

import com.example.projectT.domain.entity.Comment;
import com.example.projectT.dto.CommentDto;
import com.example.projectT.repository.BoardRepository;
import com.example.projectT.repository.CommentRepository;
import com.example.projectT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public void registerCommentAtBoard(CommentDto commentDto) {
        commentRepository.save(dtoToEntity(commentDto));
    }
    public void registerCommentAtComment(CommentDto commentDto) {
        var comment = dtoToEntityC(commentDto);
        comment.setBoard(boardRepository.findById(commentDto.getBoardId()).get());
        comment.setUser(userRepository.findById(commentDto.getUserId()).get());
        entityToDto(commentRepository.save(dtoToEntityC(commentDto)));
    }
    //board to comment
    public Comment dtoToEntityB(CommentDto commentDto) {
        var entity = Comment.builder()
                .content(commentDto.getContent())
                .parentId(commentDto.getId())
                .build();
        return entity;
    }

    //comment to comment
    public Comment dtoToEntityC(CommentDto commentDto) {
        var entity = Comment.builder()
                .content(commentDto.getContent())
                .parentId(commentDto.getParentsId())//parentId==commentId
                .build();
        return entity;
    }

    public Comment dtoToEntity(CommentDto commentDto) {
        var user = userRepository.findById(commentDto.getUserId()).get();
        var board = boardRepository.findById(commentDto.getBoardId()).get();
        var entity = Comment.builder()
                .content(commentDto.getContent())
                .parentId(commentDto.getParentsId())
                .user(user)
                .board(board)
                .build();
        return entity;
    }

    public CommentDto entityToDto(Comment comment) {
        var dto = CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .parentsId(comment.getParentId())
                .createAt(comment.getCreateAt())
                .build();
        return dto;
    }
    public void update(CommentDto commentDto){

        var boardCheck = commentRepository.findById(commentDto.getId());
        if(boardCheck.isPresent()){
            boardCheck.ifPresent(e->{
                e.setContent(commentDto.getContent());
                commentRepository.save(e);
            });
            System.out.println("댓글이 수정되었습니다.");

        }else{
            System.out.println("ERROR");
        }
    }

    //user delete
    public void delete(CommentDto commentDto){
        var commentCheck = commentRepository.findById(commentDto.getId());

        if(commentCheck.isPresent()){
            commentCheck.ifPresent(e->{
                commentRepository.delete(e);
            });

            System.out.println("댓글이 삭제되었습니다.");

        }else{
            System.out.println("비밀번호 또는 회원정보가 일치하지 않습니다.");
        }
    }
}
