package com.foodive.service;

import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.UserVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class UserServiceTests {
    @Setter(onMethod_ = {@Autowired})
    private UserService service;

    @Test
    public void testExist() {
        log.info(service);

        assertNotNull(service);
    }

    @Test
    public void testRegister() {
        UserVO user = new UserVO();
        user.setId("user001");
        user.setPassword("user001");
        user.setName("테스트용");
        user.setEmail("user001@gmail.com");

        log.info(service.register(user));
    }

    @Test
    public void testDuplicated() {
        DuplicateInfo duplicateInfo = new DuplicateInfo("user001", "I");

        log.info(service.duplicated(duplicateInfo));
    }

    @Test
    public void testGet() {
        UserVO user = new UserVO();
        user.setId("user001");

        log.info(service.get(user));
    }

    @Test
    public void testDrop() {
        String id = "user02";

        log.info(service.drop(id));
    }

    @Test
    public void testUpdate() {
        UserVO user = new UserVO();
        user.setId("user001");

        user = service.get(user);

        if(user==null) {
            return;
        }

        user.setZipcode("12345");

        log.info(service.modify(user, false));
    }

    @Test
    public void testPasswordUpdate() {
        UserVO user = new UserVO();
        user.setId("user001");

        user = service.get(user);

        if(user == null) {
            return;
        }

        user.setPassword("user001");

        log.info(service.modify(user, true));
    }
}
