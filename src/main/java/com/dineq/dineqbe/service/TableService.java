package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.domain.entity.PaymentHistoryEntity;
import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.repository.DiningTableRepository;
import com.dineq.dineqbe.repository.PaymentHistoryRepository;
import com.dineq.dineqbe.repository.TableOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {

    private final DiningTableRepository diningTableRepository;
    private final TableOrderRepository tableOrderRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public TableService(DiningTableRepository diningTableRepository, TableOrderRepository tableOrderRepository, PaymentHistoryRepository paymentHistoryRepository) {
        this.diningTableRepository = diningTableRepository;
        this.tableOrderRepository = tableOrderRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    // 테이블 추가
    // 비활성화된 테이블 중 가장 앞번호 활성화
    @Transactional
    public String addTable() {
        DiningTableEntity inactiveTable = diningTableRepository
                .findFirstByActivatedFalseOrderByTableNumberAsc()
                .orElseThrow(() -> new IllegalStateException("더 이상 추가할 수 있는 테이블이 없습니다."));

        inactiveTable.setActivated(true);
        return inactiveTable.getTableNumber() + "번 테이블 추가";
    }

    // 테이블 삭제
    // 활성화된 테이블 중 가장 뒷번호 비활성화
    @Transactional
    public String deleteTable() {
        DiningTableEntity latestActiveTable = diningTableRepository
                .findFirstByActivatedTrueOrderByTableNumberDesc()
                .orElseThrow(() -> new IllegalStateException("삭제할 활성 테이블이 없습니다."));

        latestActiveTable.setActivated(false);
        return latestActiveTable.getTableNumber() + "번 테이블 삭제";
    }

    // 테이블 수 확인
    public long getActivatedTableCount() {
        return diningTableRepository.countByActivatedTrue();
    }

    @Transactional
    public void clearTable(Long tableId) {
        List<TableOrderEntity> tableOrderEntities= tableOrderRepository.findByDiningTable_DiningTableId(tableId);

        if(tableOrderEntities.isEmpty()){
            throw new IllegalStateException("해당 테이블에는 주문 내역이 없습니다: tableId=" + tableId);
        }

        List<PaymentHistoryEntity> paymentHistoryEntities= new ArrayList<>();

        for(TableOrderEntity tableOrderEntity:tableOrderEntities) {
            PaymentHistoryEntity paymentHistoryEntity = new PaymentHistoryEntity();

            paymentHistoryEntity.setOrderId(tableOrderEntity.getOrderId());
            paymentHistoryEntity.setMenuName(tableOrderEntity.getMenu().getMenuName());
            paymentHistoryEntity.setDiningTableId(tableOrderEntity.getDiningTable().getDiningTableId());
            paymentHistoryEntity.setQuantity(tableOrderEntity.getQuantity());
            paymentHistoryEntity.setTotalPrice(tableOrderEntity.getTotalPrice());
            paymentHistoryEntity.setPaymentTime(tableOrderEntity.getOrderTime());
            paymentHistoryEntity.setCreatedAt(LocalDateTime.now());
            paymentHistoryEntity.setGroupNum(tableOrderEntity.getGroupNum());

            paymentHistoryEntities.add(paymentHistoryEntity);
        }

        paymentHistoryRepository.saveAll(paymentHistoryEntities);
        tableOrderRepository.deleteAll(tableOrderEntities);
    }
}
