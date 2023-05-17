package com.spring.boot.musicAlbum.comment.repository;

import com.spring.boot.musicAlbum.comment.model.Comment;
import com.spring.boot.musicAlbum.board.model.BoardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<com.spring.boot.musicAlbum.comment.model.Comment> findByBoard(BoardDTO board);
}
