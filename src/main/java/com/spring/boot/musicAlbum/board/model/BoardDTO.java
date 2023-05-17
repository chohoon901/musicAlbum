package com.spring.boot.musicAlbum.board.model;

import com.spring.boot.musicAlbum.login.model.Account;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="board")
public class BoardDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String bTitle;
    private String bContent;
    private String bUserID;
    private String bPassword;
    private String bImage;
    private String bSound;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
