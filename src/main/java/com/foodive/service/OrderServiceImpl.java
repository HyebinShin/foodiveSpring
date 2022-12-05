package com.foodive.service;

import com.foodive.domain.*;
import com.foodive.mapper.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j
public class OrderServiceImpl implements OrderService {

    @Setter(onMethod_ = @Autowired)
    private OrderMapper orderMapper;

    @Setter(onMethod_ = @Autowired)
    private OrderDetailMapper detailMapper;

    @Setter(onMethod_ = @Autowired)
    private ShipMapper shipMapper;

    @Setter(onMethod_ = @Autowired)
    private PayMapper payMapper;

    @Setter(onMethod_ = @Autowired)
    private ProductMapper productMapper;

    @Transactional
    @Override
    public boolean order(OrderLineDTO orderLineDTO) {
        if (!validateOrderLine(orderLineDTO)) {
            return false;
        }

        OrderVO orderVO = orderLineDTO.getOrder();
        orderMapper.insert(orderVO);

        orderLineDTO.getDetailList().forEach(detail -> {
            detail.setOno(orderVO.getOno());
            detailMapper.insert(detail);
            CartDTO cart = new CartDTO(detail.getPno(), detail.getQty(), detail.getStock());
            productMapper.afterOrder(cart);
        });

        ShipVO shipVO = orderLineDTO.getShip();
        shipVO.setOno(orderVO.getOno());
        shipMapper.insert(shipVO);

        PayVO payVO = orderLineDTO.getPay();
        payVO.setOno(orderVO.getOno());
        payMapper.insert(payVO);

        return true;
    }

    private boolean validateOrderLine(OrderLineDTO orderLineDTO) {
        if (orderLineDTO == null) {
            return false;
        }
        return orderLineDTO.getDetailList().size() != 0;
    }

    @Override
    public boolean modify(OrderVO orderVO) {
        return orderMapper.update(orderVO)==1;
    }

    @Override
    public OrderPageDTO getList(Criteria criteria, DatePageDTO datePageDTO) {
        return new OrderPageDTO(
                orderMapper.count(datePageDTO),
                orderMapper.getList(criteria, datePageDTO)
        );
    }

    @Override
    public OrderLineDTO get(Long ono) {
        return new OrderLineDTO(
                orderMapper.get(ono),
                detailMapper.getList(ono)
        );
    }
}
