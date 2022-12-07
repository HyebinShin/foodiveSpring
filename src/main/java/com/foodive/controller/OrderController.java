package com.foodive.controller;

import com.foodive.domain.*;
import com.foodive.persistence.OrderMsg;
import com.foodive.service.CartService;
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
import java.util.Optional;

@Controller
@RequestMapping("/order/*")
@Log4j
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;
    private ShipService shipService;
    private PayService payService;

    private CartService cartService;

    @GetMapping({"/orderLine", "/orderHistory", "/orderPage"})
    public void goOrderPage() {
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
            deleteCartOrderItem(orderLine.getDetailList(), loginInfo);
            return new ResponseEntity<>(OrderMsg.ADD, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isSameId(LoginInfo loginInfo, String id) {
        return loginInfo.getId().equals(id);
    }

    private void deleteCartOrderItem(List<OrderDetailVO> detailList, LoginInfo loginInfo) {
        List<CartDTO> list = loginInfo.getCartList();
        String id = loginInfo.getId();

        detailList.forEach(detail -> {
            if (cartService.deleteCart(id, detail.getPno())) {
                CartDTO removeCart = new CartDTO();
                removeCart.setPno(detail.getPno());
                removeCart.setId(id);

                list.removeIf(cart -> cart.equals(removeCart));
            }
        });
    }

    @GetMapping(
            value = "/historyList/{date}/{page}",
            produces = "application/json; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<OrderPageDTO> getOrderHistory(
            @PathVariable("date") Integer date,
            @PathVariable("page") Integer page,
            HttpSession session
    ) {
        log.info("get order history");

        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        DatePageDTO datePageDTO = new DatePageDTO(date, null, page, loginInfo.getId());

        log.info("date page dto");

        return new ResponseEntity<>(orderService.getList(null, datePageDTO), HttpStatus.OK);
    }


    @GetMapping(
            value = {"/historyGet/{type}/{ono}"},
            produces = "application/json; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<?> getOrderHistoryShip(
            @PathVariable("type") String type,
            @PathVariable("ono") Long ono
    ) {
        switch (type) {
            case "detailList":
                return new ResponseEntity<>(orderService.get(ono), HttpStatus.OK);
            case "ship":
                return new ResponseEntity<>(shipService.get(ono), HttpStatus.OK);
            case "pay":
                return new ResponseEntity<>(payService.get(ono), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(
            value = {"/{dateNumber}/{datePage}/{page}/{state}", "/{dateNumber}/{datePage}/{page}"},
            produces = "application/json; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<OrderPageDTO> getOrderList(
            @PathVariable("dateNumber") Integer dateNumber,
            @PathVariable("datePage") Integer datePage,
            @PathVariable(value = "state", required = false) Optional<Integer> state,
            @PathVariable("page") Integer page
    ) {
        Integer optionState = state.orElse(null);

        DatePageDTO datePageDTO = new DatePageDTO(dateNumber, optionState, datePage, null);
        Criteria criteria = new Criteria(page, 10);

        return new ResponseEntity<>(orderService.getList(criteria, datePageDTO), HttpStatus.OK);
    }

    @PostMapping(
            value = {"/modify"},
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> modify(
            @RequestBody OrderLineDTO orderLineDTO
    ) {
        OrderVO orderVO = orderLineDTO.getOrder();
        ShipVO ship = orderLineDTO.getShip();
        PayVO pay = orderLineDTO.getPay();

        if (orderVO != null) {
            try {
                validateState(orderVO, pay);
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        boolean isSuccess = false;
        String msg = null;

        if (ship != null) {
            isSuccess = shipService.modify(ship);
            msg = OrderMsg.MODIFY_SHIP;
        } else if (pay != null){
            isSuccess = payService.modify(orderLineDTO);
            msg = OrderMsg.MODIFY_PAY_STATE;
        } else if (orderVO != null) {
            isSuccess = orderService.modify(orderVO);
            msg = OrderMsg.MODIFY_ORDER_STATE;
        }

        return isSuccess ?
                new ResponseEntity<>(msg, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // 결제 상태 및 주문 상태 검증
    private void validateState(OrderVO orderVO, PayVO payVO) {
        payVO = payVO!=null ? payVO : payService.get(orderVO.getOno());
        Integer orderState = orderVO.getState();
        Integer payState = payVO.getState();

        switch (orderState) {
            case 1: case 2: case 3:
                if (payState!=1) {
                    throw new IllegalArgumentException();
                }
                break;
            case 4:
                if (payState!=2) {
                    throw new IllegalArgumentException();
                }
                break;
            case 0 :
                if (payState!=0) {
                    throw new IllegalArgumentException();
                }
                break;
        }
    }

}
