package com.dineq.dineqbe.dummy;


import com.dineq.dineqbe.domain.entity.CategoryEntity;
import com.dineq.dineqbe.domain.entity.DiningTableEntity;
import com.dineq.dineqbe.domain.entity.MenuEntity;
import com.dineq.dineqbe.repository.CategoryRepository;
import com.dineq.dineqbe.repository.DiningTableRepository;
import com.dineq.dineqbe.repository.MenuRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateDummyData {

    private final DiningTableRepository diningTableRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;

    @PostConstruct
    @Transactional
    public void insertDummyData() {
        // 데이터 중복 방지 (이미 존재하면 삽입 안 함)
        if (diningTableRepository.count() == 0) {
            insertTable1Data();
        }

        if (categoryRepository.count() == 0) {
            insertCategoryData();
        }

        if (menuRepository.count() == 0) {
            insertMenuData();
        }
    }

    private void insertTable1Data() {
        for (int i = 0; i < 8; i++) {
            diningTableRepository.save(new DiningTableEntity()); // ID는 자동 증가
        }
        System.out.println("DiningTable 데이터 삽입 완료!");
    }

    private void insertCategoryData() {
        List<CategoryEntity> categories = List.of(
                new CategoryEntity("탕, 찌개", "국물 요리", 1),
                new CategoryEntity("전, 튀김", "부침 요리", 2),
                new CategoryEntity("볶음, 구이", "볶거나 구운 요리", 3),
                new CategoryEntity("디저트, 사이드", "후식 및 곁들임 요리", 4),
                new CategoryEntity("음료", "마시는 음료", 5),
                new CategoryEntity("주류", "술 종류", 6)
        );
        categoryRepository.saveAll(categories);
        System.out.println("CATEGORY 데이터 삽입 완료!");
    }

    private void insertMenuData() {
        List<MenuEntity> menus = List.of(
                new MenuEntity(1L, "김치찌개", 8000, "돼지고기와 김치가 들어간 얼큰한 찌개", 1, null, true),
                new MenuEntity(1L, "된장찌개", 7500, "구수한 된장과 두부가 들어간 찌개", 2, null, true),
                new MenuEntity(1L, "순두부찌개", 8500, "매콤한 순두부와 해물이 들어간 찌개", 3, null, true),
                new MenuEntity(2L, "김치전", 7000, "매콤한 김치가 들어간 바삭한 전", 4, null, true),
                new MenuEntity(2L, "해물파전", 12000, "해물과 파가 들어간 바삭한 전", 5, null, true),
                new MenuEntity(2L, "오징어튀김", 8000, "바삭한 오징어 튀김", 6, null, true),
                new MenuEntity(3L, "제육볶음", 9000, "매콤한 돼지고기 볶음", 7, null, true),
                new MenuEntity(3L, "오징어볶음", 9500, "매콤한 오징어 볶음", 8, null, true),
                new MenuEntity(3L, "닭갈비", 10000, "매콤한 양념 닭볶음", 9, null, true),
                new MenuEntity(4L, "팥빙수", 7000, "달콤한 팥과 얼음이 어우러진 디저트", 10, null, true),
                new MenuEntity(4L, "호떡", 4000, "달콤한 시럽이 들어간 전통 간식", 11, null, true),
                new MenuEntity(4L, "붕어빵", 3000, "달콤한 팥이 들어간 길거리 간식", 12, null, true),
                new MenuEntity(5L, "아메리카노", 4000, "진한 에스프레소와 물이 어우러진 커피", 13, null, true),
                new MenuEntity(5L, "카페라떼", 4500, "부드러운 우유가 들어간 커피", 14, null, true),
                new MenuEntity(5L, "녹차라떼", 5000, "진한 녹차와 우유의 조화", 15, null, true),
                new MenuEntity(6L, "소주", 5000, "한국에서 가장 인기 있는 증류주", 16, null, true),
                new MenuEntity(6L, "맥주", 6000, "시원하고 청량한 보리 맥주", 17, null, true),
                new MenuEntity(6L, "막걸리", 7000, "부드럽고 달콤한 전통 막걸리", 18, null, true)
        );
        menuRepository.saveAll(menus);
        System.out.println("MENU 데이터 삽입 완료!");
    }
}

