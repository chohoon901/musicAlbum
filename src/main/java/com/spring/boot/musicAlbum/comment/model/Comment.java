package com.spring.boot.musicAlbum.comment.model;

import com.spring.boot.musicAlbum.board.model.BoardDTO;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardDTO board;


    private String content;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}