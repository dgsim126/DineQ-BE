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
                createMenu(2L, "속이 꽉 찬 교자만두튀김 with 감자튀김", 15900, "10pcs", 18),

                // 볶음, 구이류
                createMenu(3L, "마파두부 & 꽃빵", 20900, "", 19),
                createMenu(3L, "고추잡채 & 꽃빵", 21900, "", 20),
                createMenu(3L, "순살 치즈 불닭", 22900, "", 21),
                createMenu(3L, "대패 삼겹구이 & 비빔면", 19900, "", 22),
                createMenu(3L, "제육 두부김치", 21900, "", 23),
                createMenu(3L, "우삼겹 숙주볶음", 19900, "", 24),
                createMenu(3L, "고추장 육회 비빔면", 20900, "비빔면 2인분 + 육회 200g + 계란 노른자", 25),
                createMenu(3L, "오돌뼈 & 주먹밥", 18900, "", 26),
                createMenu(3L, "모듬 소세지 & 감자튀김", 18900, "", 27),
                createMenu(3L, "새우 듬뿍 감바스", 20900, "바게트 8조각 포함", 28),
                createMenu(3L, "쉬림프 오일 파스타", 16900, "바게트 2조각 포함", 29),

                // 디저트, 사이드
                createMenu(4L, "파인애플 사베트", 8900, "", 30),
                createMenu(4L, "아이스 황도", 10900, "", 31),
                createMenu(4L, "첵스 초코 아이스크림", 12900, "", 32),
                createMenu(4L, "망고 바닐라 아이스크림", 12900, "", 33),
                createMenu(4L, "짜계치", 10900, "짜파게티 + 치즈 + 계란후라이", 34),
                createMenu(4L, "파송송 계란탁 치즈라면", 5000, "계란 한 알, 체다치즈 한 장 들어갑니다 :)", 34),
                createMenu(4L, "마약 콘치즈", 9900, "", 35),
                createMenu(4L, "혈당 폭발 프렌치 토스트", 11900, "설탕 + 연유가 뿌려져 나갑니다 :)", 36),
                createMenu(4L, "스팸 김치 볶음밥", 8900, "", 37),
                createMenu(4L, "간장 계란밥", 5000, "밥 + 후라이2장 + 김가루 + 간장 + 참기름", 38),
                createMenu(4L, "김가루 주먹밥", 4000, "", 39),
                createMenu(4L, "라면사리 추가", 2000, "", 40),
                createMenu(4L, "우동사리 추가", 2000, "", 41),
                createMenu(4L, "계란후라이 1장", 1000, "", 41),
                createMenu(4L, "공기밥", 1000, "", 41),

                // 덮밥류
                createMenu(5L, "마파두부덮밥", 10900, "", 42),
                createMenu(5L, "대패제육덮밥", 10900, "", 43),
                createMenu(5L, "오므라이스", 9900, "", 44),
                createMenu(5L, "치킨마요덮밥", 9900, "", 45),
                createMenu(5L, "스팸데리마요덮밥", 9900, "", 46),

                // 주류
                createMenu(6L, "진로토닉워터", 2000, "", 47),
                createMenu(6L, "진로토닉워터 제로", 2000, "", 48),
                createMenu(6L, "진로토닉워터 홍차", 2000, "", 49),

                createMenu(6L, "참이슬", 5000, "", 50),
                createMenu(6L, "진로", 5000, "", 51),
                createMenu(6L, "새로", 5000, "", 52),
                createMenu(6L, "별빛 청하", 5500, "", 53),

                createMenu(6L, "카스", 5000, "", 54),
                createMenu(6L, "테라", 5000, "", 55),
                createMenu(6L, "켈리", 5000, "", 56),

                createMenu(6L, "장수 막걸리", 4500, "", 57),
                createMenu(6L, "꿀 막걸리", 5000, "", 58),

                createMenu(6L, "생맥주 500cc", 5000, "", 59),
                createMenu(6L, "생맥주 1700cc", 13000, "", 60),
                createMenu(6L, "자몽 생맥 500cc", 5500, "", 61),

                createMenu(6L, "슬러시 소주 1잔 320ml", 4900, "", 62),

                createMenu(6L, "핑크레몬 칵테일 소주 1잔 320ml", 4900, "핑크레몬", 63),
                createMenu(6L, "청포도 칵테일 소주 1잔 320ml", 4900, "청포도", 64),
                createMenu(6L, "자두 칵테일 소주 1잔 320ml", 4900, "자두", 65),
                createMenu(6L, "자몽 칵테일 소주 1잔 320ml", 4900, "자몽", 66),

                createMenu(6L, "핑크레몬 칵테일 소주 1병 750ml", 10900, "핑크레몬", 67),
                createMenu(6L, "청포도 칵테일 소주 1병 750ml", 10900, "청포도", 68),
                createMenu(6L, "자두 칵테일 소주 1병 750ml", 10900, "자두", 69),
                createMenu(6L, "자몽 칵테일 소주 1병 750ml", 10900, "자몽", 70),

                // 음료
                createMenu(7L, "파인 슬러시 1잔", 3000, "파인", 71),
                createMenu(7L, "소다 슬러시 1잔", 3000, "소다", 72),

                createMenu(7L, "초코에몽", 1500, "", 73),
                createMenu(7L, "코카콜라 355ml", 2000, "", 74),
                createMenu(7L, "펩시 제로 콜라 355ml", 2000, "", 75),
                createMenu(7L, "제로 스프라이트 355ml", 2000, "", 76),
                createMenu(7L, "칠성사이다 355ml", 2000, "", 77),
                createMenu(7L, "청포도 에이드 500ml", 3900, "", 78),
                createMenu(7L, "자몽 에이드 500ml", 3900, "", 79),
                createMenu(7L, "자두 에이드 500ml", 3900, "", 80),
                createMenu(7L, "핑크레몬 에이드 500ml", 3900, "", 81),
                createMenu(7L, "복숭아 에이드 500ml", 3900, "", 82),
                createMenu(7L, "복숭아 아이스티 500ml", 3900, "", 83),
                createMenu(7L, "아이스 아메리카노 500ml", 3500, "", 84),
                createMenu(7L, "아샷추 (아이스티+샷추가) 500ml", 4500, "", 85),
                createMenu(7L, "아망추 (아이스티+망고) 500ml", 4900, "", 86),
                createMenu(7L, "아이스 매실차 500ml", 3900, "", 87),
                createMenu(7L, "매실 에이드 500ml", 4500, "", 88)

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

