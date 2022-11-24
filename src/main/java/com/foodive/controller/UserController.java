package com.foodive.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        String msg = "";
        String url = "";

        if(loginUser!=null) {
            if(loginUser.getState()==0) {
                msg = UserMsg.USER_DROPPED;
                url = "/user/login";
            } else {
                msg = user.getId()+UserMsg.USER_LOGIN;
                url = "/main";
                session.setAttribute("loginInfo", new LoginInfo(loginUser.getId(), loginUser.getState()));
            }

        } else {
            msg = UserMsg.USER_LOGIN_FAIL;
            url = "/user/login";
        }

        rttr.addFlashAttribute("result", msg);
        return "redirect:"+url;

    }

    @GetMapping(value = {"/register", "/login"})
    public void userPage() {
        log.info("userPage...");
    }

    @GetMapping(value = {"/myPage", "/modify"})
    public void userInfoPage(Model model, @SessionAttribute("loginInfo") LoginInfo loginInfo) {
        log.info("userInfoPage...");
        UserVO user = new UserVO();
        user.setId(loginInfo.getId());

        model.addAttribute("userInfo", service.get(user));
    }

    @PostMapping(
            value = "/find",
            consumes = "application/json",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<UserVO> find(@RequestBody UserVO user) {
        log.info("user: "+user);

        return new ResponseEntity<>(service.get(user), HttpStatus.OK);
    }

    @RequestMapping (
            method = {RequestMethod.PUT, RequestMethod.PATCH},
            value = {"/{id}"},
            consumes = "application/json; charset=utf-8",
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public ResponseEntity<String> modify(
            @RequestBody ObjectNode node,
            @PathVariable("id") String id
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserVO user = mapper.treeToValue(node.get("user"), UserVO.class);
        Boolean isPasswordChange = node.get("isPassword").asBoolean();

        if(!id.equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return service.modify(user, isPasswordChange) ?
                new ResponseEntity<>(UserMsg.USER_UPDATE, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value = "/drop")
    public String drop(UserVO user, HttpSession session, RedirectAttributes rttr) {
        log.info("drop user..."+user.getId());
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");

        if(loginInfo.getId().equals(user.getId())) {
            if(service.drop(user.getId())) {
                session.invalidate();
                rttr.addFlashAttribute("result", UserMsg.USER_DROP);
            }
        }

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/main";
    }
}
