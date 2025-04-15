package com.dineq.dineqbe.repository;

import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTableEntity, Long>  {

    // 테이블 추가용
    // 비활성화된 테이블 중 가장 앞 번호
    Optional<DiningTableEntity> findFirstByActivatedFalseOrderByTableNumberAsc();

    // 테이블 삭제용
    // 활성화된 테이블 중 가장 뒤 번호
    Optional<DiningTableEntity> findFirstByActivatedTrueOrderByTableNumberDesc();

}
