package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.dto.customer.MenuListResponseDTO;
import com.dineq.dineqbe.dto.customer.MenuResponseDTO;
import com.dineq.dineqbe.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
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
     * [GET] /api/v1/menus
     * @return
     */
    public List<MenuListResponseDTO> getAllMenus() {
        List<MenuEntity> menuEntities = menuRepository.findAll();
        return menuEntities.stream()
                .map(MenuListResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 메뉴 조회
     * [GET] /api/v1/menus/{menuId}
     * @param menuId
     * @return
     */
    public MenuResponseDTO getMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .map(MenuResponseDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 존재하지 않습니다."));
    }
}
