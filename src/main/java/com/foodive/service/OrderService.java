package com.foodive.service;

import com.foodive.domain.*;

import java.util.List;

public interface OrderService {
    /**
     * 작성한 주문서를 등록한다.
     *
     * @param orderLineDTO 새로운 주문서 데이터
     * @return 등록에 성공하면 true, 아니면 false
     */
    public boolean order(OrderLineDTO orderLineDTO);

    /**
     * 주문 데이터의 상태를 변경한다.
     * @param orderVO 변경할 주문 데이터
     * @return 수정에 성공하면 true, 아니면 false
     */
    public boolean modify(OrderVO orderVO);

    /**
     * 결제 상태와 기간 단위 별로 주문 데이터 목록을 출력한다.
     * @param criteria 페이지 정보
     * @param datePageDTO 결제 상태와 기간 단위
     * @return 페이징 처리된 주문 데이터 목록
     */
    public OrderPageDTO getList(Criteria criteria, DatePageDTO datePageDTO);

    /**
     * 특정 주문 데이터를 가져온다.
     * @param ono 주문 번호
     * @return 해당하는 주문 데이터
     */
    public OrderLineDTO get(Long ono);

}
