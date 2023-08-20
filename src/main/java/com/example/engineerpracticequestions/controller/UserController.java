package com.example.engineerpracticequestions.controller;


import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

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
        try {
            User user = userService.getUserById(userId);
            if (user != null) {
                userService.updateUserCoverImage(userId, image.getBytes());
            } else {
                throw new RuntimeException("Error: User is null for user id: " + userId);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image for user id: " + userId, e);
        }
    }


    @PutMapping("/{userId}/biographies")
    public void updateBiography(@PathVariable Long userId, @RequestParam String biography) {
        // 驗證並清理用戶輸入
        String sanitizedBiography = HtmlUtils.htmlEscape(biography);

        User user = userService.getUserById(userId);
        if (user != null) {
            userService.updateUserBiography(userId, sanitizedBiography);
        } else {
            throw new RuntimeException("Error: User is null for user id: " + userId);
        }
    }

    @GetMapping(value = "/{userId}/images", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getImage(@PathVariable Long userId) {
        return userService.findImg(userId);
    }
}

