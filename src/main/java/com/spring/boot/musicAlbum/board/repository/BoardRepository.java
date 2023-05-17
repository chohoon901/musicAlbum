package com.spring.boot.musicAlbum.board.repository;

import com.spring.boot.musicAlbum.board.model.BoardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardDTO, Long> {

    @Query("SELECT b FROM BoardDTO b JOIN FETCH b.account")
    List<BoardDTO> findAllWithAccount();
}
