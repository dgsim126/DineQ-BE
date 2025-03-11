package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.category.CategoryRequestDTO;
import com.dineq.dineqbe.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 추가
     * @param categoryRequestDTO
     * @return status(200), status(409)
     */
    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        try{
            categoryService.addCategory(categoryRequestDTO);
            return ResponseEntity.ok("Category added successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 카테고리 수정
     * @param categoryId
     * @param categoryRequestDTO
     * @return status(200), status(400)
     */
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Integer categoryId, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        try{
            categoryService.updateCategory(categoryId, categoryRequestDTO);
            return ResponseEntity.ok("Category updated successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
