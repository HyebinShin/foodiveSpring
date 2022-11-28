package com.foodive.mapper;

import com.foodive.domain.ProductImageVO;

import java.util.List;

public interface ProductImageMapper {

    public void insert(ProductImageVO vo);

    public void delete(String uuid);

    public List<ProductImageVO> findByPno(Long pno);

    public void deleteAll(Long pno);

    public List<ProductImageVO> getOldFiles();
}
