package com.dineq.dineqbe.service;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    private final DiningTableRepository diningTableRepository;

    public TableService(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    public void addTable() {
        DiningTableEntity diningTableEntity = new DiningTableEntity();
        diningTableRepository.save(diningTableEntity);
    }
}
