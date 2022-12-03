package com.foodive.controller;

import com.foodive.domain.CartDTO;
import com.foodive.domain.LoginInfo;
import com.foodive.persistence.CartMsg;
import com.foodive.service.CartService;
import com.foodive.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cart/*")
@Log4j
@AllArgsConstructor
public class CartController {

    private CartService service;
    private ProductService productService;

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

            log.info("after remove list: "+loginInfo.getCartList());
        }

        return new ResponseEntity<>(CartMsg.REMOVE, HttpStatus.OK);
    }

    // 장바구니에 상품 담기
    @PostMapping(
            value = "/add",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> addCart(@RequestBody CartDTO cart, HttpSession session) {
        log.info("add cart!");

        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");

        if (!isSameId(loginInfo, cart.getId())) {
            return null;
        }

        if (service.insertCart(cart)) {
            String msg = addContainsCart(loginInfo, cart);

            log.info("after set list: "+loginInfo.getCartList());

            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String addContainsCart(LoginInfo loginInfo, CartDTO newCart) {
        List<CartDTO> list = loginInfo.getCartList();
        String msg = null;

        if (list.contains(newCart)) {
            CartDTO cloneCart = list.get(list.indexOf(newCart));
            int qty = cloneCart.getQty() + newCart.getQty();

            if (qty > cloneCart.getStock()) {
                cloneCart.setQty(cloneCart.getStock());
                msg = CartMsg.CANT_ADD;
            } else {
                cloneCart.setQty(qty);
                msg = CartMsg.ADD;
            }
            list.set(list.indexOf(newCart), cloneCart);
        } else {
            CartDTO addCart = productService.getCartInfo(newCart.getPno());
            addCart.setQty(newCart.getQty());
            addCart.setId(newCart.getId());
            list.add(addCart);
            msg = CartMsg.ADD;
        }

        return msg;
    }

    private boolean isSameId(LoginInfo loginInfo, String id) {
        return loginInfo.getId().equals(id);
    }
}
