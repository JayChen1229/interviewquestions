package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.service.CommentService;
import com.example.engineerpracticequestions.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        // 驗證並清理用戶輸入
        String sanitizedContent = HtmlUtils.htmlEscape(post.getContent());
        post.setContent(sanitizedContent);

        Post createdPost = postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PostMapping("/{postId}/images")
    public ResponseEntity<String> uploadImage(@PathVariable Long postId, @RequestParam MultipartFile image) {
        try {
            Post post = postService.getPostById(postId);
            if (post != null) {
                post.setImage(image.getBytes());
                postService.savePost(post);
                return ResponseEntity.ok("Image uploaded successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Post not found for id: " + postId);
            }
        } catch (IOException e) {
            logger.error("Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
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

