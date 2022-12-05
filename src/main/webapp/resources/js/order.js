let orderController = (function () {

    function testExist() {
        console.log("order controller exist");
    }

    function setOrderDetail(orderDetailListDTO) {
        orderService.setOrderDetail(orderDetailListDTO, function (result) {
            location.href=result;
        });
    }

    return {
        testExist:testExist,
        setOrderDetail:setOrderDetail
    };
})();

let orderService = (function () {

    function setOrderDetail(orderDetailListDTO, callback, error) {
        $.ajax({
            type:'post',
            url: '/order/setOrderDetail',
            data: JSON.stringify(orderDetailListDTO),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if(callback) {
                    callback (result);
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
        setOrderDetail:setOrderDetail
    }

})();

let orderInit = (function () {

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
        if (input!==''&&!regex(regexCase, input)) {
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
            case 'address1': case 'address2': case 'zipcode':
                msg = orderMessage.errorMsg.ADDRESS_NULL;
                break;
        }
        if (input==='') {
            printMessage(msg);
            return false;
        }
        return true;
    }

    return {
        checkChange:checkChange,
        checkNull:checkNull
    }
})();

let orderMessage = (function () {

    const errorMsg = {
        NAME_NULL: "배송 받으실 분의 성함을 입력해주세요.",
        NAME_NOT_VALI : "배송 수신인은 3자에서 15자 이내의 한글/영어/숫자만 입력가능합니다.",
        PHONE_NULL : "배송 받으실 분의 연락처를 입력해주세요.",
        PHONE_NOT_VALI : "연락처의 형식대로 입력해주세요.",
        ADDRESS_NULL : "주소를 입력해주세요.",
        ADDRESS_NOT_VALI : "상세 주소는 70자 이내로 입력해주세요."
    }

    return {
        errorMsg:errorMsg
    }
})();