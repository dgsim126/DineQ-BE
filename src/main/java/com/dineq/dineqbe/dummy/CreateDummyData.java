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
                createMenu(1L, "돈까스 김치나베", 21900, "우동사리 포함", 1, "http://file.smartbaedal.com/usr/menuitm/2012/07/m000111_a02_1280.jpg"),
                createMenu(1L, "왕푸짐 부대찌개", 22900, "라면사리 포함", 2, "http://imagefarm.baemin.com/smartmenuimage/image_library/20211213/r_20211213_240.jpg"),
                createMenu(1L, "해물가득 나가사끼짬뽕", 23900, "우동사리 포함", 3, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_338.jpg"),
                createMenu(1L, "떡오뎅탕(삼진어묵)", 16900, "", 4, "http://imagefarm.baemin.com/smartmenuimage/image_library/20211213/r_20211213_110.jpg"),
                createMenu(1L, "된장술밥", 15900, "", 5, "http://file.smartbaedal.com/usr/menuitm/2012/07/m000147_a01_1280.jpg"),
                createMenu(1L, "물만두 계란탕", 15900, "", 6, "http://file.smartbaedal.com/usr/menuitm/2013/1/22/m00046801221937_a02_1280.jpg"),
                createMenu(1L, "홍합탕", 15900, "", 7, ""),

                // 전, 튀김류
                createMenu(2L, "김피탕(김치피자탕수육)", 20900, "", 8, ""),
                createMenu(2L, "치즈 김치전", 11900, "치즈전 + 치즈", 9, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210422/r_431.jpg"),
                createMenu(2L, "대왕 치즈 계란말이", 11900, "", 10, "http://imagefarm.baemin.com/smartmenuimage/image_library/20211213/r_20211213_330.jpg"),
                createMenu(2L, "통오징어튀김 & 감자튀김", 15900, "", 11, "http://imagefarm.baemin.com/smartmenuimage/image_library/20240527/r_20240507_73.jpg"),
                createMenu(2L, "버터갈릭 감자튀김", 11900, "", 12, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210422/r_567.jpg"),
                createMenu(2L, "어니언 양념감자", 11900, "", 13, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_322.jpg"),
                createMenu(2L, "순살치킨 후라이드", 16900, "", 14, "http://file.smartbaedal.com/usr/menuitm/2012/07/m000051_b02_1280.jpg"),
                createMenu(2L, "순살치킨 허니버터", 17900, "", 15, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_538.jpg"),
                createMenu(2L, "순살치킨 고추맛", 17900, "", 16, "http://file.smartbaedal.com/usr/menuitm/2012/07/m000051_a01_1280.jpg"),
                createMenu(2L, "순살치킨 닭강정", 17900, "", 17, "http://imagefarm.baemin.com/smartmenuimage/image_library/20211213/r_20211213_473.jpg"),
                createMenu(2L, "속이 꽉 찬 교자만두튀김 with 감자튀김", 15900, "10pcs", 18, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_137.jpg"),

                // 볶음, 구이류
                createMenu(3L, "마파두부 & 꽃빵", 20900, "", 19, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210422/r_232.jpg"),
                createMenu(3L, "고추잡채 & 꽃빵", 21900, "", 20, ""),
                createMenu(3L, "순살 치즈 불닭", 22900, "", 21, ""),
                createMenu(3L, "대패 삼겹구이 & 비빔면", 19900, "", 22, ""),
                createMenu(3L, "제육 두부김치", 21900, "", 23, "http://file.smartbaedal.com/usr/menuitm/2012/07/m000132_b01_1280.jpg"),
                createMenu(3L, "우삼겹 숙주볶음", 19900, "", 24, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210531/r_20210531_1597.jpg"),
                createMenu(3L, "고추장 육회 비빔면", 20900, "비빔면 2인분 + 육회 200g + 계란 노른자", 25, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210531/r_20210531_989.jpg"),
                createMenu(3L, "오돌뼈 & 주먹밥", 18900, "", 26, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_251.jpg"),
                createMenu(3L, "모듬 소세지 & 감자튀김", 18900, "", 27, "http://file.smartbaedal.com/usr/menuitm/2012/07/m000267_a02_1280.jpg"),
                createMenu(3L, "새우 듬뿍 감바스", 20900, "바게트 8조각 포함", 28, "http://imagefarm.baemin.com/smartmenuimage/upload/image/2020/4/24/gambas%204_101734_870.jpg"),
                createMenu(3L, "쉬림프 오일 파스타", 16900, "바게트 2조각 포함", 29, "http://imagefarm.baemin.com/smartmenuimage/upload/image/2020/4/24/oil%20pasta2_101804_870.jpg"),

                // 디저트, 사이드
                createMenu(4L, "파인애플 사베트", 8900, "", 30, ""),
                createMenu(4L, "아이스 황도", 10900, "", 31, "http://file.smartbaedal.com/usr/menuitm/2013/1/22/m00048901221944_a01_1280.jpg"),
                createMenu(4L, "첵스 초코 아이스크림", 12900, "", 32, ""),
                createMenu(4L, "망고 바닐라 아이스크림", 12900, "", 33, ""),
                createMenu(4L, "짜계치", 10900, "짜파게티 + 치즈 + 계란후라이", 34, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210531/r_20210531_1065.jpg"),
                createMenu(4L, "파송송 계란탁 치즈라면", 5000, "계란 한 알, 체다치즈 한 장 들어갑니다 :)", 35, "http://imagefarm.baemin.com/smartmenuimage/image_library/20240527/r_20240514_141.jpg"),
                createMenu(4L, "마약 콘치즈", 9900, "", 36, ""),
                createMenu(4L, "혈당 폭발 프렌치 토스트", 11900, "설탕 + 연유가 뿌려져 나갑니다 :)", 37, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210531/r_20210531_2168.jpg"),
                createMenu(4L, "스팸 김치 볶음밥", 8900, "", 38, "http://imagefarm.baemin.com/smartmenuimage/image_library/20240206/r_20240206_26.jpg"),
                createMenu(4L, "간장 계란밥", 5000, "밥 + 후라이2장 + 김가루 + 간장 + 참기름", 39, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_891.jpg"),
                createMenu(4L, "김가루 주먹밥", 4000, "", 40, "http://file.smartbaedal.com/usr/menuitm/2013/1/21/m00011401211817_a01_1280.jpg"),
                createMenu(4L, "라면사리 추가", 2000, "", 41, ""),
                createMenu(4L, "우동사리 추가", 2000, "", 42, ""),
                createMenu(4L, "계란후라이 1장", 1000, "", 43, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210531/r_20210531_1336.jpg"),
                createMenu(4L, "공기밥", 1000, "", 44, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210422/r_446.jpg"),

                // 덮밥류
                createMenu(5L, "마파두부덮밥", 10900, "", 45, "http://imagefarm.baemin.com/smartmenuimage/image_library/20231113/r_20231113_100.jpg"),
                createMenu(5L, "대패제육덮밥", 10900, "", 46, "http://file.smartbaedal.com/usr/menuitm/2014/6/3/m00052606031335_b01_1280.jpg"),
                createMenu(5L, "오므라이스", 9900, "", 47, "http://imagefarm.baemin.com/smartmenuimage/image_library/20211213/r_20211213_739.jpg"),
                createMenu(5L, "치킨마요덮밥", 9900, "", 48, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210531/r_20210531_446.jpg"),
                createMenu(5L, "스팸데리마요덮밥", 9900, "", 49, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_215.jpg"),

                // 주류
                createMenu(6L, "진로토닉워터", 2000, "", 50, "https://sitem.ssgcdn.com/50/99/50/item/1000070509950_i1_1200.jpg"),
                createMenu(6L, "진로토닉워터 제로", 2000, "", 51, "http://imagefarm.baemin.com/smartmenuimage/image_library/20240605/r_20240605_74.jpg"),
                createMenu(6L, "진로토닉워터 홍차", 2000, "", 52, "http://imagefarm.baemin.com/smartmenuimage/image_library/20240605/r_20240605_76.jpg"),

                createMenu(6L, "참이슬", 5000, "", 53, "https://handokmall.de/thumbnail/6f/86/94/1713275971/18947_1920x1920.png?ts=1713275971"),
                createMenu(6L, "진로", 5000, "", 54, "https://handokmall.de/thumbnail/8c/79/42/1713210423/13531_1920x1920.jpg?ts=1713210424"),
                createMenu(6L, "새로", 5000, "", 55, "https://handokmall.de/thumbnail/12/80/92/1742801473/saero%20soju_1920x1920.webp?ts=1742801474"),
                createMenu(6L, "별빛 청하", 5500, "", 56, "https://i.namu.wiki/i/ioXkM_PJvacyNitfsjiq4z6v-RTJQGH3ZvueEBmh_0EPI8wAm5F4KzD6WQfz0fdfXjR-Xg6I76tOSXwr9JyjtaKDsyIiZCjcE5HLCKmScNcy1mCztq1y_Kvef2SaSNbj94Bz3bbmI8es-814J7cZMw.webp"),

                createMenu(6L, "카스", 5000, "", 57, "https://img.khan.co.kr/news/2021/08/02/l_2021080201000188600015961.webp"),
                createMenu(6L, "테라", 5000, "", 58, "http://dnmart.co.kr/attach/main_img_2/0_8801119770011_main.jpg"),
                createMenu(6L, "켈리", 5000, "", 59, "https://dimg.donga.com/wps/NEWS/IMAGE/2023/04/18/118890387.1.jpg"),

                createMenu(6L, "장수 막걸리", 4500, "", 60, "http://jsmakgeolli.kwk114.com/data/file/gallery/2001168095_PLUufa2F_1.png"),
                createMenu(6L, "꿀 막걸리", 5000, "", 61, ""),

                createMenu(6L, "생맥주 500cc", 5000, "", 62, ""),
                createMenu(6L, "생맥주 1700cc", 13000, "", 63, ""),
                createMenu(6L, "자몽 생맥 500cc", 5500, "", 64, ""),

                createMenu(6L, "슬러시 소주 1잔 320ml", 4900, "", 65, ""),

                createMenu(6L, "핑크레몬 칵테일 소주 1잔 320ml", 4900, "핑크레몬", 66, ""),
                createMenu(6L, "청포도 칵테일 소주 1잔 320ml", 4900, "청포도", 67, ""),
                createMenu(6L, "자두 칵테일 소주 1잔 320ml", 4900, "자두", 68, ""),
                createMenu(6L, "자몽 칵테일 소주 1잔 320ml", 4900, "자몽", 69, ""),

                createMenu(6L, "핑크레몬 칵테일 소주 1병 750ml", 10900, "핑크레몬", 70, ""),
                createMenu(6L, "청포도 칵테일 소주 1병 750ml", 10900, "청포도", 71, ""),
                createMenu(6L, "자두 칵테일 소주 1병 750ml", 10900, "자두", 72, ""),
                createMenu(6L, "자몽 칵테일 소주 1병 750ml", 10900, "자몽", 73, ""),

                // 음료
                createMenu(7L, "파인 슬러시 1잔", 3000, "파인", 74, ""),
                createMenu(7L, "소다 슬러시 1잔", 3000, "소다", 75, ""),

                createMenu(7L, "초코에몽", 1500, "", 76, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_841.jpg"),
                createMenu(7L, "코카콜라 355ml", 2000, "", 77, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210422/r_18.jpg"),
                createMenu(7L, "펩시 제로 콜라 355ml", 2000, "", 78, "http://imagefarm.baemin.com/smartmenuimage/image_library/20231020/r_20231020_98.jpg"),
                createMenu(7L, "제로 스프라이트 355ml", 2000, "", 79, "http://imagefarm.baemin.com/smartmenuimage/image_library/20231020/r_20231020_109.jpg"),
                createMenu(7L, "칠성사이다 355ml", 2000, "", 80, "http://imagefarm.baemin.com/smartmenuimage/image_library/20210422/r_2.jpg"),
                createMenu(7L, "청포도 에이드 500ml", 3900, "", 81, "http://imagefarm.baemin.com/smartmenuimage/upload/image/2020/4/24/green%20grape2_113044_870.jpg"),
                createMenu(7L, "자몽 에이드 500ml", 3900, "", 82, "http://imagefarm.baemin.com/smartmenuimage/upload/image/2020/4/24/grapefruit2_112942_870.jpg"),
                createMenu(7L, "자두 에이드 500ml", 3900, "", 83, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_1073.jpg"),
                createMenu(7L, "핑크레몬 에이드 500ml", 3900, "", 84, "http://imagefarm.baemin.com/smartmenuimage/upload/image/2020/4/27/lemonade1_135128_870.jpg"),
                createMenu(7L, "복숭아 에이드 500ml", 3900, "", 85, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_1005.jpg"),
                createMenu(7L, "복숭아 아이스티 500ml", 3900, "", 86, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_998.jpg"),
                createMenu(7L, "아이스 아메리카노 500ml", 3500, "", 87, "http://imagefarm.baemin.com/smartmenuimage/upload/image/2020/4/24/Americano2_110057_870.jpg"),
                createMenu(7L, "아샷추 (아이스티+샷추가) 500ml", 4500, "", 88, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_878.jpg"),
                createMenu(7L, "아망추 (아이스티+망고) 500ml", 4900, "", 89, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_998.jpg"),
                createMenu(7L, "아이스 매실차 500ml", 3900, "", 90, "http://imagefarm.baemin.com/smartmenuimage/image_library/20221214/r_20221214_727.jpg"),
                createMenu(7L, "매실 에이드 500ml", 4500, "", 91, "http://imagefarm.baemin.com/smartmenuimage/image_library/20220630/r_20220630_878.jpg")

        );
        menuRepository.saveAll(menus);
        System.out.println("MENU 데이터 삽입 완료!");
    }
    private MenuEntity createMenu(Long categoryId, String name, int price, String info, int priority, String imagePath) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음: " + categoryId));

        return new MenuEntity(
                category,
                name,
                price,
                info,
                priority,
                imagePath,
                true    // onSale = true
        );
    }
}
