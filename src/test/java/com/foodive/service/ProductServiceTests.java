package com.foodive.service;

import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.ProductVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class ProductServiceTests {
    @Setter(onMethod_ = {@Autowired})
    private ProductService service;

    @Test
    public void testExist() {
        assertNotNull(service);
    }

    @Test
    public void testRegister() {
        ProductVO productVO = new ProductVO();
        productVO.setKorName("상품테스트");
        productVO.setEngName("ProductTest");
        productVO.setCode("tes11");
        productVO.setPrice((int)(Math.random()*5000)+10000);

        service.register(productVO);
    }

    @Test
    public void testDuplicate() {
        DuplicateInfo duplicateInfo = new DuplicateInfo("상품테스트", "K");

        service.duplicate(duplicateInfo);
    }

    @Test
    public void testGet() {
        ProductVO productVO = new ProductVO();
        productVO.setPno(1L);

        log.info(service.get(productVO));
    }

    @Test
    public void testGetList() {
        ProductVO productVO = new ProductVO();
        productVO.setCode("tes11");
        Criteria cri = new Criteria(1, 10);

        log.info(service.getList(cri, productVO));
    }

    @Test
    public void testDrop() {
        Long pno = 7L;

        service.drop(pno);
    }

    @Test
    public void testModify() {
        ProductVO productVO = new ProductVO();
        productVO.setPno(17L);

        ProductVO updateProduct = service.get(productVO);
        updateProduct.setDiscount(15);
        updateProduct.setNation("한국");

        service.modify(updateProduct);
    }
}
