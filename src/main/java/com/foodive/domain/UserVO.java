package com.foodive.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserVO {
    //회원번호, 아이디, 비밀번호, 이름, 이메일
    //성별, 생년월일, 휴대폰번호, 우편번호, 주소
    //가입상태, 가입일자, 탈퇴일자

    private Long uno;
    private String id;
    private String password;
    private String name;
    private String email;

    private String sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String phone;
    private String zipcode;
    private String address1;
    private String address2;

    private Integer state;
    private Date regDate;
    private Date dropDate;

}
