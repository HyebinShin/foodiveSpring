package com.foodive.controller;

import com.foodive.domain.DuplicateInfo;
import com.foodive.domain.LoginInfo;
import com.foodive.domain.UserVO;
import com.foodive.persistence.UserMsg;
import com.foodive.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
            rttr.addFlashAttribute("result", UserMsg.USER_REGISTER);
            log.info("result: success...");
        }

        return "redirect:/user/login";
    }

    @PostMapping(
            value = "/check",
            consumes = "application/json",
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public ResponseEntity<String> idCheck(@RequestBody DuplicateInfo duplicateInfo, RedirectAttributes rttr) {
        log.info("check param : "+duplicateInfo);

        int checkCount = service.duplicated(duplicateInfo);

        log.info("check DUPLICATE COUNT: "+checkCount);

        return checkCount == 0 ?
                new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>("duplicated", HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public String login(UserVO user, HttpSession session, RedirectAttributes rttr) {
        log.info("login: "+user);

        UserVO loginUser = service.get(user);

        if(loginUser!=null) {
            rttr.addFlashAttribute("result", user.getId()+UserMsg.USER_LOGIN);
            log.info("login user: "+loginUser);
            session.setAttribute("loginInfo", new LoginInfo(loginUser.getId(), loginUser.getState()));
            return "redirect:/main";
        } else {
            rttr.addFlashAttribute("result", UserMsg.USER_LOGIN_FAIL);
            return "redirect:/user/login";
        }

    }

    @GetMapping(value = {"/register", "/login"})
    public void userPage() {
        log.info("userPage...");
    }

    @GetMapping(value = {"/myPage"})
    public void userInfoPage(Model model, @SessionAttribute("loginInfo") LoginInfo loginInfo) {
        log.info("userInfoPage...");
        UserVO user = new UserVO();
        user.setId(loginInfo.getId());

        model.addAttribute("userInfo", service.get(user));
    }
}
