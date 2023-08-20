package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        // 驗證並清理用戶輸入
        String sanitizedText = HtmlUtils.htmlEscape(comment.getContent());
        comment.setContent(sanitizedText);

        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }
}

