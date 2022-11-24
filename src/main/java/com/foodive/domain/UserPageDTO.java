package com.foodive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class UserPageDTO {
    private int userCnt;
    private List<UserVO> list;
}
