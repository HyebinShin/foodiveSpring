package com.foodive.controller;

import com.foodive.domain.LoginInfo;
import com.foodive.domain.OrderDetailListDTO;
import com.foodive.domain.OrderDetailVO;
import com.foodive.domain.OrderLineDTO;
import com.foodive.persistence.OrderMsg;
import com.foodive.service.OrderService;
import com.foodive.service.PayService;
import com.foodive.service.ShipService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/order/*")
@Log4j
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;
    private ShipService shipService;
    private PayService payService;

    @GetMapping("/orderLine")
    public void goOrderLine() {
        // 주문서 페이지로 이동
    }

    @PostMapping(
            value = "/setOrderDetail",
            consumes = "application/json; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> setOrderDetail(@RequestBody OrderDetailListDTO orderDetailListDTO, HttpSession session) {
        session.setAttribute("detailList", orderDetailListDTO);

        return new ResponseEntity<>("/order/orderLine", HttpStatus.OK);
    }

    @PostMapping(
            value = "/new",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> addOrder(@RequestBody OrderLineDTO orderLine, HttpSession session) {
        log.info("add new order: "+orderLine);

        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        log.info("loginInfo : "+loginInfo);

        if (!isSameId(loginInfo, orderLine.getOrder().getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (orderService.order(orderLine)) {
            return new ResponseEntity<>(OrderMsg.ADD, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isSameId(LoginInfo loginInfo, String id) {
        return loginInfo.getId().equals(id);
    }
}
