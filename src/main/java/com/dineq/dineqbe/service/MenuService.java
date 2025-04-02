package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.dto.menu.MenuRequestDTO;
import com.dineq.dineqbe.dto.menu.MenuResponseDTO;
import com.dineq.dineqbe.dto.menu.MenuUpdateRequestDTO;
import com.dineq.dineqbe.repository.CategoryRepository;
import com.dineq.dineqbe.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }
    
    // 모든 메뉴 조회
    public List<MenuResponseDTO> getAllMenu() {
        return menuRepository.findAll().stream()
                .map(menu -> new MenuResponseDTO(
                        menu.getMenuId(),
                        menu.getMenuName(),
                        menu.getMenuPrice(),
                        menu.getOnSale(),
                        menu.getMenuPriority(),
                        menu.getMenuInfo(),
                        menu.getMenuImage(),
                        menu.getCategory().getCategoryId() // CategoryEntity가 연결된 경우
                ))
                .collect(Collectors.toList());
    }

    // 메뉴 추가
    // 프론트로부터 올바르지 않은 카테고리id를 받지 않을 것이라 가정하여 개발됨(검증 x), 문제 시 검증할 것
    public void addMenu(MenuRequestDTO menuRequestDTO) {

        boolean isMenu= menuRepository.existsByMenuName(menuRequestDTO.getMenuName());

        if(isMenu){
            throw new IllegalArgumentException("Menu= '" + menuRequestDTO.getMenuName() + "' already exists.");
        }

        MenuEntity menuEntity = new MenuEntity(
                menuRequestDTO.getCategoryId(),
                menuRequestDTO.getMenuName(),
                menuRequestDTO.getMenuPrice(),
                menuRequestDTO.getMenuInfo(),
                menuRequestDTO.getMenuPriority(),
                menuRequestDTO.getMenuImage(),
                menuRequestDTO.getOnSale()
        );

        menuRepository.save(menuEntity);
    }

    // 메뉴 수정
    public void updateMenu(Long menuId, MenuUpdateRequestDTO menuRequestDTO) {

        boolean isMenu= menuRepository.existsByMenuId(menuId);

        if(!isMenu){
            throw new IllegalArgumentException("Menu= '" + menuRequestDTO.getMenuName() + "' does not exist.");
        }

        MenuEntity menuEntity = menuRepository.findById(menuId).get();

        if(menuRequestDTO.getMenuName()!=null){
            menuEntity.setMenuName(menuRequestDTO.getMenuName());
        }
        if(menuRequestDTO.getMenuPrice()!=null){
            menuEntity.setMenuPrice(menuRequestDTO.getMenuPrice());
        }
        if(menuRequestDTO.getMenuInfo()!=null){
            menuEntity.setMenuInfo(menuRequestDTO.getMenuInfo());
        }
        if(menuRequestDTO.getMenuImage()!=null){
            menuEntity.setMenuImage(menuRequestDTO.getMenuImage());
        }

        // 변경된 시간 추가하려고 했는데 Entity에 속성 없음

        menuRepository.save(menuEntity);
    }
}
