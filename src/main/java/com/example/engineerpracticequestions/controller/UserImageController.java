package com.example.engineerpracticequestions.controller;

import com.example.engineerpracticequestions.service.PostService;
import com.example.engineerpracticequestions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserImageController {
    private final UserService userService;

    @Autowired
    public UserImageController(final UserService userService) {
        this.userService = userService;
    }

    // 用於指定 {imgUrl} 只能由數字組成。 [0-9] 表示匹配一個數字字符，+ 表示匹配前面的表達式一次或多次。
    @GetMapping(value = "/img/users/{imgUrl:[0-9]+}", produces = MediaType.IMAGE_GIF_VALUE) // 複數
    public byte[] getPhoto(@PathVariable("imgUrl") Long id) {
        return userService.findImg(id);
    }
}
