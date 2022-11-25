package com.foodive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class CategoryPageDTO {
    private int categoryCnt;
    private List<CategoryVO> list;
}
