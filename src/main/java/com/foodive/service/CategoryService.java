package com.foodive.service;

import com.foodive.domain.CategoryPageDTO;
import com.foodive.domain.CategoryVO;
import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryService {

    /**
     * @param category 등록할 카테고리
     * @return 카테고리 등록이 완료되면 true, 아니면 false
     */
    public boolean register(CategoryVO category);

    /**
     * @param duplicateInfo 카테고리명 중복 검사(카테고리명, name)
     * @return 카테고리명이 중복이 아니면 true, 중복이면 false
     */
    public boolean duplicate(DuplicateInfo duplicateInfo);

    /**
     * @param category 찾고 싶은 카테고리의 정보
     * @return 카테고리 객체
     */
    public CategoryVO get(CategoryVO category);

    /**
     * @param category 찾고 싶은 카테고리의 정보
     * @param cri 페이징 정보
     * @return 카테고리 객체 리스트와 총 개수가 저장된 CategoryPageDTO 객체
     */
    public CategoryPageDTO getListWithPaging(Criteria cri, CategoryVO category);

    /**
     * @param cno 비활성화 하고 싶은 카테고리의 번호
     * @return 비활성화에 성공하면 true, 아니면 false
     */
    public boolean drop(Long cno);

    /**
     * @param category 수정하고 싶은 카테고리의 번호
     * @return 수정에 성공하면 true, 아니면 false
     */
    public boolean modify(CategoryVO category);

}
