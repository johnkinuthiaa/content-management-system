package com.slippery.nexoracms.service.impl;

import com.slippery.nexoracms.dto.UserDto;
import com.slippery.nexoracms.models.User;
import com.slippery.nexoracms.repository.UserRepository;
import com.slippery.nexoracms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder(12);

    public UserServiceImplementation(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDto createNewUser(User user) {
        UserDto response =new UserDto();
        var validUser =userValid(user);
        if(validUser.getStatusCode() !=200){
            response.setMessage("user not created because"+validUser.getMessage());
            response.setStatusCode(validUser.getStatusCode());
            return response;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword().strip()));
        user.setUsername(user.getUsername().strip());
        user.setEmail(user.getEmail().strip());
        user.setCreatedOn(LocalDateTime.now());
        userRepository.save(user);
        response.setMessage("User saved successfully");
        response.setStatusCode(201);
        response.setUser(user);
        return response;
    }

    @Override
    public UserDto login(User user) {
        UserDto response =new UserDto();
        if(user.getPassword() ==null || user.getPassword().isEmpty()
                ||user.getEmail() ==null || user.getEmail().isEmpty())
        {
            response.setMessage("Not logged in because login credentials are missing");
            response.setStatusCode(401);
            return response;
        }
        Pattern pattern =Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        if(!pattern.matcher(user.getEmail().toLowerCase()).matches()){
            response.setMessage("Email is not valid");
            response.setStatusCode(300);
            return response;
        }
        String username =userRepository.findByEmail(user.getEmail()).getUsername();
        if(username ==null){
            response.setMessage("User not authenticated");
            response.setStatusCode(401);
            return response;
        }
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,user.getPassword()
            ));
            if(authentication.isAuthenticated()){
                response.setMessage("User authenticated successfully");
                response.setStatusCode(200);
                return response;
            }
        } catch (Exception e) {
            response.setMessage("failed to authenticate user because: "+e.getLocalizedMessage());
            response.setStatusCode(401);
        }
        return response;
    }

    @Override
    public UserDto findById(Long userId) {
        UserDto response =new UserDto();
        Optional<User> existingUser =userRepository.findById(userId);
        if(existingUser.isEmpty()){
            response.setMessage("User with id "+userId+" does not exist");
            response.setStatusCode(404);
            return response;
        }
        response.setUser(existingUser.get());
        response.setMessage("User with id"+userId);
        response.setStatusCode(200);
        return response;
    }

    @Override
    public UserDto deleteById(Long userId) {
        UserDto response =new UserDto();
        var existingUser =findById(userId);
        if(existingUser.getStatusCode() !=200){
            return existingUser;
        }
        userRepository.deleteById(userId);
        response.setMessage("User with id "+userId+" deleted");
        response.setStatusCode(200);
        return response;
    }

    @Override
    public UserDto getAllBlogsByUser(Long userId) {
        UserDto response =new UserDto();
        var existingUser =findById(userId);
        if(existingUser.getStatusCode() !=200){
            return existingUser;
        }
        var blogs = existingUser.getUser().getUserBlogs();
        if(blogs.isEmpty()){
            response.setStatusCode(404);
            response.setMessage("User has no blogs");
            return response;
        }
        response.setMessage("All blogs by "+existingUser.getUser().getUsername());
        response.setStatusCode(200);
        response.setUserBlogs(blogs);
        return response;
    }
    public UserDto userValid(User user){
        UserDto response =new UserDto();
        Pattern pattern =Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Optional<User> existingUserByUsername = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
        Optional<User> existingByEmail = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if(existingByEmail.isPresent()){
            response.setMessage("User with that email already exists");
            response.setStatusCode(401);
            return response;
        }
        if( existingUserByUsername.isPresent()){
            response.setMessage("User with that username already exists");
            response.setStatusCode(401);
            return response;
        }
        try{
            if(user.getPassword() ==null || user.getPassword().isEmpty()
                    ||user.getRole() ==null || user.getRole().isEmpty()
                    ||user.getUsername() ==null || user.getUsername().isEmpty()
                    ||user.getEmail() ==null || user.getEmail().isEmpty())
            {
                response.setMessage("Some login credentials are missing");
                response.setStatusCode(401);
                return response;
            }

            if(!pattern.matcher(user.getEmail().toLowerCase()).matches()){
                response.setMessage("Email is not valid");
                response.setStatusCode(300);
                return response;
            }
            Pattern usernamePattern =Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");
            if(!usernamePattern.matcher(user.getUsername()).matches()){
                response.setMessage("Username should be less than 20 characters and more than 3 ");
                response.setStatusCode(300);
                return response;
            }
            if(!usernamePattern.matcher(user.getPassword()).matches()){
                response.setMessage("Passwords should be less than 20 characters and more than 6");
                response.setStatusCode(300);
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(200);
        response.setMessage("User is valid");
        return response;
    }
}
