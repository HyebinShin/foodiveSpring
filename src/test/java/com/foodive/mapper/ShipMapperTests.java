package com.foodive.mapper;

import com.foodive.domain.ShipVO;
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
public class ShipMapperTests {

    @Setter(onMethod_ = @Autowired)
    private ShipMapper mapper;

    @Test
    public void testExist() {
        assertNotNull(mapper);
    }

    @Test
    public void testCreate() {
        LongStream.rangeClosed(1L, 30L).forEach(l -> {
            ShipVO ship = new ShipVO();
            ship.setOno(l);
            ship.setName("배송테스트"+l);
            ship.setZipcode("123456");
            ship.setAddress("배송테스트 주소지"+l);
            ship.setPhone("010-1234-5678");

            mapper.insert(ship);
        });
    }

    @Test
    public void testUpdate() {
        ShipVO ship = new ShipVO();
        ship.setSno(5L);
        ship.setName("배송테스트 수정");
        ship.setZipcode("654321");
        ship.setAddress("배송테스트 주소지 수정");
        ship.setPhone("010-4321-8765");

        mapper.update(ship);
    }

    @Test
    public void testGet() {
        Long ono = 5L;

        mapper.get(ono);
    }
}
