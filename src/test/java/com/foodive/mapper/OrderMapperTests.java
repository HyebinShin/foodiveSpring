package com.foodive.mapper;

import com.foodive.domain.Criteria;
import com.foodive.domain.DatePageDTO;
import com.foodive.domain.OrderVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class OrderMapperTests {

    private static final String userId = "admin000";

    @Setter(onMethod_ = @Autowired)
    private OrderMapper mapper;

    @Test
    public void testExist() {
        assertNotNull(mapper);
    }

    @Test
    public void testCreate() {
        IntStream.rangeClosed(1, 30).forEach(i -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setId(userId);
            orderVO.setTotalPrice((int)(Math.random()*10000)+5000);

            mapper.insert(orderVO);
        });
    }

    @Test
    public void testUpdate() {
        OrderVO orderVO = new OrderVO();
        orderVO.setOno(3L);
        orderVO.setState(1);

        mapper.update(orderVO);
    }

    @Test
    public void testGetList() {
        Criteria criteria = new Criteria(1, 10);
        DatePageDTO datePageDTO = new DatePageDTO(1, 0);

        mapper.getList(criteria, datePageDTO);
    }

    @Test
    public void testCount() {
        DatePageDTO datePageDTO = new DatePageDTO(1, 0);

        mapper.count(datePageDTO);
    }

    @Test
    public void testGet() {
        Long ono = 5L;

        mapper.get(ono);
    }
}
