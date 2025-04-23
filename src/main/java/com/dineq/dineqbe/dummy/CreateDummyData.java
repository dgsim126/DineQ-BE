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
        int maxTables = 50; // 최대 50개
        int activeCount = 20; // 20개 활성화

        for (int i = 1; i <= maxTables; i++) {
            DiningTableEntity table = DiningTableEntity.builder()
                    .tableNumber((long) i)
                    .activated(i <= activeCount)  // 앞 20개는 true, 나머지는 false
                    .build();
            diningTableRepository.save(table);
        }
        System.out.println("DiningTable 데이터 삽입 완료!");
    }

    private void insertCategoryData() {
        List<CategoryEntity> categories = List.of(
                new CategoryEntity("탕 / 찌개류", "국물 요리", 1),
                new CategoryEntity("전 / 튀김류", "부침 요리", 2),
                new CategoryEntity("볶음 / 구이류", "볶거나 구운 요리", 3),
                new CategoryEntity("디저트 / 사이드", "후식 및 곁들임 요리", 4),
                new CategoryEntity("덮밥류", "마시는 음료", 5),
                new CategoryEntity("주류", "술 종류", 6),
                new CategoryEntity("음료", "마시는 음료", 7)
        );
        categoryRepository.saveAll(categories);
        System.out.println("CATEGORY 데이터 삽입 완료!");
    }

    private void insertMenuData() {
        List<MenuEntity> menus = List.of(
                // 탕, 찌개류
                createMenu(1L, "돈까스 김치나베", 21900, "우동사리 포함", 1),
                createMenu(1L, "왕푸짐 부대찌개", 22900, "라면사리 포함", 2),
                createMenu(1L, "해물가득 나가사끼짬뽕", 23900, "우동사리 포함", 3),
                createMenu(1L, "떡오뎅탕(삼진어묵)", 16900, "", 4),
                createMenu(1L, "된장술밥", 15900, "", 5),
                createMenu(1L, "물만두 계란탕", 15900, "", 6),
                createMenu(1L, "홍합탕", 15900, "", 7),

                // 전, 튀김류
                createMenu(2L, "김피탕(김치피자탕수육)", 20900, "", 8),
                createMenu(2L, "치즈 김치전", 11900, "치즈전 + 치즈", 9),
                createMenu(2L, "대왕 치즈 계란말이", 11900, "", 10),
                createMenu(2L, "통오징어튀김 & 감자튀김", 15900, "", 11),
                createMenu(2L, "버터갈릭 감자튀김", 11900, "", 12),
                createMenu(2L, "어니언 양념감자", 11900, "", 13),
                createMenu(2L, "순살치킨 후라이드", 16900, "", 14),
                createMenu(2L, "순살치킨 허니버터", 17900, "", 15),
                createMenu(2L, "순살치킨 고추맛", 17900, "", 16),
                createMenu(2L, "순살치킨 닭강정", 17900, "", 17),
                createMenu(2L, "속이 꽉 찬 교자만두튀김 with 감자튀김", 15900, "10pcs", 18)



        );
        menuRepository.saveAll(menus);
        System.out.println("MENU 데이터 삽입 완료!");
    }
    private MenuEntity createMenu(Long categoryId, String name, int price, String info, int priority) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음: " + categoryId));

        return new MenuEntity(
                category,
                name,
                price,
                info,
                priority,
                null,   // 이미지 없음
                true    // onSale = true
        );
    }
}

