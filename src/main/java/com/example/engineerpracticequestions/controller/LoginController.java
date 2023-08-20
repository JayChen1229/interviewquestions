package com.example.engineerpracticequestions.controller;



import com.example.engineerpracticequestions.model.User;
import com.example.engineerpracticequestions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password){
        User theUser = userService.findUser(email, password);
        if (theUser != null) {
            return ResponseEntity.ok(theUser);
        }
        return ResponseEntity.ok(null);  // Return null here
    }


    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody User user){

        User theUser = userService.findUser(user.getEmail(), user.getPassword());
        if(theUser != null){
            return ResponseEntity.ok(false);
        }

        boolean saved = userService.saveUser(user);
        return ResponseEntity.ok(saved);
    }
}

