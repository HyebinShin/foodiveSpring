package com.foodive.service;

import com.foodive.domain.PayVO;
import com.foodive.mapper.PayMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class PayServiceImpl implements PayService {
    @Setter(onMethod_ = @Autowired)
    private PayMapper mapper;

    @Override
    public boolean modify(PayVO payVO) {
        return mapper.update(payVO)==1;
    }

    @Override
    public PayVO get(Long ono) {
        return mapper.get(ono);
    }
}
