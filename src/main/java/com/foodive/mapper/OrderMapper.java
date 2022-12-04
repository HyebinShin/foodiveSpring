package com.foodive.mapper;

import com.foodive.domain.Criteria;
import com.foodive.domain.DatePageDTO;
import com.foodive.domain.OrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    /**
     * 주문 데이터를 주문 테이블에 삽입한다.
     *
     * @param order 등록할 새로운 주문 데이터
     * @return 등록에 성공하면 1, 아니면 0
     */
    public int insert(OrderVO order);

    /**
     * 주문 데이터의 상태를 변경한다.
     *
     * @param order 변경할 주문 데이터 객체
     * @return 수정에 성공하면 1, 아니면 0
     */
    public int update(OrderVO order);

    /**
     * 결제 상태와 기간 단위 별로 주문 데이터 목록을 출력한다.
     *
     * @param criteria    페이지 정보
     * @param datePageDTO 결제 상태와 기간 단위(단위:일)
     * @return 해당하는 주문 데이터 목록
     */
    public List<OrderVO> getList(
            @Param("cri") Criteria criteria,
            @Param("datePage") DatePageDTO datePageDTO
    );

    /**
     * 결제 상태와 기간 단위 별 주문 데이터의 총 개수
     *
     * @param datePageDTO 결제 상태와 기간 단위(단위:일)
     * @return 해당하는 주문 데이터의 개수
     */
    public int count(
            @Param("datePage") DatePageDTO datePageDTO
    );

    /**
     * 특정 주문 데이터를 가져온다.
     *
     * @param ono 주문 번호
     * @return 해당하는 주문 데이터 객체
     */
    public OrderVO get(Long ono);

}
