package com.foodive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class ProductPageDTO {
    private int productCnt;
    private List<ProductVO> list;
}
