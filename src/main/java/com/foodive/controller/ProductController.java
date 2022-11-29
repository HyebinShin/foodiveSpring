package com.foodive.controller;

import com.foodive.domain.CategoryPageDTO;
import com.foodive.service.CategoryService;
import com.foodive.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
