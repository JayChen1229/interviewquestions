package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // 提供img id, 得到Image 的 byte陣列
    public byte[] findImg(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        // TripImage::getImage：引用TripImage的getImage()
        // map (裡面對象如果存在則執行)
        return post.getImage();
    }
}

