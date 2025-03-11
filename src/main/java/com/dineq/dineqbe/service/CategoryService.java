package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.CategoryEntity;
import com.dineq.dineqbe.dto.category.CategoryRequestDTO;
import com.dineq.dineqbe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public void addCategory(CategoryRequestDTO categoryRequestDTO) {
        // dto의 categoryName이 DB에 있는지 확인
        boolean isCategory= categoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName());

        if(isCategory){
            throw new IllegalArgumentException("Category= '" + categoryRequestDTO.getCategoryName() + "' already exists.");
        }

        /*
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(categoryRequestDTO.getCategoryName());
        categoryEntity.setCategoryDesc(categoryRequestDTO.getCategoryDesc());
        categoryEntity = categoryRepository.save(categoryEntity);
        */
        CategoryEntity categoryEntity = new CategoryEntity(categoryRequestDTO.getCategoryName(), categoryRequestDTO.getCategoryDesc(), 0);
        categoryRepository.save(categoryEntity);
    }
}
