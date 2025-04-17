package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.dto.customer.MenuListResponseDTO;
import com.dineq.dineqbe.dto.customer.MenuResponseDTO;
import com.dineq.dineqbe.dto.customer.TableOrderRequestDTO;
import com.dineq.dineqbe.dto.customer.TableOrderResponseDTO;
import com.dineq.dineqbe.service.CustomerService;
import com.dineq.dineqbe.service.QRService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;
    private final QRService qrService;

    public CustomerController(CustomerService customerService, QRService qrService) {
        this.customerService = customerService;
        this.qrService = qrService;
    }

    /**
     * 메뉴 전체 조회
     * [GET] /api/v1/menus
     * @return status 200
     */
    @GetMapping("/menus")
    public ResponseEntity<List<MenuListResponseDTO>> getAllMenus() {
        List<MenuListResponseDTO> menuList = customerService.getAllMenus();
        return ResponseEntity.ok(menuList);
    }

    /**
     * 특정 메뉴 조회
     * [GET] /api/v1/menus/{menuId}
     * @param menuId
     * @return status 200, status 404, status 500
     */
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<?> getMenuById(@PathVariable Long menuId) {
        try {
            MenuResponseDTO menu = customerService.getMenuById(menuId);
            return ResponseEntity.ok(menu);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다.");
        }
    }

    /**
     * 장바구니 메뉴 주문
     * [POST] /api/v1/orders
     * @param request
     * @return status 200, status 404, status 500
     */
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody TableOrderRequestDTO request,
                                         @RequestHeader("token") String token,
                                         @RequestHeader("tableId") String tableId) {

        try {
            qrService.verifyToken(token, tableId);
            if(request.getTableId().toString().equals(tableId)) {
                String groupNum = customerService.createOrder(request);
                return ResponseEntity.ok().body("주문이 완료되었습니다. 주문 번호: " + groupNum);
            }else{
                return ResponseEntity.status(400).body("요청한 tableId와 헤더에 담긴 tableId가 일치하지 않음");
            }
        } catch (IllegalArgumentException e) { // "헤더에 값이 비어있음"
            return ResponseEntity.status(401).body(e.getMessage());

        }catch(AuthenticationCredentialsNotFoundException e){ // "헤더에 담긴 token, tableId에 해당하는 튜플이 존재하지 않음"
            return ResponseEntity.status(500).body(e.getMessage());

        } catch (IllegalStateException e) {  // "body에 값이 없음(장바구니에 메뉴를 담지 않음)"
            return ResponseEntity.status(501).body(e.getMessage());

        }catch (EntityNotFoundException e) { // "존재하지 않는 테이블, 메뉴가 body에 들어왔을 때"
            return ResponseEntity.status(502).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(503).body("주문 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 주문 내역 조회
     * [GET] api/v1/orders/{tableId}
     * @param tableId
     * @return status 200, status 404, status 500
     */
    @GetMapping("/orders/{tableId}")
    public ResponseEntity<?> getOrdersByTable(@PathVariable Long tableId,
                                              @RequestHeader("token") String token,
                                              @RequestHeader("tableId") String tableID) {
        try {
            qrService.verifyToken(token, tableID);
            if(tableId.toString().equals(tableID)) {
                List<List<TableOrderResponseDTO>> groupedOrders = customerService.getOrdersByTableId(tableId);
                return ResponseEntity.ok(groupedOrders);
            }
            return ResponseEntity.status(400).body("요청한 tableId와 헤더에 담긴 tableId가 일치하지 않음");
        } catch (IllegalArgumentException e) { // "헤더에 값이 비어있음"
            return ResponseEntity.status(401).body(e.getMessage());

        }catch(AuthenticationCredentialsNotFoundException e) { // "헤더에 담긴 token, tableId에 해당하는 튜플이 존재하지 않음"
            return ResponseEntity.status(500).body(e.getMessage());

        } catch (EntityNotFoundException e){
            return ResponseEntity.status(501).body(e.getMessage()); // "해당 테이블의 주문 내역이 없습니다."

        } catch (Exception e) {
            return ResponseEntity.status(503).body("주문 처리 중 오류가 발생했습니다.");
        }
    }

}
