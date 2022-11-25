package com.foodive.service;

import com.foodive.domain.CategoryPageDTO;
import com.foodive.domain.CategoryVO;
import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.mapper.CategoryMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j
public class CategoryServiceImpl implements CategoryService {
    @Setter(onMethod_ = @Autowired)
    private CategoryMapper mapper;

    @Override
    public boolean register(CategoryVO category) {
        log.info("register..."+category);

        return mapper.insert(category)==1;
    }

    @Override
    public boolean duplicate(DuplicateInfo duplicateInfo) {
        log.info("duplicate..."+duplicateInfo);

        return mapper.duplicate(duplicateInfo)==0;
    }

    @Override
    public CategoryVO get(CategoryVO category) {
        log.info("get..."+category);

        return mapper.get(category);
    }

    @Override
    public CategoryPageDTO getListWithPaging(Criteria cri, CategoryVO category) {
        log.info("get Category List..."+cri);

        log.info("get Category List...category: "+category);

        return new CategoryPageDTO(
                mapper.getTotalCategory(category),
                mapper.getListWithPaging(cri, category)
        );
    }

    @Override
    public boolean drop(Long cno) {
        log.info("drop..."+cno);

        return mapper.drop(cno)==1;
    }

    @Override
    public boolean modify(CategoryVO category) {
        log.info("modify..."+category);

        return mapper.update(category)==1;
    }

    @Override
    public List<List<CategoryVO>> gnb() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setState(1);
        List<CategoryVO> highCategory = mapper.getListWithPaging(new Criteria(1, 0), categoryVO);
        List<List<CategoryVO>> lowCategory = new ArrayList<>();

        return null;
    }
}
