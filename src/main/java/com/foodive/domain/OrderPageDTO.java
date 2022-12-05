package com.foodive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class OrderPageDTO {
    private int orderCnt;
    private List<OrderVO> list;
}
