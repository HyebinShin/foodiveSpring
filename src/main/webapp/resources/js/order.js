document.write(`<script src="/resources/js/function.js"></script>`);

let orderController = (function () {

    function testExist() {
        console.log("order controller exist");
    }

    function setOrderDetail(orderDetailListDTO) {
        orderService.setOrderDetail(orderDetailListDTO, function (result) {
            location.href = result;
        });
    }

    function addOrder(orderLine) {
        orderService.addOrder(orderLine, function (result) {
            alert(result);

            location.href = "/order/orderHistory";
        })
    }

    function getOrderHistory(date, page) {
        let param = {
            date: date,
            page: page
        }
        orderService.getOrderHistoryList(param, function (orderCnt, list) {
            if (page === -1) {
                let pageNum = Math.ceil(orderCnt / 10.0);
                getOrderHistory(date, pageNum);
                return;
            }

            orderInit.initPeriod(date * page);
            orderInit.initOrderHistory(list, orderInit.initOrderHistoryTR);
            orderInit.initOrderHistoryBtn(list, page, date);
        });
    }

    function getOrderHistoryGet(type, ono, space) {
        let param = {
            type: type,
            ono: ono
        }
        orderService.getOrderHistoryGet(param, function (data) {
            switch (type) {
                case 'detailList': // order, detailList(pno, korName, qty, totalPrice)
                    orderInit.initOrderHistoryGet(space, data.order, data.detailList)
                    break;
                case 'ship': // name, zipcode, address, phone
                    orderInit.initOrderHistoryShip(space, data);
                    break;
                case 'pay': // payment
                    orderInit.initOrderHistoryPay(space, data);
                    break;
            }
        })
    }

    function getOrderList(dateNumber, datePage, state, page) {
        let param = {
            dateNumber:dateNumber,
            datePage:datePage,
            page:page,
            state:state
        }

        console.log("param: "+JSON.stringify(param));

        orderService.getOrderList(param, function (orderCnt, list) {
            if (page === -1) {
                let pageNum = Math.ceil(orderCnt / 10.0);
                getOrderList(dateNumber, datePage, state, pageNum);
                return;
            }

            orderInit.initPeriod(dateNumber * datePage);
            orderInit.initOrderHistory(list, orderInit.initOrderListTR, true);
            fnc.initPagination(orderCnt, page, $(".order-page-btn"));

        });
    }

    function getOrder(type, ono, space) {
        let param = {
            type: type,
            ono: ono
        }
        orderService.getOrderHistoryGet(param, function (data) {
            switch (type) {
                case 'detailList': // order, detailList(pno, korName, qty, totalPrice)
                    orderInit.initOrderHistoryGet(space, data.order, data.detailList);
                    break;
                case 'ship': // name, zipcode, address1, address2, phone
                    orderInit.initOrderShip(space, data);
                    break;
                case 'pay': // payment
                    orderInit.initOrderPay(space, data);
                    break;
            }
        })
    }

    function modifyOrder(orderLineDTO) {
        orderService.modifyOrder(orderLineDTO, function (result) {
            alert(result);
        })
    }

    return {
        testExist: testExist,
        setOrderDetail: setOrderDetail,
        addOrder: addOrder,
        getOrderHistory: getOrderHistory,
        getOrderHistoryGet: getOrderHistoryGet,
        getOrderList: getOrderList,
        getOrder:getOrder,
        modifyOrder:modifyOrder
    };
})();

let orderService = (function () {

    function setOrderDetail(orderDetailListDTO, callback, error) {
        $.ajax({
            type: 'post',
            url: '/order/setOrderDetail',
            data: JSON.stringify(orderDetailListDTO),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    function addOrder(orderLine, callback, error) {
        $.ajax({
            type: 'post',
            url: '/order/new',
            data: JSON.stringify(orderLine),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    function getOrderHistoryList(param, callback, error) {
        let date = param.date;
        let page = param.page || 1;
        $.getJSON(`/order/historyList/${date}/${page}`,
            function (data) {
                if (callback) {
                    callback(data.orderCnt, data.list);
                }
            }).fail(function (xhr, status, err) {
            error(err);
        })
    }

    function getOrderHistoryGet(param, callback, error) {
        let type = param.type;
        let ono = param.ono;
        $.getJSON(`/order/historyGet/${type}/${ono}`,
            function (data) {
                if (callback) {
                    callback(data);
                }
            }).fail(function (xhr, status, err) {
            error(err);
        })
    }

    function getOrderList(param, callback, error) {
        let url = "/order";

        console.log("param: "+JSON.stringify(param));
        Object.keys(param).forEach((key) => (param[key]!=null ? url += `/${param[key]}` : ''));

        console.log("url: "+url);

        $.getJSON(url,
            function (data) {
                if (callback) {
                    callback(data.orderCnt, data.list);
                }
            }).fail(function (xhr, status, err) {
            error(err);
        })
    }

    function modifyOrder(param, callback, error) {
        $.ajax({
            type:'post',
            url: '/order/modify',
            data: JSON.stringify(param),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    return {
        setOrderDetail: setOrderDetail,
        addOrder: addOrder,
        getOrderHistoryList: getOrderHistoryList,
        getOrderHistoryGet: getOrderHistoryGet,
        getOrderList:getOrderList,
        modifyOrder:modifyOrder
    }

})();

let orderInit = (function () {

    // 고객 주문 내역 추가
    let tbody = $(".order-history tbody");
    let orderHistoryBtn = $(".order-history-btn");
    let period = $(".period");

    function initOrderHistory(orderList, functionName, isClear) {
        let innerText = tbody.text();
        if (innerText.includes("해당 기간에 주문 내역이 없습니다")) {
            tbody.empty();
        }

        if (orderList == null) {
            tbody.append(`해당 기간에 주문 내역이 없습니다.`);
            return;
        }
        let colspan = 3;

        if (isClear) {
            tbody.empty();
            colspan = 5;
        }

        let html = "";

        for (let i = 0, len = orderList.length || 0; i < len; i++) {
            html += `${functionName(orderList[i])}`;

            // 해당 주문 데이터 표시될 공간
            html += `<tr class="hidden-tr">`;

            html += `<td colspan=${colspan} data-ono=${orderList[i].ono}>내용</td>`;

            html += `</tr>`;
        }

        tbody.append(html);
    }

    function initOrderHistoryTR(order) {
        let html = `<tr data-ono=${order.ono} data-type="detailList">`;
        html += `<td>${order.ono}</td>`;
        html += `<td>${fnc.displayTime(order.orderDate)}</td>`;
        html += `<td>₩ ${order.totalPrice.toLocaleString(undefined, {maximumFractionDigits: 0})}</td>`;
        html += `</tr>`;

        return html;
    }


    function initOrderListTR(order) {
        let html = `<tr class="order-list-tr" data-ono=${order.ono} data-type="detailList">`;
        html += `<td>${order.ono}</td>`;
        html += `<td>${order.id}</td>`;
        html += `<td>${fnc.displayTime(order.orderDate)}</td>`;
        html += `<td>₩ ${order.totalPrice.toLocaleString(undefined, {maximumFractionDigits: 0})}</td>`;

        // let args = [{value:0, name:"결제 대기"}, {value:1, name: "주문 접수"}, {value:2, name: "배송 중"}, {value:3, name: "배송 완료"}];

        let args = [];
        args.push(new fnc.constructorSelect("결제 대기", 0));
        args.push(new fnc.constructorSelect("주문 접수", 1));
        args.push(new fnc.constructorSelect("배송 중", 2));
        args.push(new fnc.constructorSelect("배송 완료", 3));
        args.push(new fnc.constructorSelect("주문 취소", 4));

        html += `<td>${initSelect(order, 'orderState', args)}</td>`;

        html += `</tr>`;

        return html;
    }

    function initPeriod(date) {
        let today = new Date();
        let endDay = new Date();
        endDay.setDate(today.getDate() - date);

        let tYY = today.getFullYear();
        let tMM = today.getMonth() + 1;
        let tDD = today.getDate();

        let eYY = endDay.getFullYear();
        let eMM = endDay.getMonth() + 1;
        let eDD = endDay.getDate();

        period.empty();

        let html = [eYY, '/', eMM, '/', eDD, ' - ', tYY, '/', tMM, '/', tDD].join('');

        period.append(html);
    }

    function initOrderHistoryBtn(orderList, page, date) {
        orderHistoryBtn.empty();
        if (orderList == null) {
            return;
        }
        orderHistoryBtn.append(`<button type="button" data-page=${Number(page) + 1} data-date=${date} class="btn btn-default">${date}일 더 보기</button>`);
    }

    // 주문 데이터 표시
    function initOrderHistoryGet(space, order, detailList) {
        space.empty();

        if (detailList.length === 0) {
            return;
        }

        let html = "";
        // 경고 메시지 출력 div
        html += `<div class="error-well">`;
        html += `<div class='well'>`;
        html += `<div><i class="fa fa-info-circle"></i>`;
        html += `<div class='validate-well'>경고메시지</div></div></div>`;
        html += `</div>`;

        html += `<table class='table table-hover'>`;
        html += `<thead>`
        html += `<tr>`;
        html += `<th>상품명</th>`;
        html += `<th>가격</th>`;
        html += `<th>구매수량</th>`;
        html += `<th>총액</th>`;
        html += `</tr></thead>`;

        html += `<tbody>`;

        for (let i = 0, len = detailList.length || 0; i < len; i++) {
            let qty = Number(detailList[i].qty);
            let total = Number(detailList[i].totalPrice);
            let price = total / qty;

            html += `<tr data-pno=${detailList[i].pno}>`;

            html += `<td>${detailList[i].korName}</td>`;
            html += `<td>₩ ${price.toLocaleString(undefined, {maximumFractionDigits: 0})}</td>`;
            html += `<td>${qty}</td>`;
            html += `<td>₩ ${total.toLocaleString(undefined, {maximumFractionDigits: 0})}</td>`;

            html += `</tr>`;
        }

        // 총 구매 금액 표시
        html += `<tr><td></td><td></td><td></td><td>₩ ${order.totalPrice.toLocaleString(undefined, {maximumFractionDigits: 0})}</td></tr>`;

        // 배송지 표시될 공간
        html += `<tr class="hidden-tr ship">`;
        html += `<td colspan="4">배송지</td>`;
        html += `</tr>`;

        // 결제 방법 표시될 공간
        html += `<tr class="hidden-tr pay">`;
        html += `<td colspan="4">결제방법</td>`;
        html += `</tr>`;

        html += `</tbody>`;

        html += `</table>`;

        html += `<div class='order-history-get-btn'>`
        html += `<button class='btn btn-default' data-type="ship" data-ono=${order.ono}>배송 정보</button>`
        html += `<button class='btn btn-default' data-type="pay" data-ono=${order.ono}>결제 정보</button>`
        html += `</div>`;

        space.append(html);
        space.closest("tr").show();
    }

    function initOrderHistoryPay(space, pay) {
        space.empty();

        let html = initFormGroup("결제 방법", pay.payment)

        space.append(html);
        space.closest("tr").show();
    }

    function initOrderHistoryShip(space, ship) {
        space.empty();

        let html = initFormGroup("배송 수신인 성함", ship.name);
        html += initFormGroup("배송지 우편번호", ship.zipcode);
        let address = ship.address1 + ", " + ship.address2;
        html += initFormGroup("배송지 주소", address);
        html += initFormGroup("배송 수신자 연락처", ship.phone);

        space.append(html);
        space.closest("tr").show();
    }

    // 관리자 페이지 결제 방법 init
    function initOrderPay(space, pay) {
        space.empty();

        let html = `<div class="modify-pay" id='modify-pay-${pay.ono}'>`;

        let args = [];
        args.push(new fnc.constructorSelect("무통장입금", "무통장입금"));
        html += `${initSelect(pay, 'payment', args, '결제 방법')}`;

        args = [];
        args.push(new fnc.constructorSelect("무통장 입금 전", 0));
        args.push(new fnc.constructorSelect("결제 완료", 1));
        args.push(new fnc.constructorSelect("결제 취소", 2));

        html += `${initSelect(pay, 'payState', args, '결제 상태')}`;

        html += `<div class='order-modify-btn'>`
        html += `<button type="button" class='btn btn-info removeReadOnly' data-type="pay" data-action="removeReadOnly" data-payno=${pay.payNo}>수정</button>`
        html += `<button type="button" class='btn btn-info modify' data-type="pay" data-action="modify" data-payno=${pay.payNo}>수정</button>`
        html += `<button type="reset" class='btn btn-default reset' data-type="pay" data-action="reset">수정 취소</button>`
        html += `</div>`;

        html += `</div>`;
        space.append(html);
        $(".order-modify-btn button[data-action!='removeReadOnly']").hide();
        $(".modify-pay select").attr("disabled", "disabled");
        space.closest("tr").show();
    }

    // 관리자 페이지 배송 방법 init
    function initOrderShip(space, ship) {
        space.empty();

        let html = "";
        let args = [];
        args.push(new fnc.constructorInput('배송 수신인 성함', 'name', ship.name));
        args.push(new fnc.constructorInput('배송 수신자 연락처', 'phone', ship.phone));

        html += `<div class="modify-ship" id="modify-ship-${ship.ono}">`

        html += `${initInputText(args)}`;
        html += `<div class='form-group'>`;
        html += `<label>배송지</label>`;
        html += `<div class="address-form">`;
        html += `<input class="form-control" value=${ship.zipcode} name="zipcode" id="zipcode" maxlength="7" readonly>`
        html += `<div class="form-control" id="search_zipcode"><span>우편번호 검색</span></div>`;
        html += `<input class="form-control address" value='${ship.address1}' name="address1" id="address1" maxlength="70" readonly>`;
        let address2 = ship.address2 != null ? ship.address2 : '';
        html += `<input class="form-control address" value='${address2}' name="address2" id="address2" maxlength="70" required="required">`;

        html += `</div></div>`;

        html += `<div class='order-modify-btn'>`
        html += `<button type="button" class='btn btn-info removeReadOnly' data-type="ship" data-action="removeReadOnly" data-sno=${ship.sno}>수정</button>`
        html += `<button type="button" class='btn btn-info modify' data-type="ship" data-action="modify" data-sno=${ship.sno}>수정</button>`
        html += `<button type="reset" class='btn btn-default reset' data-type="ship" data-action="reset">수정 취소</button>`
        html += `</div>`;

        html += `</div>`;
        space.append(html);
        $(".order-modify-btn button[data-action!='removeReadOnly']").hide();
        $(".modify-ship input").attr("disabled", "disabled");
        space.closest("tr").show();
    }

    // 단순 출력
    function initFormGroup(label, data) {
        let html = `<div class='form-group'>`;
        html += `<label>${label}</label>`;
        html += `<p>${data}</p>`;
        html += `</div>`;

        return html;
    }

    // input text
    function initInputText(args) {
        let html = "";

        for (let i=0; i<args.length; i++) {
            html += `<div class='form-group'>`;
            html += `<label>${args[i].label}</label>`;
            html += `<input class='form-control' type='text' name='${args[i].name}' value='${args[i].data}'>`;
            html += `</div>`;
        }

        return html;
    }

    // select
    function initSelect(order, type, args, label) {
        let html = "";

        if(label!=null) {
            html += `<div class='form-group col-lg-6'>`;
            html += `<label>${label}</label>`;
        } else {
            html += `<div class='form-group'>`;
        }

        html += `<select class="form-control oneSelect" name='${type}' id='select${order.ono}' data-ono='${order.ono}'>`;

        for(let i=0; i<args.length; i++) {
            let isSelected = args[i].value === order.state ? 'selected' : '';
            html += `<option value='${args[i].value}' ${isSelected}>${args[i].name}</option>`;
        }

        html += `</select>`;
        html += `</div>`;

        return html;
    }

    return {
        initPeriod: initPeriod,
        initOrderHistory: initOrderHistory,
        initOrderHistoryBtn: initOrderHistoryBtn,
        initOrderHistoryGet: initOrderHistoryGet,
        initOrderHistoryPay: initOrderHistoryPay,
        initOrderHistoryShip: initOrderHistoryShip,
        initOrderHistoryTR:initOrderHistoryTR,
        initOrderListTR:initOrderListTR,
        initOrderPay:initOrderPay,
        initOrderShip:initOrderShip
    }

})();

let orderValidate = (function () {

    function regex(regexCase, input) {
        let regex;

        switch (regexCase) {
            case 'name':
                regex = /^[a-zA-Z0-9가-힣]{3,15}/g;
                break;
            case 'phone':
                regex = /^01[0|1]-\d{3,4}-\d{4}$/;
                break;
            case 'address2':
                regex = /^.{1,70}/g;
                break;
        }

        return regex.test(input);
    }

    function printMessage(msg, location) {
        let validateWell = $(".validate-well");
        validateWell.empty();
        validateWell.append(msg);
        location.find(".well").css("display", "block");
        // $(`${location} > .well`).css("display", "block");
    }

    function checkChange(regexCase, input, location) {
        let msg;
        switch (regexCase) {
            case 'name':
                msg = orderMessage.errorMsg.NAME_NOT_VALI;
                break;
            case 'phone':
                msg = orderMessage.errorMsg.PHONE_NOT_VALI;
                break;
            case 'address2':
                msg = orderMessage.errorMsg.ADDRESS_NOT_VALI;
                break;
        }
        if (input !== '' && !regex(regexCase, input)) {
            printMessage(msg, location);
            return false;
        }
        return true;
    }

    function checkNull(regexCase, input, location) {
        let msg;
        switch (regexCase) {
            case 'name':
                msg = orderMessage.errorMsg.NAME_NULL;
                break;
            case 'phone':
                msg = orderMessage.errorMsg.PHONE_NULL;
                break;
            case 'address1':
            case 'address2':
            case 'zipcode':
                msg = orderMessage.errorMsg.ADDRESS_NULL;
                break;
        }
        if (input === '') {
            printMessage(msg, location);
            return false;
        }
        return true;
    }

    function checkPayState(pay, order) {
        switch (order) {
            case '1': case '2': case '3':
                if (pay!=1) {
                    return false;
                }
                return true;
            case '4':
                if (pay!=2) {
                    return false;
                }
                return true;
            case '0':
                if (pay!=0) {
                    return false;
                }
                return true;
        }
    }

    return {
        checkChange: checkChange,
        checkNull: checkNull,
        checkPayState:checkPayState
    }
})();

let orderMessage = (function () {

    const errorMsg = {
        NAME_NULL: "배송 받으실 분의 성함을 입력해주세요.",
        NAME_NOT_VALI: "배송 수신인은 3자에서 15자 이내의 한글/영어/숫자만 입력가능합니다.",
        PHONE_NULL: "배송 받으실 분의 연락처를 입력해주세요.",
        PHONE_NOT_VALI: "연락처의 형식대로 입력해주세요.",
        ADDRESS_NULL: "주소를 입력해주세요.",
        ADDRESS_NOT_VALI: "상세 주소는 70자 이내로 입력해주세요."
    }

    return {
        errorMsg: errorMsg
    }
})();