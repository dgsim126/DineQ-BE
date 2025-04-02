package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.MenuResponseDTO;
import com.dineq.dineqbe.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class MenuController {

    private MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 모든 메뉴 조회
     * GET /api/v1/store/menus
     * @return
     */
    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<MenuResponseDTO> menus= menuService.getAllMenu();
        return ResponseEntity.ok(menus);
    }
}
