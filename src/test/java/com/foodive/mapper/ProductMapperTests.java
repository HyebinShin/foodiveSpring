package com.foodive.mapper;

import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.ProductVO;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class ProductMapperTests {

    // initializationError
    // java.lang.Exception: No tests found matching Method testDuplicate(com.foodive.mapper.ProductMapperTests) from org.junit.vintage.engine.descriptor.RunnerRequest

    @Autowired
    private ProductMapper mapper;

    @Test
    public void testExist() {
        assertNotNull(mapper);
    }

    @Test
    public void testCreate() {
        IntStream.rangeClosed(51, 60).forEach(i -> {
            ProductVO productVO = new ProductVO();
            productVO.setKorName("상품테스트"+i);
            productVO.setEngName("ProductTest"+i);
            productVO.setCode("tes11");
            productVO.setPrice((int)(Math.random()*5000)+10000);

            mapper.insert(productVO);
        });
    }

    @DisplayName("상품명 중복 테스트")
    @MethodSource("provideTestDuplicate")
    @ParameterizedTest(name = "{index} {displayName} test={0}")
    public void testDuplicate(String duplicateParam, String duplicateCase, int expected) {
        DuplicateInfo duplicateInfo = new DuplicateInfo(duplicateParam, duplicateCase);
        assertEquals(expected, mapper.duplicate(duplicateInfo));
    }

    private static Stream<Arguments> provideTestDuplicate() {
        return Stream.of(
                Arguments.of("상품테스트1", "K", 1),
                Arguments.of("상품명중복테스트", "K", 0),
                Arguments.of("ProductTest1", "E", 1),
                Arguments.of("DuplicateTest", "E", 0)
        );
    }

    @DisplayName("상품 정보 출력 테스트")
    @MethodSource("provideTestGet")
    @ParameterizedTest
    public void testGet(ProductVO productVO) {
        log.info(mapper.get(productVO));
    }

    private static Stream<Arguments> provideTestGet() {
        List<ProductVO> productList = new ArrayList<>(
                List.of(
                        new ProductVO(), new ProductVO()
                )
        );
        productList.get(0).setPno(1L);
        productList.get(1).setPcode("p1");
        return Stream.of(
                Arguments.of(productList.get(0)),
                Arguments.of(productList.get(1))
        );
    }

    @DisplayName("상품 목록 출력 테스트")
    @MethodSource("provideTestGetList")
    @ParameterizedTest
    public void testGetList(ProductVO productVO) {
        Criteria cri = new Criteria();
        log.info(mapper.getListWithPaging(cri, productVO));
        log.info(mapper.getTotalProduct(productVO));
    }

    private static Stream<Arguments> provideTestGetList() {
        List<ProductVO> productList = new ArrayList<>(
                List.of(
                    new ProductVO(), new ProductVO()
                )
        );

        productList.get(0).setKorName("상품테스트1");
        productList.get(1).setCode("tes11");
        return Stream.of(
                Arguments.of(productList.get(0)),
                Arguments.of(productList.get(1))
        );
    }

    @ValueSource(longs = {11L, 12L, 13L})
    @ParameterizedTest
    public void testDrop(Long pno) {
        log.info("DROP: "+mapper.drop(pno));
    }

    @Test
    public void testUpdate() {
        ProductVO productVO = new ProductVO();
        productVO.setPno(5L);

        ProductVO updateProduct = mapper.get(productVO);
        updateProduct.setDiscount(5);
        updateProduct.setNation("한국");

        log.info("update: "+mapper.update(updateProduct));
    }

}
