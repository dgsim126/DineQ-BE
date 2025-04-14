package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.*;
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
     * @param request
     * @return
     */
    @PostMapping("/menus")
    public ResponseEntity<String> addMenu(@RequestBody MenuRequestDTO request) {
        try{
            menuService.addMenu(request);
            return ResponseEntity.ok("Menu added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * 메뉴 수정
     * PUT /api/v1/store/menus/{menuId}
     * @param menuId
     * @param request
     * @return
     */
    @PutMapping("/menus/{menuId}")
    public ResponseEntity<String> updateMenu(@PathVariable Long menuId, @RequestBody MenuUpdateRequestDTO request) {
        try{
            menuService.updateMenu(menuId, request);
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

    /**
     * 메뉴 우선순위 변경
     * PUT /api/v1/store/menus/sort
     * @param request
     * @return
     */
    @PutMapping("/menus/sort")
    public ResponseEntity<String> sortMenu(@RequestBody MenuPriorityUpdateRequestDTO request) {
        menuService.updatePriorities(request.getPriorities());
        return ResponseEntity.status(200).body("Menu sorted successfully");
    }

    @PutMapping("/menus/{menuId}/available")
    public ResponseEntity<String> availableMenu(@PathVariable Long menuId, @RequestBody MenuAvailableRequestDTO request) {
        try{
            menuService.changeAvailable(menuId, request);
            return ResponseEntity.status(200).body("Menu available successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }
}
