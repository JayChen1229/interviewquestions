package com.example.engineerpracticequestions.controller;



import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestParam String email, @RequestParam String password){
        User theUser = userService.login(email,password);
        if(theUser != null){
            return theUser;
        }
        System.out.println("登入失敗");
        return null;
    }

    @PostMapping("/register")
    public Boolean register(@RequestBody User user){
        User theUser = userService.findUser(user.getEmail(), user.getPassword());
        if(theUser == null){
            userService.saveUser(user);
            return true;
        }
        return false;
    }
}
