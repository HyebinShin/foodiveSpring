package com.foodive.mapper;

import com.foodive.domain.PayVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class PayMapperTests {

    @Setter(onMethod_ = @Autowired)
    private PayMapper mapper;

    @Test
    public void testExist() {
        assertNotNull(mapper);
    }

    @Test
    public void testCreate() {
        LongStream.rangeClosed(1L, 30L).forEach(l -> {
            PayVO pay = new PayVO();
            pay.setOno(l);
            pay.setPayment("무통장입금");
            pay.setState(0);

            mapper.insert(pay);
        });
    }

    @Test
    public void testUpdate() {
        PayVO pay = new PayVO();
        pay.setOno(3L);
        pay.setState(1);

        mapper.update(pay);
    }

    @Test
    public void testGet() {
        Long ono = 3L;

        mapper.get(ono);
    }
}
