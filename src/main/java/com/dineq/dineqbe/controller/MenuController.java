package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.menu.MenuRequestDTO;
import com.dineq.dineqbe.dto.menu.MenuUpdateRequestDTO;
import com.dineq.dineqbe.dto.menu.MenuPriorityUpdateRequestDTO;
import com.dineq.dineqbe.dto.menu.MenuAvailableRequestDTO;
import com.dineq.dineqbe.dto.menu.MenuResponseDTO;
import com.dineq.dineqbe.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class MenuController {

    private final MenuService menuService;
    private final ObjectMapper objectMapper;

    public MenuController(MenuService menuService, ObjectMapper objectMapper) {
        this.menuService = menuService;
        this.objectMapper = objectMapper;
    }

    /**
     * 모든 메뉴 조회
     * GET /api/v1/store/menus
     * @return
     */
    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenu());
    }

    /**
     * 메뉴 추가
     * POST /api/v1/store/menus
     * @param
     * @return
     */
    @PostMapping(value = "/menus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addMenu(
            @RequestParam("menu") String menuJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            MenuRequestDTO request = objectMapper.readValue(menuJson, MenuRequestDTO.class);
            menuService.addMenu(request, image);
            return ResponseEntity.ok("Menu added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * 메뉴 수정
     * PUT /api/v1/store/menus/{menuId}
     * @param menuId
     * @param
     * @return
     */
    @PutMapping(value = "/menus/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMenu(
            @PathVariable Long menuId,
            @RequestParam(value = "menu", required = false) String menuJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            MenuUpdateRequestDTO request = (menuJson != null && !menuJson.isBlank())
                    ? objectMapper.readValue(menuJson, MenuUpdateRequestDTO.class)
                    : new MenuUpdateRequestDTO(); // 빈 DTO

            menuService.updateMenu(menuId, request, image);
            return ResponseEntity.ok("Menu updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * 메뉴 삭제
     * DELETE /api/v1/store/menus/{menuId}
     * @param menuId
     * @return
     */
    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menuId) {
        try {
            menuService.deleteMenu(menuId);
            return ResponseEntity.ok("Menu deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
        return ResponseEntity.ok("Menu sorted successfully");
    }

    /**
     * 메뉴 활성화, 비활성화
     * @param menuId
     * @param request
     * @return
     */
    @PutMapping("/menus/{menuId}/available")
    public ResponseEntity<String> availableMenu(@PathVariable Long menuId, @RequestBody MenuAvailableRequestDTO request) {
        try {
            menuService.changeAvailable(menuId, request);
            return ResponseEntity.ok("Menu availability updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
