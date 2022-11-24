package com.foodive.domain;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryVO {
    private Long cno;
    private String code;
    private String name;
    private Integer state;

    private Date regDate;
    private Date modDate;
    private Date dropDate;

    private String hCode;
    private String eName;
}
