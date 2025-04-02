package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.dto.customer.MenuListResponseDTO;
import com.dineq.dineqbe.dto.customer.MenuResponseDTO;
import com.dineq.dineqbe.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final MenuRepository menuRepository;

    public CustomerService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * 메뉴 전체 조회
     * GET /api/v1/menus
     * @return
     */
    public List<MenuListResponseDTO> getAllMenus() {
        List<MenuEntity> menuEntities = menuRepository.findAll();
        return menuEntities.stream()
                .map(MenuListResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
