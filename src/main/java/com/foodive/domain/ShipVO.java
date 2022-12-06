package com.foodive.domain;

import lombok.*;

import java.util.Date;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ShipVO {
    private Long sno;
    private Long ono;
    @NonNull
    private String name;
    @NonNull
    private String zipcode;
    @NonNull
    private String address1;
    private String address2;
    @NonNull
    private String phone;

    private Date orderDate;
}
