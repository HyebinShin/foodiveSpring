package com.foodive.controller;

import com.foodive.domain.CategoryPageDTO;
import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.ProductVO;
import com.foodive.persistence.ProductMsg;
import com.foodive.service.CategoryService;
import com.foodive.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        service.register(product);

        return new ResponseEntity<>(ProductMsg.INSERT, HttpStatus.OK);
    }
}
