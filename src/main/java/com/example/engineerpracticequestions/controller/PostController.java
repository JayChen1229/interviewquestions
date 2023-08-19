package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.savePost(post);
    }

    @PostMapping("/{postId}/upload")
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
            System.out.println("上船失敗");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}

