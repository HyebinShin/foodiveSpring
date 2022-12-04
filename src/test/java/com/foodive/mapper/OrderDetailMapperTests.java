package com.foodive.mapper;

import com.foodive.domain.OrderDetailVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class OrderDetailMapperTests {

    @Setter(onMethod_ = @Autowired)
    private OrderDetailMapper mapper;

    @Test
    public void testExist() {
        assertNotNull(mapper);
    }

    @Test
    public void testCreate() {
        LongStream.rangeClosed(1L, 30L).forEach(l -> {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            orderDetailVO.setOno(l % 5 + 1L);
            orderDetailVO.setPno(60L);
            orderDetailVO.setKorName("주문상세테스트상품");
            orderDetailVO.setQty((int)(Math.random()*5)+3);
            orderDetailVO.setTotalPrice((int)(Math.random()*30000)+10000);

            mapper.insert(orderDetailVO);
        });
    }

    @Test
    public void testGetList() {
        Long ono = 5L;

        mapper.getList(ono);
    }
}
