package com.foodive.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class CartDTO {
    // cart table
    private Long cno;
    private String id;
    private Long pno;
    private Integer qty;

    // product table
    private String korName;
    private Integer price;
    private Integer discount;
    private Integer stock;

    // calculate
    private Integer realPrice;
    private Integer totalPrice;

    public Integer getRealPrice() {
        return getPrice() * (100-getDiscount())/100;
    }

    public Integer getTotalPrice() {
        return getRealPrice() * getQty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDTO cartDTO = (CartDTO) o;
        return Objects.equals(id, cartDTO.id) && Objects.equals(pno, cartDTO.pno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pno);
    }

}
