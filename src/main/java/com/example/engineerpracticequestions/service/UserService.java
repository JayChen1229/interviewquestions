package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User getUserById(Long id) {
        try {
            return userRepository.findUserById(id);
        } catch (Exception e) {
            logger.error("Error fetching user by id: {}", id, e);
            throw new RuntimeException("Error fetching user by id: " + id, e);
        }
    }

    @Transactional
    public Boolean saveUser(User user) {
        try {
            String email = user.getEmail();
            if (userRepository.existsByEmail(email) != 0) {
                return false;
            }
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.saveOrUpdateUser(user.getUserId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getCoverImage(), user.getBiography());
            return true;
        } catch (Exception e) {
            logger.error("Error saving user with email: {}", user.getEmail(), e);
            throw new RuntimeException("Error saving user with email: " + user.getEmail(), e);
        }
    }

    @Transactional
    public byte[] findImg(Long id) {
        try {
            User user = userRepository.findUserById(id);
            return user.getCoverImage();
        } catch (Exception e) {
            logger.error("Error fetching image by user id: {}", id, e);
            throw new RuntimeException("Error fetching image by user id: " + id, e);
        }
    }

    @Transactional
    public void updateUserCoverImage(Long userId, byte[] coverImage) {
        try {
            userRepository.updateUserCoverImage(userId, coverImage);
        } catch (Exception e) {
            logger.error("Error updating cover image for user id: {}", userId, e);
            throw new RuntimeException("Error updating cover image for user id: " + userId, e);
        }
    }

    @Transactional
    public void updateUserBiography(Long userId, String biography) {
        try {
            userRepository.updateUserBiography(userId, biography);
        } catch (Exception e) {
            logger.error("Error updating biography for user id: {}", userId, e);
            throw new RuntimeException("Error updating biography for user id: " + userId, e);
        }
    }

    @Transactional
    public User findUser(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return null;
            }
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
            return null;
        } catch (Exception e) {
            logger.error("Error finding user by email: {}", email, e);
            throw new RuntimeException("Error finding user by email: " + email, e);
        }
    }
}
