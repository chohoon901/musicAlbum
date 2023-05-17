package com.spring.boot.musicAlbum.comment.service;

import com.spring.boot.musicAlbum.board.model.BoardDTO;
import com.spring.boot.musicAlbum.comment.repository.CommentRepository;
import com.spring.boot.musicAlbum.comment.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByBoard(BoardDTO board) {
        return commentRepository.findByBoard(board);
    }

    public Comment createComment(BoardDTO board, String content) {
        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
}
