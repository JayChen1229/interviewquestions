package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.PostRepository;
import com.example.engineerpracticequestions.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAllPosts();

        for(var post:posts){
            User userByPostId = userRepository.findUserByPostId(post.getPostId());
            post.setUser(userByPostId);
        }
        return posts;
    }

    @Transactional
    public Post getPostById(Long id) {
        return postRepository.findPostById(id);
    }



    @Transactional
    public Post savePost(Post post) {
        return postRepository.saveOrUpdatePost(
                post.getPostId(),
                post.getUserId(),
                post.getContent(),
                post.getImage()
        );
    }
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    @Transactional
    // 提供img id, 得到Image 的 byte陣列
    public byte[] findImg(Long id) {
        Post post = postRepository.findPostById(id);
        // TripImage::getImage：引用TripImage的getImage()
        // map (裡面對象如果存在則執行)
        return post.getImage();
    }
}

