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

            orderInit.initPeriod(date*page);
            orderInit.initOrderHistory(list);
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

    return {
        testExist: testExist,
        setOrderDetail: setOrderDetail,
        addOrder: addOrder,
        getOrderHistory: getOrderHistory,
        getOrderHistoryGet:getOrderHistoryGet
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

    return {
        setOrderDetail: setOrderDetail,
        addOrder: addOrder,
        getOrderHistoryList: getOrderHistoryList,
        getOrderHistoryGet: getOrderHistoryGet
    }

})();

let orderInit = (function () {

    // 고객 주문 내역 추가
    let tbody = $(".order-history tbody");
    let orderHistoryBtn = $(".order-history-btn");
    let period = $(".period");

    function initOrderHistory(orderList) {
        let innerText = tbody.text();
        if (innerText.includes("해당 기간에 주문 내역이 없습니다")) {
            tbody.empty();
        }

        if (orderList == null) {
            tbody.append(`해당 기간에 주문 내역이 없습니다.`);
            return;
        }

        let html = "";

        for (let i = 0, len = orderList.length || 0; i < len; i++) {
            html += `<tr data-ono=${orderList[i].ono} data-type="detailList">`;

            html += `<td>${orderList[i].ono}</td>`;
            html += `<td>${fnc.displayTime(orderList[i].orderDate)}</td>`;
            html += `<td>${orderList[i].totalPrice}</td>`;

            html += `</tr>`;

            // 해당 주문 데이터 표시될 공간
            html += `<tr class="hidden-tr">`;

            html += `<td colspan="3" data-ono=${orderList[i].ono}>내용</td>`;

            html += `</tr>`;
        }

        tbody.append(html);
    }

    function initPeriod(date) {
        let today = new Date();
        let endDay = new Date();
        endDay.setDate(today.getDate()-date);

        let tYY = today.getFullYear();
        let tMM = today.getMonth()+1;
        let tDD = today.getDate();

        let eYY = endDay.getFullYear();
        let eMM = endDay.getMonth()+1;
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

        let html = `<table class='table table-hover'>`;
        html += `<thead><tr>`
        html += `<th>상품명</th>`;
        html += `<th>가격</th>`;
        html += `<th>구매수량</th>`;
        html += `<th>총액</th>`;
        html += `</tr></thead>`;

        html += `<tbody>`;

        for (let i=0, len=detailList.length||0; i<len; i++) {
            let qty = Number(detailList[i].qty);
            let total = Number(detailList[i].totalPrice);
            let price = total / qty;

            html += `<tr data-pno=${detailList[i].pno}>`;

            html += `<td>${detailList[i].korName}</td>`;
            html += `<td>₩ ${price.toLocaleString(undefined, {maximumFractionDigits:0})}</td>`;
            html += `<td>${qty}</td>`;
            html += `<td>₩ ${total.toLocaleString(undefined, {maximumFractionDigits:0})}</td>`;

            html += `</tr>`;
        }

        // 총 구매 금액 표시
        html += `<tr><td></td><td></td><td></td><td>₩ ${order.totalPrice.toLocaleString(undefined, {maximumFractionDigits:0})}</td></tr>`;

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

        let html = initFormGroup("배송 수신자 성함", ship.name);
        html += initFormGroup("배송지 우편번호", ship.zipcode);
        html += initFormGroup("배송지 주소", ship.address);
        html += initFormGroup("배송 수신자 연락처", ship.phone);

        space.append(html);
        space.closest("tr").show();
    }

    function initFormGroup(label, data) {
        let html = `<div class='form-group'>`;
        html += `<label>${label}</label>`;
        html += `<p>${data}</p>`;
        html += `</div>`;

        return html;
    }

    return {
        initPeriod:initPeriod,
        initOrderHistory: initOrderHistory,
        initOrderHistoryBtn: initOrderHistoryBtn,
        initOrderHistoryGet:initOrderHistoryGet,
        initOrderHistoryPay:initOrderHistoryPay,
        initOrderHistoryShip:initOrderHistoryShip
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

    function printMessage(msg) {
        let validateWell = $(".validate-well");
        validateWell.empty();
        validateWell.append(msg);
        $(".order-line-ship > .well").css("display", "block");
    }

    function checkChange(regexCase, input) {
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
            printMessage(msg);
            return false;
        }
        return true;
    }

    function checkNull(regexCase, input) {
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
            printMessage(msg);
            return false;
        }
        return true;
    }

    return {
        checkChange: checkChange,
        checkNull: checkNull
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