package com.dineq.dineqbe.dto.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuPriorityUpdateRequestDTO {
    private List<MenuPriorityRequestDTO> priorities;
}
