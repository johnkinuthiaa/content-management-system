package com.slippery.nexoracms.service;

import com.slippery.nexoracms.dto.CategoryDto;
import com.slippery.nexoracms.models.Category;

public interface CategoryService {
    CategoryDto createNewCategory(Category category);
    CategoryDto updateCategory(Category category);
    CategoryDto findCategoryById(Long categoryId);
    CategoryDto findAllCategories();
    CategoryDto deleteCategoryById(Long categoryId);
}
