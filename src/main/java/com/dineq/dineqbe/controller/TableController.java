package com.dineq.dineqbe.controller;

import com.dineq.dineqbe.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/tables")
    public ResponseEntity<String> addTable() {
        tableService.addTable();
        return ResponseEntity.ok("Table added successfully");
    }

    @DeleteMapping("/tables")
    public ResponseEntity<String> deleteTable() {
        tableService.deleteTable();
        return ResponseEntity.ok("Table deleted successfully");
    }
}
