package com.foodive.mapper;

import com.foodive.domain.CategoryVO;
import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {

    /**
     * @param category 등록할 카테고리
     * @return 카테고리 등록이 완료되면 1, 아니면 0
     */
    public int insert(CategoryVO category);

    /**
     * @param duplicateInfo 카테고리명 중복 검사(카테고리명, name)
     * @return 카테고리명이 중복이면 1이상, 아니면 0
     */
    public int duplicate(DuplicateInfo duplicateInfo);

    /**
     * @param category 찾고 싶은 카테고리의 정보
     * @return 카테고리 객체
     */
    public CategoryVO get(@Param("category") CategoryVO category);

    /**
     * @param category 찾고 싶은 카테고리의 정보
     * @param cri 페이징 정보
     * @return 카테고리 객체 리스트
     */
    public List<CategoryVO> getListWithPaging(
            @Param("cri") Criteria cri,
            @Param("category") CategoryVO category
    );

    /**
     * @param category 찾고 싶은 카테고리의 정보
     * @return 리스트 객체의 총 개수
     */
    public int getTotalCategory(@Param("category") CategoryVO category);

    /**
     * @param cno 비활성화 하고 싶은 카테고리의 번호
     * @return 비활성화에 성공하면 1, 아니면 0
     */
    public int drop(@Param("cno") Long cno);

    /**
     * @param category 수정하고 싶은 카테고리의 번호
     * @return 수정에 성공하면 1, 아니면 0
     */
    public int update(CategoryVO category);

}
