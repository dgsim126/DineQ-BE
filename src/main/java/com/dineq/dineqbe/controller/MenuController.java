package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.MenuRequestDTO;
import com.dineq.dineqbe.dto.menu.MenuResponseDTO;
import com.dineq.dineqbe.dto.menu.MenuUpdateRequestDTO;
import com.dineq.dineqbe.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 메뉴 추가
     * POST /api/v1/store/menus
     * @param menuRequestDTO
     * @return
     */
    @PostMapping("/menus")
    public ResponseEntity<String> addMenu(@RequestBody MenuRequestDTO menuRequestDTO) {
        try{
            menuService.addMenu(menuRequestDTO);
            return ResponseEntity.ok("Menu added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 메뉴 수정
     * PUT /api/v1/store/menus/{menuId}
     * @param menuId
     * @param menuRequestDTO
     * @return
     */
    @PutMapping("/menus/{menuId}")
    public ResponseEntity<String> updateMenu(@PathVariable Long menuId, @RequestBody MenuUpdateRequestDTO menuRequestDTO) {
        try{
            menuService.updateMenu(menuId, menuRequestDTO);
            return ResponseEntity.ok("Menu updated successfully");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 메뉴 삭제
     * DELETE /api/v1/store/menus/{menuId}
     * @param menuId
     * @return
     */
    @DeleteMapping("menus/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menuId) {
        try{
            menuService.deleteMenu(menuId);
            return ResponseEntity.ok("Menu deleted successfully");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
