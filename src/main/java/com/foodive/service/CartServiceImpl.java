package com.foodive.service;

import com.foodive.domain.CartDTO;
import com.foodive.mapper.CartMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class CartServiceImpl implements CartService {
    @Setter(onMethod_ = @Autowired)
    private CartMapper mapper;

    @Override
    public boolean insertCart(CartDTO cart) {
        if (hasCart(cart.getId(), cart.getPno())) {
            log.info("already exist cart!");
            return updateCart(cart);
        }

        return mapper.insert(cart)==1;
    }

    @Override
    public boolean deleteCart(String id, Long pno) {
        if (!hasCart(id, pno)) {
            log.info("no data in cart table");
            return true;
        }

        return mapper.delete(id, pno)==1;
    }

    @Override
    public List<CartDTO> getCartList(String id) {
        return mapper.findById(id);
    }

    @Override
    public boolean updateCart(CartDTO cart) {
        return mapper.update(cart)==1;
    }

    @Override
    public boolean hasCart(String id, Long pno) {
        return mapper.countCart(id, pno)==1;
    }
}
