package com.foodive.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderVO {
    // order table
    private Long ono;
    private String id;
    private Integer totalPrice;
    private Integer state; // 결제 대기 : 0, 주문 접수 : 1, 배송 중 : 2, 배송 완료 : 3
    private Date orderDate;

    public OrderVO(String id, Integer totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }
}
