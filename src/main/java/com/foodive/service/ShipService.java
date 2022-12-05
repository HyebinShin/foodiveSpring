package com.foodive.service;

import com.foodive.domain.ShipVO;

public interface ShipService {
    /**
     * 배송 데이터를 변경한다.
     * @param shipVO 변경할 배송 데이터
     * @return 수정에 성공하면 true, 아니면 false
     */
    public boolean modify(ShipVO shipVO);

    /**
     * 주문 번호에 해당하는 배송 데이터를 가져온다.
     * @param ono 주문 번호
     * @return 해당하는 배송 데이터
     */
    public ShipVO get(Long ono);
}
