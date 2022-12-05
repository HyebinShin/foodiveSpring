package com.foodive.service;

import com.foodive.domain.ShipVO;
import com.foodive.mapper.ShipMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class ShipServiceImpl implements ShipService {
    @Setter(onMethod_ = @Autowired)
    private ShipMapper mapper;

    @Override
    public boolean modify(ShipVO shipVO) {
        return mapper.update(shipVO)==1;
    }

    @Override
    public ShipVO get(Long ono) {
        return mapper.get(ono);
    }
}
