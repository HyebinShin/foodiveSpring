package com.foodive.service;

import com.foodive.domain.CartDTO;

import java.util.List;

public interface CartService {

    /**
     * 장바구니 테이블에 세션 장바구니를 등록한다.
     * @param cart 등록할 새로운 장바구니
     * @return 등록에 성공하면 true, 아니면 false
     */
    public boolean insertCart(CartDTO cart);

    /**
     * 장바구니 테이블에서 기존의 장바구니를 삭제한다.
     * @param id 세션에 저장된 로그인한 유저 아이디
     * @param pno 장바구니 테이블에 저장되어 있는 상품
     * @return 삭제에 성공하면 true, 아니면 false
     */
    public boolean deleteCart(String id, Long pno);

    /**
     * 장바구니 테이블에서 장바구니 객체 리스트를 가져온다.
     * @param id 세션에 저장된 로그인한 유저 아이디
     * @return 장바구니 테이블에 저장되어 있는 기존의 장바구니 리스트
     */
    public List<CartDTO> getCartList(String id);

    /**
     * 장바구니 테이블에 저장되어 있는 기존의 장바구니의 수량을 수정한다.
     * @param cart 유저 아이디, 상품 번호, 변경하고 싶은 상품 수량이 저장되어 있는 장바구니 객체
     * @return 수정에 성공하면 true, 아니면 false
     */
    public boolean updateCart(CartDTO cart);

    /**
     * 장바구니 테이블에 데이터가 있는지 확인한다.
     * @param id 세션에 저장된 로그인한 유저 아이디
     * @param pno 장바구니 테이블에 저장되어 있는지 확인할 상품 번호
     * @return 있으면 true, 없으면 false
     */
    public boolean hasCart(String id, Long pno);

}
