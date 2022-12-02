package com.foodive.controller;

import com.foodive.domain.CategoryPageDTO;
import com.foodive.domain.CategoryVO;
import com.foodive.domain.Criteria;
import com.foodive.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
@Log4j
@AllArgsConstructor
public class MainController {

    @Setter(onMethod_ = @Autowired)
    private CategoryService service;

    @GetMapping(value = { "/adminMain"})
    public void goAdminMain() {
        log.info("mainPage...");
    }

    @GetMapping(value = "/main")
    public void goMain(HttpSession session) {
        Criteria cri = new Criteria(1, 0);
        CategoryVO setting = new CategoryVO();
        setting.setHCode("null");
        setting.setState(1);
        List<CategoryVO> high = (service.getListWithPaging(cri, setting)).getList();
        List<List<CategoryVO>> gnb = new ArrayList<>();

        for (CategoryVO categoryVO : high) {
            setting.setHCode(categoryVO.getCode());
            List<CategoryVO> menu = (service.getListWithPaging(cri, setting)).getList();
            gnb.add(menu);
        }

        session.setAttribute("highGnb", high);
        session.setAttribute("lowGnb", gnb);
    }

}
