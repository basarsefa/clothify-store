package com.sefa.clothify_store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sefa.clothify_store.repository.CategoryRepository;
import com.sefa.clothify_store.entity.Category;

@Service 
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
}
