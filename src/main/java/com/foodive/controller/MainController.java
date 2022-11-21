package com.foodive.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
@Log4j
@AllArgsConstructor
public class MainController {

    @GetMapping(value = "/main")
    public void goMain() {
        log.info("mainPage...");
    }
}
