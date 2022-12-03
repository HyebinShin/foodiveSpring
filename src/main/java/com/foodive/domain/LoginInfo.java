package com.foodive.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class LoginInfo {

    // user table
    private String id;
    private Integer state;

    // cart table
    private List<CartDTO> cartList;

}
