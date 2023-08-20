package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.PostRepository;
import com.example.engineerpracticequestions.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Post> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAllPosts();
            for (var post : posts) {
                User userByPostId = userRepository.findUserByPostId(post.getPostId());
                post.setUser(userByPostId);
            }
            return posts;
        } catch (Exception e) {
            logger.error("Error fetching all posts", e);
            throw new RuntimeException("Error fetching all posts", e);
        }
    }

    @Transactional
    public Post getPostById(Long id) {
        try {
            return postRepository.findPostById(id);
        } catch (Exception e) {
            logger.error("Error fetching post by id: {}", id, e);
            throw new RuntimeException("Error fetching post by id: " + id, e);
        }
    }

    @Transactional
    public Post savePost(Post post) {
        try {
            return postRepository.saveOrUpdatePost(post.getPostId(), post.getUserId(), post.getContent(), post.getImage());
        } catch (Exception e) {
            logger.error("Error saving post with id: {}", post.getPostId(), e);
            throw new RuntimeException("Error saving post with id: " + post.getPostId(), e);
        }
    }

    @Transactional
    public byte[] findImg(Long id) {
        try {
            Post post = postRepository.findPostById(id);
            return post.getImage();
        } catch (Exception e) {
            logger.error("Error fetching image by post id: {}", id, e);
            throw new RuntimeException("Error fetching image by post id: " + id, e);
        }
    }

    public void deletePostAndComments(Long id) {
        try {
            postRepository.deletePostAndComments(id);
        } catch (Exception e) {
            logger.error("Error deleting post and its comments by id: {}", id, e);
            throw new RuntimeException("Error deleting post and its comments by id: " + id, e);
        }
    }
}
