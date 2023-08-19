package com.example.engineerpracticequestions.service;

import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Boolean saveUser(User user) {
        String email = user.getEmail();

        // 檢查資料庫中是否已經存在相同的電子郵件地址
        if (userRepository.existsByEmail(email)) {
            System.out.println("Email already exists");
            return false;
        }
        // 針對 password 做Bcrypt加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

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

