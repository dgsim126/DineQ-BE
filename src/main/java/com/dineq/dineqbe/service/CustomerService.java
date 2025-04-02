package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.dto.customer.MenuListResponseDTO;
import com.dineq.dineqbe.dto.customer.MenuResponseDTO;
import com.dineq.dineqbe.dto.customer.OrderItemDTO;
import com.dineq.dineqbe.dto.customer.TableOrderRequestDTO;
import com.dineq.dineqbe.repository.DiningTableRepository;
import com.dineq.dineqbe.repository.MenuRepository;
import com.dineq.dineqbe.repository.TableOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.dineq.dineqbe.domain.enums.OrderStatus;


import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final MenuRepository menuRepository;
    private final DiningTableRepository diningTableRepository;
    private final TableOrderRepository tableOrderRepository;

    public CustomerService(MenuRepository menuRepository,
                           DiningTableRepository diningTableRepository,
                           TableOrderRepository tableOrderRepository) {
        this.menuRepository = menuRepository;
        this.diningTableRepository = diningTableRepository;
        this.tableOrderRepository = tableOrderRepository;
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

    /**
     * 장바구니 메뉴 주문
     * [POST] /api/v1/orders
     * @param request
     * @return
     */
    public String createOrder(TableOrderRequestDTO request) {
        // 1. 주문 그룹 식별자 생성
        String groupNum = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 2. 테이블 조회
        DiningTableEntity table = diningTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new EntityNotFoundException("해당 테이블이 존재하지 않습니다."));

        // 3. 주문 항목 저장
        for (OrderItemDTO item : request.getOrders()) {
            MenuEntity menu = menuRepository.findById(item.getMenuId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 존재하지 않습니다."));

            TableOrderEntity order = TableOrderEntity.builder()
                    .diningTable(table)
                    .menu(menu)
                    .quantity(item.getQuantity())
                    .totalPrice(menu.getMenuPrice() * item.getQuantity())
                    .status(OrderStatus.REQUESTED) // 초기값 REQUESTED로 설정
                    .orderTime(LocalDateTime.now())
                    .groupNum(groupNum)
                    .build();

            tableOrderRepository.save(order);
        }

        return groupNum;
    }

}
