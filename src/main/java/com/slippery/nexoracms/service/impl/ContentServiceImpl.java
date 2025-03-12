package com.slippery.nexoracms.service.impl;

import com.slippery.nexoracms.dto.ContentDto;
import com.slippery.nexoracms.models.Content;
import com.slippery.nexoracms.repository.ContentRepository;
import com.slippery.nexoracms.repository.UserRepository;
import com.slippery.nexoracms.service.ContentService;
import com.slippery.nexoracms.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ContentServiceImpl(ContentRepository repository, UserService userService, UserRepository userRepository) {
        this.repository = repository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public ContentDto createNewContent(Content content, Long userId) {
        ContentDto response =new ContentDto();
        if(content.getBody() ==null || content.getBody().isEmpty()
                ||content.getTitle() ==null||content.getTitle().isEmpty()
                ||content.getStatus()==null||content.getStatus().isEmpty()
        ){
            response.setMessage("Cannot create content due to missing credentials");
        }
        var existingUser =userService.findById(userId);
        if(existingUser.getStatusCode() !=200){
            response.setMessage(existingUser.getMessage());
            response.setStatusCode(existingUser.getStatusCode());
            return response;
        }
        var blogs =existingUser.getUser().getUserBlogs();
        content.setPublishedOn(LocalDateTime.now());
        content.setSlug(content.getTitle().toLowerCase().replace(" ","-"));
        content.setAuthor(existingUser.getUser());
        content.setImagesInContent(new ArrayList<>());
        repository.save(content);
        blogs.add(content);
        existingUser.getUser().setUserBlogs(blogs);
        userRepository.save(existingUser.getUser());
        response.setMessage("New blog created");
        response.setStatusCode(201);
        return response;
    }

    @Override
    public ContentDto getContentById(Long contentId) {
        ContentDto response =new ContentDto();
        var content =repository.findById(contentId);
        if(content.isEmpty()){
            response.setMessage("Content not found");
            response.setStatusCode(404);
            return response;
        }
        response.setContent(content.get());
        response.setMessage("Content with id "+contentId);
        response.setStatusCode(200);
        return response;
    }

    @Override
    public ContentDto deleteContentById(Long contentId, Long userId) {
        ContentDto response =new ContentDto();
        var content =getContentById(contentId);
        var user =userService.findById(userId);
        if(content.getStatusCode() !=200){
            return content;
        }
        if(user.getStatusCode() !=200){
            response.setMessage(user.getMessage());
            response.setStatusCode(user.getStatusCode());
            return response;
        }
        if(!user.getUser().getId().equals(content.getContent().getId())){
            response.setMessage("Blog does not belong to the user");
            response.setStatusCode(401);
            return response;
        }
        var blogs = user.getUser().getUserBlogs();
        content.getContent().setAuthor(null);
        repository.delete(content.getContent());
        blogs.remove(content.getContent());
        user.getUser().setUserBlogs(blogs);
        userRepository.save(user.getUser());
        response.setMessage("BLOG deleted");
        response.setStatusCode(200);

        return response;
    }

    @Override
    public ContentDto getAllContentForUser(Long userId) {
        ContentDto response =new ContentDto();
        var userBlogs =userRepository.findById(userId);
        if(userBlogs.isEmpty()){
            response.setMessage("User not found");
            response.setStatusCode(404);
            return response;
        }
        response.setContents(userBlogs.get().getUserBlogs());
        response.setMessage("All contents by "+ userBlogs.get().getUsername());
        response.setStatusCode(200);
        return response;
    }

    @Override
    public ContentDto getAllContent() {
        ContentDto response =new ContentDto();
        var content =repository.findAll();
        if(content.isEmpty()){
            response.setStatusCode(404);
            response.setMessage("No blogs available for you");
            return response;
        }
        response.setContents(content);
        response.setMessage("All content");
        response.setStatusCode(200);
        return response;
    }


    @Override
    public ContentDto updateContent(Content content, Long userId, Long contentId) {
        return null;
    }
}
