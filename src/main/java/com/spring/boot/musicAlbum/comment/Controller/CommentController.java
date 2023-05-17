package com.spring.boot.musicAlbum.comment.Controller;

import com.spring.boot.musicAlbum.board.model.BoardDTO;
import com.spring.boot.musicAlbum.comment.model.Comment;
import com.spring.boot.musicAlbum.board.service.BoardService;
import com.spring.boot.musicAlbum.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BoardService boardService;

    @PostMapping("/comment/add/{boardId}")
    public String addComment(@PathVariable Long boardId,
                             @RequestParam("content") String content,
                             RedirectAttributes redirectAttributes) {
        BoardDTO board = boardService.getBoardById(boardId);
        if (board == null) {
            return "redirect:/bList";
        }

        commentService.createComment(board, content);

        redirectAttributes.addFlashAttribute("msg", "댓글이 작성되었습니다.");
        return "redirect:/bDetail/" + boardId;
    }

    @GetMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
                                RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return "redirect:/bList";
        }
        Long boardId = comment.getBoard().getId();
        commentService.deleteComment(commentId);

        redirectAttributes.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
        return "redirect:/bDetail/" + boardId;
    }
}
