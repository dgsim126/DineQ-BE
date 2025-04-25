package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.customer.TableOrderResponseDTO;
import com.dineq.dineqbe.dto.table.ChangeTableRequestDTO;
import com.dineq.dineqbe.service.CustomerService;
import com.dineq.dineqbe.service.TableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class TableController {

    private final TableService tableService;
    private final CustomerService customerService;

    public TableController(TableService tableService, CustomerService customerService) {
        this.tableService = tableService;
        this.customerService = customerService;
    }

    /**
     * 테이블 추가 (비활성화된 테이블 중 가장 앞번호 활성화)
     * POST /api/v1/store/tables/add
     * @return
     */
    @PostMapping("/tables/add")
    public ResponseEntity<String> addTable() {
        String message = tableService.addTable();
        return ResponseEntity.ok(message);
    }

    /**
     * 테이블 삭제 (활성화된 테이블 중 가장 뒷번호 비활성화)
     * POST /api/v1/store/tables/delete
     * @return
     */
    @PostMapping("/tables/delete")
    public ResponseEntity<String> deleteTable() {
        String message = tableService.deleteTable();
        return ResponseEntity.ok(message);
    }

    /**
     * 테이블 수 확인
     * GET /api/v1/store/tables/count
     * @return
     */
    @GetMapping("/tables/count")
    public ResponseEntity<Long> getActivatedTableCount() {
        long count = tableService.getActivatedTableCount();
        return ResponseEntity.ok(count);
    }

    /**
     * 테이블 별 주문 내역 조회
     * GET /api/v1/store/orders/{tableId}
     * @param tableId
     * @return
     */
    @GetMapping("/orders/{tableId}")
    public ResponseEntity<?> getOrdersForOwner(@PathVariable Long tableId) {
        try {
            List<List<TableOrderResponseDTO>> groupedOrders = customerService.getOrdersByTableId(tableId);
            return ResponseEntity.ok(groupedOrders);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("테이블 주문 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 테이블 비우기(테이블 비우고, 기록)
     * POST /api/v1/store/tables/{tableId}/clear
     * @param tableId
     * @return
     */
    @PostMapping("tables/{tableId}/clear")
    public ResponseEntity<String> clearTable(@PathVariable Long tableId) {
        try{
            tableService.clearTable(tableId);
            return ResponseEntity.ok("Table cleared successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("tables/change")
    public ResponseEntity<String> changeTable(@RequestBody ChangeTableRequestDTO request) {
        try{
            tableService.changeTable(request);
            return ResponseEntity.ok("Table changed");
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
