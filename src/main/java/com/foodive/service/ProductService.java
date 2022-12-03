package com.foodive.service;

import com.foodive.domain.*;

import java.util.List;

public interface ProductService {

    /**
     * 새로운 상품을 등록한다.
     * @param product 등록할 상품
     */
    public void register(ProductVO product);

    /**
     * 상품명을 중복 검사한다.
     * @param duplicateInfo 상품명 중복 검사(상품명, name)
     * @return 상품명이 중복이 아니면 true, 중복이면 false
     */
    public boolean duplicate(DuplicateInfo duplicateInfo);

    /**
     * 특정한 상품의 정보를 얻는다.
     * @param product 찾고 싶은 상품의 정보
     * @return 상품 객체
     */
    public ProductVO get(ProductVO product);

    /**
     * 키워드 혹은 상품이 속한 카테고리에 따른 상품 목록을 출력한다.
     * @param cri 페이징 정보
     * @param product 찾고 싶은 상품의 정보
     * @return 상품 객체 리스트와 총 개수가 저장된 ProductPageDTO 객체
     */
    public ProductPageDTO getList(Criteria cri, ProductVO product);

    /**
     * 특정 상품을 판매 중지 처리를 한다.
     * @param pno 판매 중지를 하고 싶은 상품의 번호
     * @return 상품 판매 중지에 성공하면 true, 아니면 false
     */
    public boolean drop(Long pno);

    /**
     * 특정 상품의 정보를 수정한다.
     * @param product 수정하고 싶은 상품
     * @return 수정에 성공하면 true, 아니면 false
     */
    public boolean modify(ProductVO product);

    /**
     * 상품 이미지의 첨부 파일 리스트를 얻는다.
     * @param pno 상품 번호
     * @return 해당 상품 번호의 상품 이미지 리스트
     */
    public List<ProductImageVO> getImageList(Long pno);

    /**
     * 새로운 상품을 장바구니에 담을 때 해당 상품의 정보를 얻어 장바구니 객체에 담는다
     * @param pno 검색하고 싶은 상품 번호
     * @return 장바구니 객체 (유저 아이디와 수량, 총액은 빠져 있음)
     */
    public CartDTO getCartInfo(Long pno);
}
