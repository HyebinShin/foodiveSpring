package com.foodive.domain;

import lombok.Data;

import java.util.Date;

@Data
public class PayVO {
    private Long payNo;
    private Long ono;
    private String payment;
    private Integer state;
    private Date payDate;
}
