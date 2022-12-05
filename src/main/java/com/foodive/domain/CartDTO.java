package com.foodive.domain;

import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class CartDTO {
    // cart table
    private Long cno;
    private String id;
    @NonNull
    private Long pno;
    @NonNull
    private Integer qty;

    // product table
    private String korName;
    private Integer price;
    private Integer discount;
    @NonNull
    private Integer stock;

    // calculate
    private Integer realPrice;
    private Integer totalPrice;

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
