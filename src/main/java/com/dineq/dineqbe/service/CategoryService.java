package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.CategoryEntity;
import com.dineq.dineqbe.dto.category.CategoryRequestDTO;
import com.dineq.dineqbe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 카테고리 추가
    public void addCategory(CategoryRequestDTO categoryRequestDTO) {

        boolean isCategory= categoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName());

        if(isCategory){
            throw new IllegalArgumentException("Category= '" + categoryRequestDTO.getCategoryName() + "' already exists.");
        }

        CategoryEntity categoryEntity = new CategoryEntity(categoryRequestDTO.getCategoryName(), categoryRequestDTO.getCategoryDesc(), 0);
        categoryRepository.save(categoryEntity);
    }

    // 카테고리 수정
    public void updateCategory(Integer categoryId, CategoryRequestDTO categoryRequestDTO) {

        boolean isCategory= categoryRepository.existsByCategoryId(categoryId);

        if(!isCategory){
            throw new IllegalArgumentException("Category= '" + categoryRequestDTO.getCategoryName() + "' does not exist.");
        }

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).get();

        if(categoryRequestDTO.getCategoryName()!=null){
            categoryEntity.setCategoryName(categoryRequestDTO.getCategoryName());
        }
        if(categoryRequestDTO.getCategoryDesc()!=null){
            categoryEntity.setCategoryDesc(categoryRequestDTO.getCategoryDesc());
        }

        categoryEntity.setCreatedAt(LocalDateTime.now());
        categoryRepository.save(categoryEntity);
    }

    // 카테고리 삭제
    public void deleteCategory(Integer categoryId) {

        boolean isCategory= categoryRepository.existsByCategoryId(categoryId);
        if(!isCategory){
            throw new IllegalArgumentException("Category= '" + categoryId + "' does not exist.");
        }
        categoryRepository.deleteById(categoryId);
    }
}
