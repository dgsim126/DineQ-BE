package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.customer.MenuListResponseDTO;
import com.dineq.dineqbe.dto.customer.MenuResponseDTO;
import com.dineq.dineqbe.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 메뉴 전체 조회
     * GET /api/v1/menus
     * @return
     */
    @GetMapping("/menus")
    public ResponseEntity<List<MenuListResponseDTO>> getAllMenus() {

        List<MenuListResponseDTO> menuList = customerService.getAllMenus();
        return ResponseEntity.ok(menuList);
    }

    /**
     * 특정 메뉴 조회
     * GET /api/v1/menus/{menuId}
     * @param menuId
     * @return
     */
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<?> getMenuById(@PathVariable Long menuId) {

        try {
            MenuResponseDTO menu = customerService.getMenuById(menuId);
            return ResponseEntity.ok(menu);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }
}
