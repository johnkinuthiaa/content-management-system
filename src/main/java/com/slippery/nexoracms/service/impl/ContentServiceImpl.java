package com.slippery.nexoracms.service.impl;

import com.slippery.nexoracms.dto.ContentDto;
import com.slippery.nexoracms.dto.MediaDto;
import com.slippery.nexoracms.models.Content;
import com.slippery.nexoracms.models.Media;
import com.slippery.nexoracms.repository.CategoryRepository;
import com.slippery.nexoracms.repository.ContentRepository;
import com.slippery.nexoracms.repository.UserRepository;
import com.slippery.nexoracms.service.ContentService;
import com.slippery.nexoracms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ContentServiceImpl(ContentRepository repository, UserService userService, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ContentDto createNewContent(MultipartFile image,Content content, Long userId,String category) {
        try{
            if(image ==null||image.isEmpty()){
                content.setImagesInContent(null);
            }else{
                Media media =new Media();
                media.setFileData(image.getBytes());
                media.setFileName(image.getOriginalFilename());
                media.setFileType(image.getContentType());
                content.setImagesInContent(media);
            }
        } catch (Exception e) {
            log.warn("warning {}",e.getLocalizedMessage());
        }
        return validContent(content, userId, category);

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

        if(!user.getUser().getId().equals(content.getContent().getAuthor().getId())){
            response.setMessage("Blog does not belong to the user");
            response.setStatusCode(401);
            return response;
        }

        var blogs = user.getUser().getUserBlogs();
        blogs.remove(content.getContent());
        user.getUser().setUserBlogs(blogs);
        content.getContent().setAuthor(null);
        var category =content.getContent().getCategory();
        var itemsInCategory =category.getContentInCategory();
        itemsInCategory.remove(content.getContent());
        category.setContentInCategory(itemsInCategory);
        categoryRepository.save(category);
        userRepository.save(user.getUser());
        repository.delete(content.getContent());
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

    @Override
    public ContentDto createNewContentWithoutImage(Content content, Long userId, String category) {
        content.setImagesInContent(null);
        return validContent(content, userId, category);
    }
    public ContentDto validContent(Content content, Long userId, String category){
        ContentDto response =new ContentDto();
        if(content.getBody() == null || content.getBody().isEmpty()
                || content.getTitle() == null || content.getTitle().isEmpty()
                || content.getStatus() == null || content.getStatus().isEmpty()
                || category ==null||category.isEmpty()
        ){
            response.setMessage("Cannot create content due to missing credentials");
            response.setStatusCode(300);
            return response;
        }
        var existingUser =userService.findById(userId);
        if(existingUser.getStatusCode() !=200){
            response.setMessage(existingUser.getMessage());
            response.setStatusCode(existingUser.getStatusCode());
            return response;
        }

        var existingCategory =categoryRepository.findByName(category.strip());
        if(existingCategory ==null){
            response.setMessage("Category with the name"+ category+" does not exist");
            response.setStatusCode(404);
            return response;
        }

        var blogs =existingUser.getUser().getUserBlogs();
        content.setPublishedOn(LocalDateTime.now());
        content.setSlug(content.getTitle().toLowerCase().replace(" ","-"));
        content.setAuthor(existingUser.getUser());

        content.setCategory(existingCategory);
        repository.save(content);

        var contentsInCategory =existingCategory.getContentInCategory();
        contentsInCategory.add(content);
        existingCategory.setContentInCategory(contentsInCategory);
        categoryRepository.save(existingCategory);

        blogs.add(content);
        existingUser.getUser().setUserBlogs(blogs);
        userRepository.save(existingUser.getUser());
        response.setMessage("New blog created");
        response.setStatusCode(201);
        return response;
    }
}
