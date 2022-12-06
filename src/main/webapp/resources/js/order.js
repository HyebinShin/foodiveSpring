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

            location.href = "/main"; // [수정 예정] 주문 결과창으로 이동
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

    function getOrderHistoryGet(type, ono) {
        let param = {
            type: type,
            ono: ono
        }
        orderService.getOrderHistoryGet(param, function (order) {
            switch (type) {
                case 'detailList': // order, detailList(pno, korName, qty, totalPrice)
                    break;
                case 'ship': // name, zipcode, address, phone
                    break;
                case 'pay': // payment
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

    return {
        initPeriod:initPeriod,
        initOrderHistory: initOrderHistory,
        initOrderHistoryBtn: initOrderHistoryBtn
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