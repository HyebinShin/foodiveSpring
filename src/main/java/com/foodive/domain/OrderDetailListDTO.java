package com.foodive.domain;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailListDTO {
    private List<OrderDetailVO> detailList;
    private Integer sumPrice;
}
