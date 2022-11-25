package com.foodive.controller;

import com.foodive.domain.CategoryPageDTO;
import com.foodive.domain.CategoryVO;
import com.foodive.domain.Criteria;
import com.foodive.domain.DuplicateInfo;
import com.foodive.persistence.CategoryMsg;
import com.foodive.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category/*")
@Controller
@Log4j
@AllArgsConstructor
public class CategoryController {

    private CategoryService service;

    private CategoryPageDTO getHighCategoryList() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setHCode("null");
        categoryVO.setState(1);
        return service.getListWithPaging(new Criteria(1, 0), categoryVO);
    }

    @GetMapping(value = {"/categoryPage"})
    public void goPage(Model model) {
        CategoryPageDTO categoryPageDTO = getHighCategoryList();

        model.addAttribute("gnb", categoryPageDTO);
    }

    @PostMapping(
            value = "/register",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody CategoryVO category) {
        log.info("register..."+category);

        return service.register(category) ?
                new ResponseEntity<>(CategoryMsg.INSERT, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(
            value = "/check",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> check(@RequestBody DuplicateInfo duplicateInfo) {
        return service.duplicate(duplicateInfo) ?
                new ResponseEntity<>(CategoryMsg.SINGLE, HttpStatus.OK)
                : new ResponseEntity<>(CategoryMsg.DUPLICATE, HttpStatus.OK);
    }

    @GetMapping(
            value = {"/pages/{hCode}/{state}/{page}"},
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<CategoryPageDTO> getList(
            @PathVariable("hCode") String hCode,
            @PathVariable("state") int state,
            @PathVariable("page") int page
    ) {
        CategoryVO category = new CategoryVO();
        log.info("state: "+state+", page: "+page+", category: "+category);
        Criteria cri = new Criteria(page, 10);
        category.setState(state);
        if(!"all".equals(hCode)) {
            category.setHCode(hCode);
        }
        log.info("set state category: "+category);

        return new ResponseEntity<>(service.getListWithPaging(cri, category), HttpStatus.OK);
    }
}
