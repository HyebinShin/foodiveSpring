package com.foodive.mapper;

import com.foodive.domain.OrderDetailVO;

import java.util.List;

public interface OrderDetailMapper {

    /**
     * 주문 상세 데이터를 주문 상세 테이블에 삽입한다.
     * @param detail 등록할 새로운 주문 상세 데이터
     * @return 등록에 성공하면 1, 아니면 0
     */
    public int insert(OrderDetailVO detail);

    /**
     * 주문 번호에 해당하는 주문 상세 데이터 목록을 출력한다.
     * @param ono 주문 번호
     * @return 주문 상세 데이터 목록
     */
    public List<OrderDetailVO> getList(Long ono);

}
