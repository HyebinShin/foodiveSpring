package com.foodive.service;

import com.foodive.domain.CartDTO;
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
public class CartServiceTests {

    private static final String id = "admin000";
    private static final Long existPno = 70L;

    @Setter(onMethod_ = {@Autowired})
    private CartService service;

    @Test
    public void testExist() {
        assertNotNull(service);
    }

    @Test
    public void testInsertCart() {
        CartDTO cart = new CartDTO();
        cart.setId(id);
        cart.setPno(71L);
        cart.setQty(4);

        log.info("insert? "+service.insertCart(cart));
    }

    @Test
    public void testExistCartInsert() {
        CartDTO cart = new CartDTO();
        cart.setId(id);
        cart.setPno(existPno);
        cart.setQty(5);

        service.insertCart(cart);

    }

    @Test
    public void testNoDataDeleteCart() {
        Long pno = 72L;

        service.deleteCart(id, pno);
    }

    @Test
    public void testDeleteCart() {
        Long pno = 71L;

        service.deleteCart(id, pno);
    }

    @Test
    public void testNoDataGetList() {
        String noDataId = "admin001";

        service.getCartList(noDataId);
    }

}
