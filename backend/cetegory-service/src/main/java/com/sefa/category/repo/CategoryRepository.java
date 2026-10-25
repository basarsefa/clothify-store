package com.sefa.category.repo;

import com.sefa.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository 
public interface CategoryRepository extends JpaRepository<Category, Integer>{

    
} 