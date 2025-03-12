package com.slippery.nexoracms.controller;

import com.slippery.nexoracms.dto.UserDto;
import com.slippery.nexoracms.models.User;
import com.slippery.nexoracms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController{
    private final UserService  service;

    public UserController(UserService service) {
        this.service = service;
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> createNewUser(@RequestBody User user) {
        var newUser =service.createNewUser(user);
        if(newUser.getStatusCode() !=201){
            return ResponseEntity.status(newUser.getStatusCode()).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @PutMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody User user) {
        var loggedInUser =service.login(user);
        if(loggedInUser.getStatusCode() !=200){
            return ResponseEntity.status(loggedInUser.getStatusCode()).body(loggedInUser);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(loggedInUser);
    }
    @GetMapping("/{userId}/get")
    public ResponseEntity<UserDto> findById(@PathVariable Long userId) {
        var user =service.findById(userId);
        if(user.getStatusCode() !=200){
            return ResponseEntity.status(user.getStatusCode()).body(user);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<UserDto> deleteById(@PathVariable Long userId) {
        var deleteUser =service.deleteById(userId);
        if(deleteUser.getStatusCode() !=200){
            return ResponseEntity.status(deleteUser.getStatusCode()).body(deleteUser);
        }
        return ResponseEntity.status(HttpStatus.OK).body(deleteUser);
    }
    @GetMapping("/{userId}/blogs")
    public ResponseEntity<UserDto> getAllBlogsByUser(@PathVariable Long userId) {
        var userBlogs =service.getAllBlogsByUser(userId);
        if(userBlogs.getStatusCode() !=200){
            return ResponseEntity.status(userBlogs.getStatusCode()).body(userBlogs);
        }
        return ResponseEntity.ok(userBlogs);
    }
}
