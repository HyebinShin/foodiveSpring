package com.foodive.service;

import com.foodive.domain.OrderLineDTO;
import com.foodive.domain.PayVO;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

public interface PayService {

    /**
     * 결제 데이터의 결제 상태와 주문 데이터의 주문 상태를 변경한다.
     * @param orderLineDTO 변경할 결제 데이터와 주문 데이터가 저장된 객체
     * @return 수정에 성공하면 true, 아니면 false
     */
    public boolean modify(OrderLineDTO orderLineDTO);

    /**
     * 주문 번호에 해당하는 결제 데이터를 가져온다.
     * @param ono 주문 번호
     * @return 해당하는 결제 데이터
     */
    public PayVO get(Long ono);
}
