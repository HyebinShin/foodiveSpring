package com.foodive.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test/*")
@Controller
@Log4j
@AllArgsConstructor
public class TestPageController {

    @GetMapping(value = {
            "/blank","/buttons","/flot","/forms","/grid","/icons",
            "/index","/login","/morris","/notifications","/panels-wells",
            "/tables","/typography"
    })
    public void goTestPage() {

    }
}
