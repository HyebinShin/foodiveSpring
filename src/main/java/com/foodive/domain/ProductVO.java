package com.foodive.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductVO {
    private Long pno;
    private String pcode;
    private String korName;
    private String engName;
    private Integer state; // 판매 : 1 , 판매 중지 : 0, 재고 없음 : 2

    private String code; //카테고리 코드
    private Integer price;
    private Integer discount;
    private String nation;
    private String detail;
    private Integer stock;

    private Date regDate;
    private Date modDate;
    private Date dropDate;

    private List<ProductImageVO> imageList;
    private String name;
}
