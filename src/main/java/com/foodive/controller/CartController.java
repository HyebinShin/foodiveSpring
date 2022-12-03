package com.foodive.controller;

import com.foodive.domain.CartDTO;
import com.foodive.domain.LoginInfo;
import com.foodive.persistence.CartMsg;
import com.foodive.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart/*")
@Log4j
@AllArgsConstructor
public class CartController {

    private CartService service;

    @GetMapping(value = "/cartPage")
    public void goCartPage() {

    }

    @GetMapping(
            value = "/get/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<List<CartDTO>> get(
            @PathVariable("id") String id,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (!loginInfo.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(loginInfo.getCartList(), HttpStatus.OK);
    }

    @DeleteMapping(
            value = "/{pno}",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> deleteCart(
            @PathVariable("pno") Long pno,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");

        if (service.deleteCart(loginInfo.getId(), pno)) {
            CartDTO removeCart = new CartDTO();
            removeCart.setPno(pno);
            removeCart.setId(loginInfo.getId());

            List<CartDTO> list = loginInfo.getCartList();

            log.info("before remove list: "+list);

            list.removeIf(cart -> cart.equals(removeCart));

            log.info("after remove list: "+list);
        }

        return new ResponseEntity<>(CartMsg.REMOVE, HttpStatus.OK);
    }
}
