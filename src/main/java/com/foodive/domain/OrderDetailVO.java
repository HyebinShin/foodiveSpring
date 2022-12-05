package com.foodive.domain;

import lombok.*;

import java.util.Date;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class OrderDetailVO {
    private Long dno;
    private Long ono;
    @NonNull
    private Long pno;
    @NonNull
    private String korName;
    @NonNull
    private Integer qty;
    @NonNull
    private Integer totalPrice;

    private Date orderDate;
}
