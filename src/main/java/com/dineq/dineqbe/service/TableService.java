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
        Long maxTableNumber = diningTableRepository.findMaxTableNumber();
        Long nextTableNumber = maxTableNumber + 1;

        DiningTableEntity diningTableEntity = new DiningTableEntity(null, nextTableNumber, null);
        diningTableRepository.save(diningTableEntity);
    }

    public void deleteTable() {
        DiningTableEntity target= diningTableRepository.findTopByMaxTableNumber();
        if(target==null) {
            throw new IllegalArgumentException("삭제할 테이블 존재하지 않음");
        }
        diningTableRepository.delete(target);
    }
}
