package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.Comment;
import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.CommentRepository;
import com.example.engineerpracticequestions.repository.PostRepository;
import com.example.engineerpracticequestions.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Comment> getCommentsByPost(Long postId) {
        try {
            List<Comment> comments = commentRepository.findCommentByPostId(postId);
            for (var comment : comments) {
                Post post = postRepository.findPostByCommentId(comment.getCommentId());
                User user = userRepository.findUserByCommentId(comment.getCommentId());

                if(user.getCoverImage()!= null){
                    user.setImgUrl("/api/v1/users/" + user.getUserId() + "/images");
                }else {
                    user.setImgUrl("defaultAvatar.png");
                }

                comment.setPost(post);
                comment.setUser(user);
            }
            return comments;
        } catch (Exception e) {
            logger.error("Error fetching comments for post id: {}", postId, e);
            throw new RuntimeException("Error fetching comments for post id: " + postId, e);
        }
    }

    @Transactional
    public Comment saveComment(Comment comment) {
        try {
            return commentRepository.saveOrUpdateComment(
                    comment.getCommentId(),
                    comment.getUserId(),
                    comment.getPostId(),
                    comment.getContent()
            );
        } catch (Exception e) {
            logger.error("Error saving comment with id: {}", comment.getCommentId(), e);
            throw new RuntimeException("Error saving comment with id: " + comment.getCommentId(), e);
        }
    }
}
