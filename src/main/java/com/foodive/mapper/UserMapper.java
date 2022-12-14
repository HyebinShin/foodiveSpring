package com.foodive.mapper;

import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    public int insert(UserVO user);

    public int duplicated(
            @Param("duplicateInfo") DuplicateInfo duplicateInfo
    );

    public UserVO get(
            @Param("user") UserVO user
    ); //회원정보, 로그인

    public int drop(String id);

    public int update(
            @Param("user") UserVO user,
            @Param("check") Boolean isPasswordCase
    );

    public List<UserVO> getListWithPaging(
            @Param("cri") Criteria cri,
            @Param("state") Integer state
    );

    public int getTotalUser(int state);


    //추가

}
