package com.slippery.nexoracms.service;

import com.slippery.nexoracms.dto.ContentDto;
import com.slippery.nexoracms.dto.MediaDto;
import com.slippery.nexoracms.models.Content;
import org.springframework.web.multipart.MultipartFile;

public interface ContentService {
    ContentDto createNewContent(MultipartFile image,Content content,Long userId,String category);
    ContentDto getContentById(Long contentId);
    ContentDto deleteContentById(Long contentId,Long userId);
    ContentDto getAllContentForUser(Long userId);
    ContentDto getAllContent();
    ContentDto updateContent(Content content,Long userId,Long contentId);
    ContentDto createNewContentWithoutImage(Content content,Long userId,String category);

}
