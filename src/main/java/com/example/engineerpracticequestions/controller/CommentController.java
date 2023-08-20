package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// posts/comments/postId

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

//    @GetMapping
//    public List<Comment> getAllComments() {
//        return commentService.getAllComments();
//    }

//    @GetMapping ("/post/{postId}")
//    public List<Comment> getCommentByPostId(@PathVariable Long postId) {
//        return commentService.getCommentsByPost(postId);
//    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

//    @DeleteMapping("/{id}")
//    public void deleteComment(@PathVariable Long id) {
//        commentService.deleteComment(id);
//    }
}

