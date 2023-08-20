package com.example.engineerpracticequestions.controller;


import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/images")
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

    @PostMapping("/{userId}/biographies")
    public void updateBiography(@PathVariable Long userId, @RequestParam String biography) {
        User user = userService.getUserById(userId);
        if (user != null) {
            userService.updateUserBiography(userId,biography);
        }else {
            System.out.println("出問題了");
        }
    }



    @GetMapping(value = "/{userId}/images", produces = MediaType.IMAGE_GIF_VALUE) // 複數
    public byte[] getImage(@PathVariable Long userId) {
        return userService.findImg(userId);
    }
}

