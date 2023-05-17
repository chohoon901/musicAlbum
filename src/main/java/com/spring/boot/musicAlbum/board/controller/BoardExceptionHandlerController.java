package com.spring.boot.musicAlbum.board.controller;

import com.spring.boot.musicAlbum.board.exception.UserAuthorizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class BoardExceptionHandlerController {

//    @ExceptionHandler(Exception.class) // Exception으로 발생되는 것들을 처리하겠다 (모든 에러는 Exception타입)
//    public String handleException(Exception ex, Model model) {
//        // ex.getMessage() -> new Exception(message)
//        // throw new Exception("중복된 Username 입니다");
//        model.addAttribute("msg",ex.getMessage());
//        return "error";
//    }

    @ExceptionHandler(UserAuthorizeException.class)
    public String handleException(UserAuthorizeException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("msg", ex.getMessage());
        return "redirect:/error";
    }
}
