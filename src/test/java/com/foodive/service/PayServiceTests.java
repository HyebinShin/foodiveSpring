package com.foodive.service;

import com.foodive.domain.OrderLineDTO;
import com.foodive.domain.OrderVO;
import com.foodive.domain.PayVO;
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
public class PayServiceTests {
    @Setter(onMethod_ = @Autowired)
    private PayService service;

    @Test
    public void testExist() {
        assertNotNull(service);
    }

    @Test
    public void testUpdate() {
        PayVO payVO = new PayVO();
        payVO.setOno(33L);
        payVO.setState(1);

        OrderVO orderVO = new OrderVO("admin000", 5000);
        orderVO.setState(1);

        OrderLineDTO orderLineDTO = new OrderLineDTO();
        orderLineDTO.setPay(payVO);
        orderLineDTO.setOrder(orderVO);

        service.modify(orderLineDTO);
    }

    @Test
    public void testGet() {
        Long ono = 33L;

        service.get(ono);
    }
}
