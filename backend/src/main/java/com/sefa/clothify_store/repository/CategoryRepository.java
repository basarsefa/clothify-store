package com.sefa.clothify_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sefa.clothify_store.entity.Category;

@Repository 
public interface CategoryRepository extends JpaRepository<Category, Integer>{

    
} 