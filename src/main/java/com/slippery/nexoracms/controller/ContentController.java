package com.slippery.nexoracms.controller;

import com.slippery.nexoracms.dto.ContentDto;
import com.slippery.nexoracms.models.Content;
import com.slippery.nexoracms.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/content")
@CrossOrigin
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }
    @PostMapping("/create/blog/{userId}")
    public ResponseEntity<ContentDto> createNewContent(@RequestPart MultipartFile image, @RequestPart Content content, @PathVariable Long userId, @RequestParam String category) {
        var createdBlog =contentService.createNewContent(image,content, userId,category);
        if(createdBlog.getStatusCode() !=201){
            return ResponseEntity.status(createdBlog.getStatusCode()).body(createdBlog);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
    }
    @GetMapping("/{contentId}/get")
    public ResponseEntity<ContentDto> getContentById(@PathVariable Long contentId) {
        var foundContent =contentService.getContentById(contentId);
        if(foundContent.getStatusCode() !=200){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(foundContent);
        }
        return ResponseEntity.status(HttpStatus.OK).body(foundContent);
    }

    @DeleteMapping("/{contentId}/delete")
    public ResponseEntity<ContentDto> deleteContentById(@PathVariable Long contentId,@RequestParam Long userId) {
        var deleteContent =contentService.deleteContentById(contentId, userId);
        if(deleteContent.getStatusCode() !=200){
            return ResponseEntity.status(HttpStatusCode.valueOf(deleteContent.getStatusCode())).body(deleteContent);
        }

        return ResponseEntity.ok(deleteContent);
    }

    @GetMapping("/{userId}/all-user-blogs")
    public ResponseEntity<ContentDto> getAllContentForUser(@PathVariable Long userId) {
        var userBlogs =contentService.getAllContentForUser(userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(userBlogs.getStatusCode())).body(userBlogs);
    }
    @GetMapping("/all-blogs")
    public ResponseEntity<ContentDto> getAllContent() {
        var allBlogs =contentService.getAllContent();
        return ResponseEntity.status(HttpStatusCode.valueOf(allBlogs.getStatusCode())).body(allBlogs);
    }
    @PutMapping("/{contentId}/update")
    public ResponseEntity<ContentDto> updateContent(@RequestBody Content content,@RequestParam Long userId,@PathVariable Long contentId) {
        var updatedBlog =contentService.updateContent(content, userId, contentId);
        return ResponseEntity.status(HttpStatusCode.valueOf(updatedBlog.getStatusCode())).body(updatedBlog);
    }
    @PostMapping("/create/without-image/{userId}")
    public ResponseEntity<ContentDto> createNewContentWithoutImage(@RequestBody Content content,@PathVariable Long userId,@RequestParam String category){
        var createdBlog =contentService.createNewContentWithoutImage(content, userId, category);
        return ResponseEntity.status(HttpStatusCode.valueOf(createdBlog.getStatusCode())).body(createdBlog);
    }
}
