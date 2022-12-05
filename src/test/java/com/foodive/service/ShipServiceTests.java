package com.foodive.service;

import com.foodive.domain.ShipVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class ShipServiceTests {
    @Setter(onMethod_ = @Autowired)
    private ShipService service;

    @Test
    public void testExist() {
        assertNotNull(service);
    }

    @Test
    public void testModify() {
        ShipVO shipVO = new ShipVO("배송서비스 수신인 수정", "333333", "배송서비스 주소지 수정", "010-1234-7777");
        shipVO.setSno(33L);

        service.modify(shipVO);
    }

    @Test
    public void testGet() {
        Long ono = 33L;

        service.get(ono);
    }
}
