package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.CommentRepository;
import com.example.engineerpracticequestions.repository.PostRepository;
import com.example.engineerpracticequestions.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<Comment> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findCommentByPostId(postId);
        for(var comment:comments){
            Post post = postRepository.findPostByCommentId(comment.getCommentId());
            User user = userRepository.findUserByCommentId(comment.getCommentId());
            comment.setPost(post);
            comment.setUser(user);
        }

        return comments;
    }

    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.saveOrUpdateComment(
                comment.getCommentId(),
                comment.getUserId(),
                comment.getPostId(),
                comment.getContent()
        );
    }
}

