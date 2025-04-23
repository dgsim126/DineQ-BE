package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.dto.customer.*;
import com.dineq.dineqbe.repository.DiningTableRepository;
import com.dineq.dineqbe.repository.MenuRepository;
import com.dineq.dineqbe.repository.TableOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.dineq.dineqbe.domain.enums.OrderStatus;


import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;


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
        return menuRepository.findAll().stream()
                .map(MenuListResponseDTO::fromEntity)
                .sorted(Comparator.comparing(MenuListResponseDTO::getCategoryPriority)
                        .thenComparing(MenuListResponseDTO::getMenuPriority))
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
        if (request.getOrders() == null || request.getOrders().isEmpty()) {
            throw new IllegalStateException("주문 목록이 비어 있습니다.");
        }

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

    /**
     * 주문 내역 조회
     * [GET] api/v1/orders/{tableId}
     * @param tableId
     * @return
     */
    public List<List<TableOrderResponseDTO>> getOrdersByTableId(Long tableId) {
        List<TableOrderEntity> orders = tableOrderRepository.findByDiningTable_DiningTableId(tableId);

        if (orders.isEmpty()) {
            throw new EntityNotFoundException("해당 테이블의 주문 내역이 없습니다.");
        }

        // groupNum 기준으로 묶기
        Map<String, List<TableOrderResponseDTO>> grouped = orders.stream()
                .collect(Collectors.groupingBy(
                        TableOrderEntity::getGroupNum,
                        Collectors.mapping(TableOrderResponseDTO::fromEntity, Collectors.toList())
                ));

        return grouped.values().stream()
                .sorted((g1, g2) -> g2.get(0).getOrderTime().compareTo(g1.get(0).getOrderTime()))
                .collect(Collectors.toList());
    }

}
