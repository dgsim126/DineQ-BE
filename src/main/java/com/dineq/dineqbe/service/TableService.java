package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.domain.entity.PaymentHistoryEntity;
import com.dineq.dineqbe.domain.entity.TableOrderEntity;
import com.dineq.dineqbe.dto.table.ChangeTableRequestDTO;
import com.dineq.dineqbe.repository.DiningTableRepository;
import com.dineq.dineqbe.repository.PaymentHistoryRepository;
import com.dineq.dineqbe.repository.QRRepository;
import com.dineq.dineqbe.repository.TableOrderRepository;
import com.dineq.dineqbe.websocket.InvalidateSender;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {

    private final DiningTableRepository diningTableRepository;
    private final TableOrderRepository tableOrderRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final QRRepository qrRepository;
    private final InvalidateSender invalidateSender;

    public TableService(DiningTableRepository diningTableRepository,
                        TableOrderRepository tableOrderRepository,
                        PaymentHistoryRepository paymentHistoryRepository,
                        QRRepository qrRepository,
                        InvalidateSender invalidateSender) {
        this.diningTableRepository = diningTableRepository;
        this.tableOrderRepository = tableOrderRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.qrRepository = qrRepository;
        this.invalidateSender = invalidateSender;
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

    // 테이블 비우기
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

        qrRepository.deleteByTableId(String.valueOf(tableId));

        // 커밋 성공 후 신호 발사 (웹소켓)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                invalidateSender.sendAlert("/api/v1/store/tables/{tableId}/clear");
            }
        });
    }

    // 테이블 변경
    /*
    1. TableOrderEntity에서 diningTable의 값이 request.fromUserId에 해당하는 객체 모두 찾기
    2. 찾아온 모든 객체의 dingingTable의 값을 request.toUserId로 변경하기
     */
    @Transactional
    public void changeTable(ChangeTableRequestDTO request) {
        // 1. 테이블 ID로 각각의 DiningTableEntity 조회
        DiningTableEntity fromTable = diningTableRepository.findById(request.getFromTableId())
                .orElseThrow(() -> new EntityNotFoundException("기존 테이블을 찾을 수 없습니다: " + request.getFromTableId()));

        DiningTableEntity toTable = diningTableRepository.findById(request.getToTableId())
                .orElseThrow(() -> new EntityNotFoundException("이동할 테이블을 찾을 수 없습니다: " + request.getToTableId()));

        // 2. 기존 테이블에 속한 모든 주문 조회
        List<TableOrderEntity> orders = tableOrderRepository.findByDiningTable_DiningTableId(fromTable.getDiningTableId());

        if (orders.isEmpty()) {
            throw new IllegalStateException("기존 테이블에 주문이 존재하지 않습니다: " + request.getFromTableId());
        }

        for (TableOrderEntity order : orders) {
            order.setDiningTable(toTable);
        }

        tableOrderRepository.saveAll(orders);
        System.out.println("총 " + orders.size() + "건의 주문이 테이블 " + request.getFromTableId() + " → " + request.getToTableId() + "로 이동되었습니다.");

        // 커밋 성공 후 신호 발사 (웹소켓)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                invalidateSender.sendAlert("/api/v1/store/tables/change");
            }
        });
    }
}
