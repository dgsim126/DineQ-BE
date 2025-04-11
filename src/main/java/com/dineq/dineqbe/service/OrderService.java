package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.domain.enums.OrderStatus;
import com.dineq.dineqbe.dto.menu.OrderResponseDTO;
import com.dineq.dineqbe.repository.TableOrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final TableOrderRepository tableOrderRepository;

    public OrderService(TableOrderRepository tableOrderRepository) {
        this.tableOrderRepository = tableOrderRepository;
    }


    // status에 따른 주문 내역 확인
    public List<OrderResponseDTO> getOrderByStatus(String status) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("주문 상태 값이 비어 있습니다.");
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<TableOrderEntity> tableOrderEntities = tableOrderRepository.findByStatus(orderStatus);

            List<OrderResponseDTO> result = new ArrayList<>();

            for (TableOrderEntity entity : tableOrderEntities) {
                OrderResponseDTO dto = OrderResponseDTO.fromEntity(entity);
                result.add(dto);
            }

            return result;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태: " + status);
        }

    }

    // 요청된주문 -> 요리중인 주문
    public void acceptOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId가 비어있음");
        }

        TableOrderEntity tableOrderEntity= tableOrderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("orderId "+ orderId +"에 해당되는 주문이 존재하지 않음"));

        if(tableOrderEntity.getStatus()!=OrderStatus.valueOf("REQUESTED")){
            throw new IllegalArgumentException("상태가 REQUESTED가 아닌 주문을 변경할 수 없습니다");
        }


        tableOrderEntity.setStatus(OrderStatus.valueOf("COOKING"));
        tableOrderRepository.save(tableOrderEntity);
    }

    // 요리중인 주문 -> 완료된 주문
    public void completeOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId가 비어있음");
        }

        TableOrderEntity tableOrderEntity= tableOrderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("orderId "+ orderId +"에 해당되는 주문이 존재하지 않음"));

        if(tableOrderEntity.getStatus()!=OrderStatus.valueOf("COOKING")){
            throw new IllegalArgumentException("상태가 COOKING이 아닌 주문을 변경할 수 없습니다");
        }

        tableOrderEntity.setStatus(OrderStatus.valueOf("COMPLETED"));
        tableOrderRepository.save(tableOrderEntity);
    }

    // orderId에 해당하는 주문내역 지우기
    public void cancelOneOrder(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId가 비어있음");
        }

        TableOrderEntity tableOrderEntity= tableOrderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("orderId "+ orderId +"에 해당되는 주문이 존재하지 않음"));

        tableOrderRepository.delete(tableOrderEntity);
    }

    // groupNum에 해당하는 주문내역 지우기
    public void cancelAll(String groupNum) {
        if(groupNum == null) {
            throw new IllegalArgumentException("groupNum이 비어있음");
        }

        List<TableOrderEntity> tableOrderEntities = tableOrderRepository.findAllByGroupNum(groupNum);

        if(tableOrderEntities.isEmpty()) {
            throw new IllegalArgumentException(groupNum + " groupNum에 해당하는 주문내역이 존재하지 않음");
        }
        tableOrderRepository.deleteAll(tableOrderEntities);
    }
}
