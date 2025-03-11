package com.slippery.nexoracms.service;

import com.slippery.nexoracms.dto.UserDto;
import com.slippery.nexoracms.models.User;

public interface UserService {
    UserDto createNewUser(User user);
    UserDto login(User user);
    UserDto findById(Long userId);
    UserDto deleteById(Long userId);
    UserDto getAllBlogsByUser(Long userId);
}
