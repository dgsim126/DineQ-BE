package com.dineq.dineqbe.service;

import com.dineq.dineqbe.dto.menu.MenuResponseDTO;
import com.dineq.dineqbe.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    
    // 모든 메뉴 조회
    public List<MenuResponseDTO> getAllMenu() {
        return menuRepository.findAll().stream()
                .map(menu -> new MenuResponseDTO(
                        menu.getMenuId(),
                        menu.getMenuName(),
                        menu.getMenuPrice(),
                        menu.isOnSale(),
                        menu.getMenuPriority(),
                        menu.getMenuInfo(),
                        menu.getMenuImage(),
                        menu.getCategory().getCategoryId() // CategoryEntity가 연결된 경우
                ))
                .collect(Collectors.toList());
    }
}
