package com.foodive.controller;

import com.foodive.domain.*;
import com.foodive.persistence.ProductMsg;
import com.foodive.service.CategoryService;
import com.foodive.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.Optional;

@RequestMapping("/product/*")
@Controller
@Log4j
@AllArgsConstructor
public class ProductController {
    private ProductService service;
    private CategoryService categoryService;

    @GetMapping(value = "/productPage")
    public void goProductPage(Model model) {
        CategoryPageDTO categoryPageDTO = new CategoryController(categoryService).getHighCategoryList();
        model.addAttribute("gnb", categoryPageDTO);
    }

    @GetMapping(value = "/pages/{hCode}/{state}")
    @ResponseBody
    public ResponseEntity<CategoryPageDTO> getCategoryList(
            @PathVariable("hCode") String hCode,
            @PathVariable("state") int state
    ) {
        return new CategoryController(categoryService).getList(hCode, state, 0);
    }

    @PostMapping(
            value = "/check",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> check(@RequestBody DuplicateInfo duplicateInfo) {
        return service.duplicate(duplicateInfo) ?
                new ResponseEntity<>(ProductMsg.SINGLE, HttpStatus.OK)
                : new ResponseEntity<>(ProductMsg.DUPLICATE, HttpStatus.OK);
    }

    @PostMapping(
            value = "/register",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody ProductVO product) {
        log.info("new product!");

        if (product.getImageList() != null) {
            product.getImageList().forEach(log::info);
        }

        service.register(product);

        return new ResponseEntity<>(ProductMsg.INSERT, HttpStatus.OK);
    }

    @GetMapping(
            value = {"/list/{state}/{page}", "/list/{state}/{page}"},
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<ProductPageDTO> getList(
            @PathVariable("state") int state,
            @PathVariable("page") int page,
            @RequestParam(value = "keyword", required = false) Optional<String> keyword,
            @RequestParam(value = "code", required = false) Optional<String> code
    ) {
        log.info("keyword: "+keyword);
        log.info("code: "+code);
        log.info("state: "+state);
        log.info("page: "+page);

        String name = keyword.orElse("null");
        String category = code.orElse("null");
        log.info("name: "+name);
        log.info("category: "+category);

        ProductVO product = new ProductVO();
        product.setCode(category);
        product.setKorName(name);
        product.setState(state);
        Criteria criteria = new Criteria(page, 10);

        return new ResponseEntity<>(service.getList(criteria, product), HttpStatus.OK);
    }
}
