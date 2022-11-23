package com.foodive.service;

import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.UserVO;

import java.util.List;

public interface UserService {

    public boolean register(UserVO user);

    public int duplicated(DuplicateInfo duplicateInfo);

    public UserVO get(UserVO user);

    public boolean drop(String id);

    public boolean modify(UserVO user, Boolean isPasswordCheck);

    public List<UserVO> getList(Criteria cri);
}
