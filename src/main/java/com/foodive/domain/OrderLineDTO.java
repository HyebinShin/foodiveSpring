package com.foodive.domain;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDTO {
    private OrderVO order;
    private List<OrderDetailVO> detailList;
    private ShipVO ship;
    private PayVO pay;

    public OrderLineDTO(OrderVO order, List<OrderDetailVO> detailList) {
        this.order = order;
        this.detailList = detailList;
    }
}
