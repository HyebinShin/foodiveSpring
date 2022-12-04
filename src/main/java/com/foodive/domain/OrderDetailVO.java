package com.foodive.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailVO {
    private Long dno;
    private Long ono;
    private Long pno;
    private String korName;
    private Integer qty;
    private Integer totalPrice;

    private Date orderDate;
}
