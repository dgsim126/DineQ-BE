package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.domain.entity.CategoryEntity;
import com.dineq.dineqbe.dto.category.CategoryPriorityUpdateRequestDTO;
import com.dineq.dineqbe.dto.category.CategoryRequestDTO;
import com.dineq.dineqbe.dto.category.CategoryResponseDTO;
import com.dineq.dineqbe.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 모든 카테고리 조회
     * GET /api/v1/store/categories
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    /**
     * 카테고리 추가
     * POST /api/v1/store/categories
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
     * PUT /api/v1/store/categories/{categoryId}
     * @param categoryId
     * @param categoryRequestDTO
     * @return status(200), status(400)
     */
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        try{
            categoryService.updateCategory(categoryId, categoryRequestDTO);
            return ResponseEntity.ok("Category updated successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 카테고리 삭제
     * DELETE /api/v1/store/categories/{categoryId}
     * @param categoryId
     * @return status(200),
     */
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try{
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("Category deleted successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 카테고리 우선순위 변경
     * PUT /api/v1/store/categories/sort
     * @param request
     * @return
     */
    @PutMapping("/categories/sort")
    public ResponseEntity<String> updatePriorities(@RequestBody CategoryPriorityUpdateRequestDTO request) {
        categoryService.updateCategory(request.getPriorities());
        return ResponseEntity.status(200).body("Category updated successfully");
    }

}
