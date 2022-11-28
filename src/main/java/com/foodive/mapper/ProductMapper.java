package com.foodive.mapper;

import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.ProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    /**
     * @param product 등록할 상품
     * @return 등록에 성공하면 1, 아니면 0
     */
    public int insert(ProductVO product);

    /**
     * @param duplicateInfo 상품명 중복 검사 (상품명, name)
     * @return 상품명이 중복이면 1 이상, 아니면 0
     */
    public int duplicate(DuplicateInfo duplicateInfo);

    /**
     * @param product 찾고 싶은 상품의 정보
     * @return 상품 객체
     */
    public ProductVO get(@Param("product") ProductVO product);

    /**
     * @param cri 페이징 정보
     * @param product 찾고 싶은 상품의 정보
     * @return 상품 객체 리스트
     */
    public List<ProductVO> getListWithPaging(
            @Param("cri") Criteria cri,
            @Param("product") ProductVO product
    );

    /**
     * @param product 찾고 싶은 상품의 정보
     * @return 리스트 객체의 총 개수
     */
    public int getTotalProduct(@Param("product") ProductVO product);

    /**
     * @param pno 판매 중지를 하고 싶은 상품의 정보
     * @return 판매 중지에 성공하면 1, 아니면 0
     */
    public int drop(@Param("pno") Long pno);

    /**
     * @param product 수정하고 싶은 상품의 정보
     * @return 수정에 성공하면 1, 아니면 0
     */
    public int update(ProductVO product);

}
