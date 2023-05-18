package com.spring.boot.musicAlbum.board.controller;

import com.spring.boot.musicAlbum.board.exception.UserAuthorizeException;
import com.spring.boot.musicAlbum.board.model.BoardDTO;
import com.spring.boot.musicAlbum.board.service.BoardService;
import com.spring.boot.musicAlbum.login.model.Account;
import com.spring.boot.musicAlbum.login.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.security.Principal;

@Controller
public class BoardController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private S3Client s3Client; // @Bean

    @GetMapping("/bAdd")
    public String addBoard(Model model) {
        model.addAttribute("board", new BoardDTO());
        return "bAdd";
    }

    @PostMapping("/bAdd")
    public String add(@ModelAttribute BoardDTO board,
                      @RequestParam("imageFile") MultipartFile imageFile,
                      @RequestParam("soundFile") MultipartFile soundFile,
                      RedirectAttributes redirectAttributes) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Account account = accountService.getAccountByUsername(currentUserName);
        board.setAccount(account);
        boardService.createBoard(board, imageFile, soundFile);
        redirectAttributes.addFlashAttribute("msg","게시글이 작성되었습니다.");
        return "redirect:/bList";
    }


    @GetMapping("/bList")
    public String getAllbList(Model model) {

//        model.addAttribute("boards", boardService.getAllBoards());
        model.addAttribute("boards", boardService.getAllBoardsWithAccount());
        return "bList";
    }

    @GetMapping("/search")
    public String searchBList(Model model,
                              @RequestParam("search") String search,
                              @RequestParam("option") String option) {
        if(option=="title"){
            model.addAttribute("boards", boardService.getTitleBySearch(search));
        }
        else if(option=="genre"){
            model.addAttribute("boards", boardService.getGenreBySearch(search));
        }
        else if(option=="author"){
            model.addAttribute("boards", boardService.getAuthorBySearch(search));
        }
        return "bList";
    }


    @GetMapping("/bDetail/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        BoardDTO board = boardService.getBoardById(id);
        if (board == null) {
            return "redirect:/";
        }
        model.addAttribute("board", board);
        return "bDetail";
    }

    @PostMapping("/bDetail/{id}")
    public String DetailBoard(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        BoardDTO board = boardService.getBoardById(id);
        if (board == null) {
            return "redirect:/bList";
        }
        redirectAttributes.addFlashAttribute("board", board);
        return "redirect:/bUpdate";
    }

    @GetMapping("/bUpdate/{id}")
    public String updateBoard(@PathVariable(value="id") Long id, Model model, Principal principal) throws UserAuthorizeException {
        BoardDTO board = boardService.getBoardById(id);
        if (board == null) {
            return "redirect:/";
        }
        if (!board.getAccount().getUsername().equals(principal.getName())) {
            throw new UserAuthorizeException("권한이 없습니다.");
        }
        model.addAttribute("board", board);
        return "bUpdate";
    }

    @PostMapping("/edit")
    public String UpdateBoard(@ModelAttribute("board") BoardDTO newBoard,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @RequestParam("soundFile") MultipartFile soundFile,
                              RedirectAttributes redirectAttributes) throws IOException, UserAuthorizeException {
        boardService.updateBoard(newBoard,imageFile,soundFile);
        redirectAttributes.addFlashAttribute("msg", "정상적으로 수정되었습니다.");
        return "redirect:/bList";
    }


    @GetMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id, Principal principal) throws UserAuthorizeException {
        // 게시글 삭제 전에 파일도 함께 삭제
        BoardDTO board = boardService.getBoardById(id);
        if (board != null) {
            // R2에서 파일 삭제
            if (board.getBImage() != null) {
                boardService.deleteFileFromR2(board.getBImage());
            }
            if (board.getBSound() != null) {
                boardService.deleteFileFromR2(board.getBSound());
            }
        }
        // 게시글 삭제
        boardService.deleteBoard(id, principal.getName());
        return "redirect:/bList";
    }


//    @GetMapping("/delete/{id}")
//    public String deleteBoard(@PathVariable Long id) {
//        // 게시글 삭제 전에 파일도 함께 삭제
//        BoardDTO board = boardService.getBoardById(id);
//        if (board != null) {
//            // R2에서 파일 삭제
//            if (board.getBImage() != null) {
//                boardService.deleteFileFromR2(board.getBImage());
//            }
//            if (board.getBSound() != null) {
//                boardService.deleteFileFromR2(board.getBSound());
//            }
//        }
//        // 게시글 삭제
//        boardService.deleteBoard(id);
//        return "redirect:/bList";
//    }


//    @PostMapping("/{postId}/delete")
//    public String deletePost(@PathVariable Long postId, Principal principal) {
//        postService.deletePost(postId, principal.getName());
//
//        return "redirect:/posts";
//    }

    @GetMapping("/upload/{filename}")
    public ResponseEntity<?> upload(@PathVariable String filename) throws IOException {
        byte[] byteArray = boardService.loadFile(filename);
        return new ResponseEntity<>(byteArray, HttpStatus.OK);
    }


}
