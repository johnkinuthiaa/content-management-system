package com.slippery.nexoracms.service.impl;

import com.slippery.nexoracms.repository.ContentRepository;
import com.slippery.nexoracms.service.ContentService;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository repository;

    public ContentServiceImpl(ContentRepository repository) {
        this.repository = repository;
    }
}
