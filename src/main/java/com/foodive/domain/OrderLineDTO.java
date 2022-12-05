package com.foodive.domain;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderLineDTO {
    @NonNull
    private OrderVO order;
    @NonNull
    private List<OrderDetailVO> detailList;
    private ShipVO ship;
    private PayVO pay;
}
