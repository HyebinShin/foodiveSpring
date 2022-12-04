package com.foodive.mapper;

import com.foodive.domain.ShipVO;

public interface ShipMapper {

    /**
     * 배송 데이터를 배송 테이블에 삽입한다.
     * @param ship 새로운 배송 데이터
     * @return 등록에 성공하면 1, 아니면 0
     */
    public int insert(ShipVO ship);

    /**
     * 배송 데이터를 변경한다.
     * @param ship 변경할 배송 데이터
     * @return 수정에 성공하면 1, 아니면 0
     */
    public int update(ShipVO ship);

    /**
     * 주문 번호에 해당하는 배송 데이터를 가져온다.
     * @param ono 주문 번호
     * @return 해당하는 배송 데이터
     */
    public ShipVO get(Long ono);
}
