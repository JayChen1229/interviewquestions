package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.model.Post;
import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/upload")
    public void uploadImage(@PathVariable Long userId, @RequestParam MultipartFile image) {
        User user = userService.getUserById(userId);
        if (user != null) {
            try {
                userService.updateUserCoverImage(userId, image.getBytes());
                System.out.println("上傳成功");
            } catch (IOException e) {
                System.out.println("上傳失敗");
            }
        } else {
            System.out.println("上傳失敗");
        }
    }
}

