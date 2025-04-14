package com.dineq.dineqbe.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateRequestDTO {
    private String menuName;
    private Integer menuPrice;
    private String menuInfo;
    private String menuImage;
}
