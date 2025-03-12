package com.slippery.nexoracms.controller;

import com.slippery.nexoracms.dto.UserDto;
import com.slippery.nexoracms.models.User;
import com.slippery.nexoracms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController{
    private final UserService  service;

    public UserController(UserService service) {
        this.service = service;
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> createNewUser(@RequestBody User user) {
        var newUser =service.createNewUser(user);
        if(newUser.getStatusCode() !=200){
            return ResponseEntity.status(newUser.getStatusCode()).body(newUser);
        }
        return ResponseEntity.ok(newUser);
    }
    @PutMapping("/login")
    public UserDto login(@RequestBody User user) {
        return null;
    }

    public UserDto findById(Long userId) {
        return null;
    }

    public UserDto deleteById(Long userId) {
        return null;
    }

    public UserDto getAllBlogsByUser(Long userId) {
        return null;
    }
}
