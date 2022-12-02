package com.foodive.service;

import com.foodive.domain.*;
import com.foodive.mapper.ProductImageMapper;
import com.foodive.mapper.ProductMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j
public class ProductServiceImpl implements ProductService {
    @Setter(onMethod_ = @Autowired)
    private ProductMapper mapper;

    @Setter(onMethod_ = @Autowired)
    private ProductImageMapper imageMapper;

    @Override
    @Transactional
    public void register(ProductVO product) {
        log.info("register: " + product);

        mapper.insert(product);

        if (product.getImageList() == null || product.getImageList().size() == 0) {
            return;
        }

        product.getImageList().forEach(image -> {
            image.setPno(product.getPno());
            imageMapper.insert(image);
        });
    }

    @Override
    public boolean duplicate(DuplicateInfo duplicateInfo) {
        log.info("duplicate: "+duplicateInfo);

        return mapper.duplicate(duplicateInfo)==0;
    }

    @Override
    public ProductVO get(ProductVO product) {
        log.info("get: "+product);

        return mapper.get(product);
    }

    @Override
    public ProductPageDTO getList(Criteria cri, ProductVO product) {
        log.info("get product list");

        List<ProductVO> list = mapper.getListWithPaging(cri, product);

        for(ProductVO productVO : list) {
            productVO.setImageList(imageMapper.findByPno(productVO.getPno()));
        }

        return new ProductPageDTO(
                mapper.getTotalProduct(product),
                list
        );
    }

    @Override
    public boolean drop(Long pno) {
        log.info("drop: "+pno);

        return mapper.drop(pno)==1;
    }

    @Override
    @Transactional
    public boolean modify(ProductVO product) {
        log.info("modify: "+product);

        // 기존의 첨부파일 관련 데이터 삭제
        imageMapper.deleteAll(product.getPno());
        boolean modifyResult = mapper.update(product)==1;

        // 첨부파일이 있을 경우 DB에 저장
        if (modifyResult && product.getImageList()!=null && product.getImageList().size()>0) {
            product.getImageList().forEach(image -> {
                image.setPno(product.getPno());
                imageMapper.insert(image);
            });
        }

        return modifyResult;
    }

    @Override
    public List<ProductImageVO> getImageList(Long pno) {
        log.info("get image list by pno: "+pno);

        return imageMapper.findByPno(pno);
    }
}
