package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

//    @GetMapping("/{postId}")
//    public List<Comment> getAllCommentsByPostId(@PathVariable Long postId) {
//        List<Comment> allCommentsByPostId = commentService.getAllCommentsByPostId(postId);
//        return allCommentsByPostId;
//    }

    @PostMapping ("/post")
    public List<Comment> getCommentByPostId(@RequestBody Post post) {
        System.out.println(commentService.getCommentsByPost(post).toString());
        return commentService.getCommentsByPost(post);
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        System.out.println(comment.toString());
        return commentService.saveComment(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}

