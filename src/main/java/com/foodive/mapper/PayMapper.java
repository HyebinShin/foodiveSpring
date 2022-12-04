package com.foodive.mapper;

import com.foodive.domain.PayVO;

public interface PayMapper {

    /**
     * 새로운 결제 데이터를 결제 테이블에 삽입한다.
     * @param pay 새로운 결제 데이터
     * @return 등록에 성공하면 1, 아니면 0
     */
    public int insert(PayVO pay);

    /**
     * 결제 데이터의 결제 상태를 변경한다.
     * @param pay 변경할 결제 데이터
     * @return 수정에 성공하면 1, 아니면 0
     */
    public int update(PayVO pay);

    /**
     * 주문 번호에 해당하는 결제 데이터를 가져온다.
     * @param ono 주문 번호
     * @return 해당하는 결제 데이터
     */
    public PayVO get(Long ono);

}
