package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.CategoryEntity;
import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.dto.menu.*;
import com.dineq.dineqbe.repository.CategoryRepository;
import com.dineq.dineqbe.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final ImageStorageService imageStorageService;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository, ImageStorageService imageStorageService) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.imageStorageService = imageStorageService;
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
    public void addMenu(MenuRequestDTO menuRequestDTO, MultipartFile image) throws IOException {

        boolean isMenu= menuRepository.existsByMenuName(menuRequestDTO.getMenuName());

        if(isMenu){
            throw new IllegalArgumentException("Menu= '" + menuRequestDTO.getMenuName() + "' already exists.");
        }

        CategoryEntity category = categoryRepository.findById(menuRequestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid categoryId: " + menuRequestDTO.getCategoryId()));

        String imageUrl= (image!=null && !image.isEmpty())
                ? imageStorageService.saveImage(image)
                : null;

        Integer priority= menuRequestDTO.getMenuPriority();
        if(priority==null){
            Integer maxPriority= menuRepository.findMaxMenuPriority();
            priority= (maxPriority!=null ? maxPriority : 0)+1;
        }

        MenuEntity menuEntity = new MenuEntity(
                category,
                menuRequestDTO.getMenuName(),
                menuRequestDTO.getMenuPrice(),
                menuRequestDTO.getMenuInfo(),
                priority,
                imageUrl,
                menuRequestDTO.getOnSale()
        );

        menuRepository.save(menuEntity);
    }

    // 메뉴 수정
    public void updateMenu(Long menuId, MenuUpdateRequestDTO menuRequestDTO, MultipartFile image) throws IOException {

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

        if (image != null && !image.isEmpty()) {
            String imageUrl = imageStorageService.saveImage(image);
            menuEntity.setMenuImage(imageUrl);
        }

        // 변경된 시간 추가하려고 했는데 Entity에 속성 없음

        menuRepository.save(menuEntity);
    }

    // 메뉴 삭제
    public void deleteMenu(Long menuId) {
        boolean isMenu= menuRepository.existsByMenuId(menuId);

        if(!isMenu){
            throw new IllegalArgumentException("Menu= '" + menuId + "' does not exist.");
        }

        menuRepository.deleteById(menuId);
    }

    // 메뉴 우선순위 변경
    @Transactional
    public void updatePriorities(List<MenuPriorityRequestDTO> priorities) {
        // Step 1: 대상 menuId에 해당하는 메뉴들 전부 조회
        List<Long> menuIds = priorities.stream()
                .map(MenuPriorityRequestDTO::getMenuId)
                .toList();

        List<MenuEntity> menuEntities = menuRepository.findAllById(menuIds);

        // Step 2: 임시값 (-1)으로 우선순위 초기화 (중복 회피)
        int tempPriority = -1;
        for (MenuEntity menu : menuEntities) {
            menu.setMenuPriority(tempPriority--);
        }
        menuRepository.saveAll(menuEntities);
        menuRepository.flush();

        // Step 3: 실제 우선순위 반영
        for (MenuEntity menu : menuEntities) {
            priorities.stream()
                    .filter(p -> p.getMenuId().equals(menu.getMenuId()))
                    .findFirst()
                    .ifPresent(p -> menu.setMenuPriority(p.getMenuPriority()));
        }

        menuRepository.saveAll(menuEntities); // 최종 저장
    }

    // 메뉴 판매 여부 변경
    @Transactional
    public void changeAvailable(Long menuId, MenuAvailableRequestDTO request) {
        MenuEntity menuEntity = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다. menuId=" + menuId));

        menuEntity.setOnSale(request.getOn_sale());

    }
}
