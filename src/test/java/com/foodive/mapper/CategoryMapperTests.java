package com.foodive.mapper;

import com.foodive.domain.CategoryVO;
import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class CategoryMapperTests {

    @Setter(onMethod_ = @Autowired)
    private CategoryMapper mapper;

    @Test
    public void testCreate() {
        IntStream.rangeClosed(1, 1).forEach(i -> {
            CategoryVO category = new CategoryVO();
            category.setHCode("tes");
            category.setName("테스트하위카테고리");
            category.setEName("testLowCategory");

            mapper.insert(category);
        });
    }

    @Test
    public void testDuplicate() {
        String name = "테스트하위카테고리";
        String duplicateCase = "N";
        DuplicateInfo duplicateInfo = new DuplicateInfo(name, duplicateCase);

        log.info(mapper.duplicate(duplicateInfo));
    }

    @Test
    public void testGet() {
        CategoryVO category = new CategoryVO();
//        category.setCode("tes5");
//        category.setName("테스트하위카테고리");
        category.setCno(5L);

        log.info(mapper.get(category));
    }

    @Test
    public void testGetList() {
        Criteria cri = new Criteria(1, 10);
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setHCode("tes");

        List<CategoryVO> list = mapper.getListWithPaging(cri, categoryVO);
        list.forEach(log::info);
    }

    @Test
    public void testGetTotal() {
        CategoryVO categoryVO = new CategoryVO();
//        categoryVO.setHCode("tes");
//        categoryVO.setCode("tes5");
        categoryVO.setState(1);

        log.info(mapper.getTotalCategory(categoryVO));
    }

    @Test
    public void testDrop() {
        Long cno = 5L;

        log.info(mapper.drop(cno));
    }

    @Test
    public void testUpdate() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setCno(5L);
        categoryVO.setName("테스트하위카테고리수정");
        categoryVO.setEName("TestLowCategory");
        categoryVO.setHCode("hig");
        categoryVO.setCode("tes5");
        categoryVO.setState(1);

        log.info(mapper.update(categoryVO));
    }
}
