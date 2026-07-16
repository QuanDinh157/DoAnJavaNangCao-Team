package com.example.DoAnJava.service;

import com.example.DoAnJava.entity.Category;
import com.example.DoAnJava.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }


    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }


    public Category getCategoryById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }
}