package com.foodive.controller;

import com.foodive.domain.UserVO;
import com.foodive.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/user/*")
@Controller
@Log4j
@AllArgsConstructor
public class UserController {

    private UserService service;

    @PostMapping(value = "/register")
    public String register(UserVO user, RedirectAttributes rttr) {
        log.info("register: "+user);

        if(service.register(user)) {
            rttr.addFlashAttribute("result", "success");
            log.info("result: success...");
        }

        return "redirect:/user/login";
    }

    @PostMapping(value = "/login")
    public String login(UserVO user, RedirectAttributes rttr) {
        log.info("login: "+user);

        if(service.get(user)!=null) {
            rttr.addFlashAttribute("result", "success");
        }

        return "redirect:/main";
    }

    @GetMapping(value = {"/register", "/login"})
    public void userPage() {
        log.info("userPage...");
    }
}
