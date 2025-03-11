package com.slippery.nexoracms.service.impl;

import com.slippery.nexoracms.dto.CategoryDto;
import com.slippery.nexoracms.models.Category;
import com.slippery.nexoracms.repository.CategoryRepository;
import com.slippery.nexoracms.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createNewCategory(Category category) {
        CategoryDto response =new CategoryDto();
        if(category.getDescription() ==null || category.getDescription().isEmpty()
                ||category.getName() ==null ||category.getName().isEmpty()
        ){
            response.setMessage("Cannot create category due to missing fields");
            response.setStatusCode(300);
            return response;
        }
        Category existingCategory =categoryRepository.findByName(category.getName());
        if(existingCategory !=null){
            response.setMessage("Category with the name already exists");
            response.setStatusCode(300);
            return response;
        }
        categoryRepository.save(category);
        response.setMessage("new category "+category+" created");
        response.setStatusCode(201);
        response.setCategory(category);
        return response;
    }

    @Override
    public CategoryDto updateCategory(Long userId, Category category) {
        return null;
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        CategoryDto response =new CategoryDto();
        Optional<Category> existingCategory =categoryRepository.findById(categoryId);
        if(existingCategory.isEmpty()){
            response.setMessage("The category with id "+categoryId+" does not exist! ");
            response.setStatusCode(404);
            return response;
        }
        response.setCategory(existingCategory.get());
        response.setMessage("Category with id "+categoryId);
        response.setStatusCode(200);
        return response;
    }

    @Override
    public CategoryDto findAllCategories() {
        return null;
    }

    @Override
    public CategoryDto deleteCategoryById(Long userId, Long categoryId) {
        return null;
    }
}
