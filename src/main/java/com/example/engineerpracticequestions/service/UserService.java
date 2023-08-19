package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }


    public Boolean saveUser(User user) {
        String email = user.getEmail();
        // 檢查資料庫中是否已經存在相同的電子郵件地址
        if (userRepository.existsByEmail(email)!=0) {
            System.out.println("Email already exists");
            return false;
        }
        // 針對 password 做Bcrypt加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.saveOrUpdateUser(user.getUserId(),user.getUserName(),user.getEmail(),user.getPassword(),user.getCoverImage(),user.getBiography());
        return true;
    }

    // 提供img id, 得到Image 的 byte陣列
    @Transactional
    public byte[] findImg(Long id) {
        User user = userRepository.findUserById(id);
        // TripImage::getImage：引用TripImage的getImage()
        // map (裡面對象如果存在則執行)
        return user.getCoverImage();
    }

    @Transactional
    public void updateUserCoverImage(Long userId, byte[] coverImage) {
        userRepository.updateUserCoverImage(userId, coverImage);
    }

    @Transactional
    public User findUser(String email, String password) {
        // 根據電子郵件地址從資料庫中獲取使用者
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null; // 使用者不存在，直接返回 null
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            // 密碼匹配，返回使用者
            return user;
        }
        return null; // 密碼不匹配，返回 null
    }

}

