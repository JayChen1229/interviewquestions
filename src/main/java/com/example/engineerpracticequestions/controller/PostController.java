package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.service.CommentService;
import com.example.engineerpracticequestions.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.savePost(post);
    }
    @PostMapping("/{postId}/images")
    public void uploadImage(@PathVariable Long postId, @RequestParam MultipartFile image) {
        Post post = postService.getPostById(postId);
        if (post != null) {
            try {
                post.setImage(image.getBytes());
                postService.savePost(post);
                System.out.println("上傳成功");
            } catch (IOException e) {
                System.out.println("上傳失敗");
            }
        } else {
            System.out.println("上傳失敗");
        }
    }
    @GetMapping("/{postId}/comments")
    public List<Comment> getCommentByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePostAndComments(postId);
    }
    @GetMapping(value = "/{postId}/images", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getImage(@PathVariable Long postId) {
        return postService.findImg(postId);
    }
}

