package com.foodive.mapper;

import com.foodive.domain.CartDTO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class CartMapperTests {

    private final static String id = "admin000";

    @Setter(onMethod_ = @Autowired)
    private CartMapper mapper;

    @Test
    public void testExist() {
        assertNotNull(mapper);
    }

    @Test
    public void testCreate() {

        LongStream.rangeClosed(61L, 70L).forEach(l -> {
            CartDTO cart = new CartDTO();
            cart.setId(id);
            cart.setPno(l);
            cart.setQty((int)(Math.random()*6)+1);

            mapper.insert(cart);
        });
    }

    @Test
    public void testDelete() {
        Long pno = 67L;

        log.info("delete count: "+mapper.delete(id, pno));
    }

    @Test
    public void testFindById() {
        List<CartDTO> list = mapper.findById(id);

        list.forEach(log::info);
    }

    @Test
    public void testUpdate() {
        CartDTO cart = new CartDTO();
        cart.setId(id);
        cart.setPno(68L);
        cart.setQty(5);

        log.info("update count: "+mapper.update(cart));
    }

    @Test
    public void testCountCart() {
        Long pno = 68L;

        log.info("has cart? "+(mapper.countCart(id, pno)>=1));
    }

}
