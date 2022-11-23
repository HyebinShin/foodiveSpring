package com.foodive.mapper;

import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.UserVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class UserMapperTests {

    @Setter(onMethod_ = @Autowired)
    private UserMapper mapper;

    @Test
    public void testCreate() {
        IntStream.rangeClosed(16, 25).forEach(i -> {
            UserVO user = new UserVO();

            user.setId("user0"+i);
            user.setPassword("user0"+i);
            user.setName("테스트");
            user.setEmail("user0"+i+"@gmail.com");

            mapper.insert(user);
        });
    }

    @Test
    public void testDuplicated() {
        DuplicateInfo duplicateInfo = new DuplicateInfo("user001", "I");

        log.info("duplicated COUNT: "+mapper.duplicated(duplicateInfo));
    }

    @Test
    public void testLogin() {
        UserVO user = new UserVO();
        user.setId("user00");
//        user.setPassword("user00");

        log.info("get..."+mapper.get(user));
    }

    @Test
    public void testDrop() {
        String id = "user01";

        log.info("drop..."+mapper.drop(id));
    }

    @Test
    public void testUpdate() {
        UserVO user = new UserVO();
        user.setId("user01");
        user.setEmail("user01@naver.com");
        user.setPhone("010-0000-0000");

        log.info("update..."+mapper.update(user, false));
    }

    @Test
    public void testUpdatePassword() {
        UserVO user = new UserVO();
        user.setId("user01");
        user.setPassword("user001");

        log.info("update password..."+mapper.update(user, true));
    }

    @Test
    public void testGetList() {
        Criteria cri = new Criteria(1, 10);

        List<UserVO> users = mapper.getListWithPaging(cri);
        users.forEach(log::info);
    }

    @Test
    public void testTotal() {
        int state = 1;

        log.info("count: "+mapper.getTotalUser(state));
    }
}
