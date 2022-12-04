package com.foodive.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ShipVO {
    private Long sno;
    private Long ono;
    private String name;
    private String zipcode;
    private String address;
    private String phone;

    private Date orderDate;
}
