package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.customer.MenuListResponseDTO;
import com.dineq.dineqbe.dto.customer.MenuResponseDTO;
import com.dineq.dineqbe.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     */
    @GetMapping("/menus")
    public ResponseEntity<List<MenuListResponseDTO>> getAllMenus() {
        List<MenuListResponseDTO> menuList = customerService.getAllMenus();
        return ResponseEntity.ok(menuList);
    }

}
