package com.slippery.nexoracms.controller;

import com.slippery.nexoracms.dto.CategoryDto;
import com.slippery.nexoracms.models.Category;
import com.slippery.nexoracms.service.CategoryService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createNewCategory(@RequestBody Category category) {
        var createdCategory =categoryService.createNewCategory(category);
        return ResponseEntity.status(HttpStatusCode.valueOf(createdCategory.getStatusCode())).body(createdCategory);
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        return null;
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable Long categoryId) {
        var categoryWithId =categoryService.findCategoryById(categoryId);
        return ResponseEntity.status(HttpStatusCode.valueOf(categoryWithId.getStatusCode())).body(categoryWithId);
    }
    @GetMapping("/all")
    public ResponseEntity<CategoryDto> findAllCategories() {
        var allCategories =categoryService.findAllCategories();
        return ResponseEntity.status(HttpStatusCode.valueOf(allCategories.getStatusCode())).body(allCategories);
    }
    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<CategoryDto> deleteCategoryById(@PathVariable Long categoryId) {
        var deleteCategory =categoryService.deleteCategoryById(categoryId);

        return ResponseEntity.status(HttpStatusCode.valueOf(deleteCategory.getStatusCode())).body(deleteCategory);
    }
}
