package com.foodive.service;

import com.foodive.domain.Criteria;
import com.foodive.domain.UserVO;
import com.foodive.function.Encrypt;
import com.foodive.mapper.UserMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class UserServiceImpl implements UserService {
    @Setter(onMethod_ = @Autowired)
    private UserMapper mapper;

    @Override
    public boolean register(UserVO user) {
        log.info("register..."+user);
        user.setPassword(Encrypt.getEncryptPassword(user.getPassword()));

        return mapper.insert(user)==1;
    }

    @Override
    public int duplicated(String id) {
        log.info("duplicated..."+id);

        return mapper.duplicated(id);
    }

    @Override
    public UserVO get(UserVO user) {
        log.info("get..."+user);

        if(user.getPassword()!=null) {
            user.setPassword(Encrypt.getEncryptPassword(user.getPassword()));
        }

        return mapper.get(user);
    }

    @Override
    public boolean drop(String id) {
        log.info("drop..."+id);

        return mapper.drop(id)==1;
    }

    @Override
    public boolean modify(UserVO user, Boolean isPasswordCheck) {
        log.info("update..."+user);

        if(user.getPassword()!=null) {
            user.setPassword(Encrypt.getEncryptPassword(user.getPassword()));
        }

        return mapper.update(user, isPasswordCheck)==1;
    }

    @Override
    public List<UserVO> getList(Criteria cri) {
        log.info("get User List..."+cri);

        return mapper.getListWithPaging(cri);
    }
}
