package com.foodive.controller;

import com.foodive.domain.Criteria;
import com.foodive.domain.UserPageDTO;
import com.foodive.domain.UserVO;
import com.foodive.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/manageUser/*")
@Controller
@Log4j
@AllArgsConstructor
public class UserAdminController {

    private UserService service;

    @GetMapping(value = {"/adminPage", "/userPage"})
    public void goPage() {

    }

    @GetMapping(
            value = {"/pages/{state}/{page}"},
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<UserPageDTO> getList(
            @PathVariable("state") int state,
            @PathVariable("page") int page
    ) {
        log.info("get list");

        Criteria cri = new Criteria(page, 10);
        log.info("get user|admin list state: "+state);
        log.info(cri);

        return new ResponseEntity<>(service.getList(cri, state), HttpStatus.OK);
    }

    @PostMapping(
            value = "/register",
            consumes = "application/json",
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody UserVO user) {
        log.info("UserVO: "+user);

        return service.register(user) ?
                new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
