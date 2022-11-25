package com.foodive.service;

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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class CategoryServiceTests {

    @Setter(onMethod_ = {@Autowired})
    private CategoryService service;

    @Test
    public void testExist() {
        assertNotNull(service);
    }

    @Test
    public void testRegister() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setCode("tes");
        categoryVO.setName("테스트상위카테고리");
        categoryVO.setEName("TestHighCategory");

        log.info(service.register(categoryVO));
    }

    @Test
    public void testDuplicate() {
        String duplicateParam = "테스트상위카테고리";
        String duplicateCase = "N";
        DuplicateInfo duplicateInfo = new DuplicateInfo(duplicateParam, duplicateCase);

        log.info(service.duplicate(duplicateInfo));
    }

    @Test
    public void testGet() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setHCode("tes");
        categoryVO.setCode("tes");

        log.info(service.get(categoryVO));
    }

    @Test
    public void testGetList() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setHCode("hig");
        categoryVO.setCode("tes");

        Criteria criteria = new Criteria(1, 10);

        log.info(service.getListWithPaging(criteria, categoryVO));
    }

    @Test
    public void testModify() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setCno(5L);
        categoryVO.setName("테스트하위카테고리");
        categoryVO.setEName("TestLowCategory");
        categoryVO.setHCode("tes");
        categoryVO.setCode("hig5");
        categoryVO.setState(1);

        log.info(service.modify(categoryVO));
    }

    @Test
    public void testDrop() {
        Long cno = 5L;

        log.info(service.drop(cno));
    }


}
