package com.foodive.service;

import com.foodive.domain.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class OrderServiceTests {

    private static final String userId = "admin000";
    private static final int totalPrice = (int)(Math.random()*10000)+5000;
    private static final int qty = (int)(Math.random()*5)+3;

    @Setter(onMethod_ = {@Autowired})
    private OrderService service;

    @Test
    public void testExist() {
        assertNotNull(service);
    }

    @Test
    public void testOrder() {
        OrderVO orderVO = new OrderVO(userId, totalPrice);
        List<OrderDetailVO> detailList = new ArrayList<>();

        LongStream.rangeClosed(40L, 45L).forEach(l -> {
            OrderDetailVO orderDetailVO = new OrderDetailVO(l, "상품테스트"+l, qty,totalPrice);
            orderDetailVO.setStock(20);
            detailList.add(orderDetailVO);
        });

        ShipVO shipVO = new ShipVO("주문서비스 배송수신인", "123456", "주문서비스 배송지", "010-1111-2222");

        PayVO payVO = new PayVO("무통장입금");

        OrderLineDTO orderLineDTO = new OrderLineDTO(orderVO, detailList, shipVO, payVO);

        service.order(orderLineDTO);
    }

    @Test
    public void testModify() {
        OrderVO orderVO = new OrderVO();
        orderVO.setOno(33L);
        orderVO.setState(1);

        service.modify(orderVO);
    }

    @Test
    public void testGetList() {
        Criteria criteria = new Criteria(1, 10);
        DatePageDTO datePageDTO = new DatePageDTO(1, 0, null);

        service.getList(criteria, datePageDTO);
    }

    @Test
    public void testGet() {
        Long ono = 33L;

        service.get(ono);
    }
}
