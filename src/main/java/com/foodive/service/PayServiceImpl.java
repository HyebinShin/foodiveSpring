package com.foodive.service;

import com.foodive.domain.OrderLineDTO;
import com.foodive.domain.PayVO;
import com.foodive.mapper.OrderMapper;
import com.foodive.mapper.PayMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j
public class PayServiceImpl implements PayService {
    @Setter(onMethod_ = @Autowired)
    private PayMapper mapper;

    @Setter(onMethod_ = @Autowired)
    private OrderMapper orderMapper;

    @Transactional
    @Override
    public boolean modify(OrderLineDTO orderLineDTO) {
        if (mapper.update(orderLineDTO.getPay()) == 1) {
            if (orderLineDTO.getOrder() != null) {
                return orderMapper.update(orderLineDTO.getOrder()) == 1;
            }
            return true;
        }
        return false;
    }

    @Override
    public PayVO get(Long ono) {
        return mapper.get(ono);
    }
}
