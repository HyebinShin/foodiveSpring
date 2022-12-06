<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/adminHeader.jsp" %>
<link href="/resources/css/orderCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">주문 관리 페이지</h1>
    </div>
</div>

<div class="row page-body">
    <div class="col-lg-12">
        <div class="panel panel-info">
            <div class="panel-heading">
                기간 단위 및 주문 상태 별 주문 내역
            </div> <!-- div.class.panel-heading -->
            <div class="panel-body">
                <div class="col-lg-4">
                    <div class="form-group input-group">
                        <span class="input-group-addon">기간 단위</span>
                        <div class="input-group-addon dateBtn" data-type="minus"><i class="fa fa-minus"></i></div>
                        <select class="form-control mainSelect" name="date" id="dateSelect">
                            <option value="7" selected>7일</option>
                            <option value="14">14일</option>
                            <option value="30">30일</option>
                        </select>
                        <div class="input-group-addon dateBtn" data-type="plus"><i class="fa fa-plus"></i></div>
                    </div>
                </div> <!-- 기간 단위 선택. 끝 -->
                <div class="col-lg-4">
                    <div class="form-group input-group">
                        <span class="input-group-addon">주문 상태</span>
                        <select class="form-control mainSelect" name="state" id="stateSelect">
                            <option></option>
                            <option value="0">결제 대기</option>
                            <option value="1">주문 접수</option>
                            <option value="2">배송 중</option>
                            <option value="3">배송 완료</option>
                        </select>
                    </div>
                </div> <!-- 주문 상태 선택. 끝 -->
                <div class="col-lg-4">
                    <div class="form-group input-group">
                        <span class="input-group-addon">기간</span>
                        <div class="period input-group-addon">
                            2022.12.06~
                        </div>
                    </div>
                </div> <!-- 기간 표시. 끝 -->
                <div class="order-history">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>#주문번호</th>
                            <th>주문자 아이디</th>
                            <th>주문 날짜</th>
                            <th>주문 금액</th>
                            <th>주문 상태</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div> <!-- 기간 단위별 결제 내역. 끝 -->
                <div class="order-page-btn">

                </div>
            </div> <!-- div.class.panel-body -->
        </div> <!-- div.class.panel panel-info -->
    </div> <!-- div.class.col-lg-12 -->
</div>

<div class="page-footer">

</div>

<%@include file="../includes/adminFooter.jsp" %>

<script src="/resources/js/order.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        let pageNum = 1;
        let dateNumber = $("#dateSelect option:selected").val();
        let datePage = 1;
        let state = $("#stateSelect option:selected").val();

        orderController.getOrderList(7, 1, state, 1);

        $(".mainSelect").on("change", function (e) {
            e.preventDefault();

            dateNumber = $("#dateSelect option:selected").val();
            state = $("#stateSelect option:selected").val();

            let name = $(this).attr("name");

            if (name === 'date') {
                datePage = 1;
            }

            console.log("dateNumber: "+dateNumber+", state: "+state);
            console.log("datePage: "+datePage+", page: "+pageNum);

            orderController.getOrderList(dateNumber, datePage, state, 1);
        })

        $(document).on("click", ".pagination li a", function (e) {
            e.preventDefault();

            let href = $(this).attr("href");

            console.log("datePage: "+datePage);

            orderController.getOrderList(dateNumber, datePage, state, href);
        })

        $(".dateBtn").on("click", function () {
            let type = $(this).data("type");

            switch (type) {
                case 'plus':
                    datePage++;
                    break;
                case 'minus':
                    if (datePage <= 1) {
                        alert('기간 단위가 0이 될 수는 없습니다.');
                        return;
                    }
                    datePage--;
                    break;
            }

            console.log("datePage: "+datePage);

            orderController.getOrderList(dateNumber, datePage, state, 1);
        })

        // 주문 상세 정보 확인
        $(document).on("click", ".order-history tbody tr", function (e) {
            let type = $(this).data("type");
            let ono = $(this).data("ono");

            if (type==='detailList') {
                let closestHidden = $(this).next(".hidden-tr").find("td");
                $(this).siblings(".hidden-tr").hide();

                orderController.getOrder(type, ono, closestHidden);
            }
        })

        // 배송 정보 및 결제 정보 확인
        $(document).on("click", ".order-history-get-btn button", function (e) {
            e.preventDefault();

            let type = $(this).data("type");
            let ono = $(this).data("ono");

            console.log("type: "+type+", ono: "+ono);
            let closestHidden = $(this).closest("div").prev("table").find("."+type+" td");

            orderController.getOrder(type, ono, closestHidden);
        })

        // 배송 정보 및 결제 정보 수정 버튼 오픈 및 객체 clone
        let cloneShipObj;
        let clonePayObj;
        $(document).on("click", ".order-modify-btn button", function () {
            let type = $(this).data("type");
            let action = $(this).data("action");

            console.log("type: "+type);
            let closestDiv = $(this).closest(".modify-"+type);

            switch (action) {
                case 'removeReadOnly':
                    switch (type) {
                        case 'ship':
                            cloneShipObj = new fnc.constructorShip(
                                fnc.returnInputVal(closestDiv, 'name'),
                                fnc.returnInputVal(closestDiv, 'zipcode'),
                                fnc.returnInputVal(closestDiv, 'address1'),
                                fnc.returnInputVal(closestDiv, 'address2'),
                                fnc.returnInputVal(closestDiv, 'phone')
                            );
                            fnc.hideAndShowBtn(this, 'removeReadOnly');
                            $(this).closest(".modify-ship").find("input").removeAttr("disabled");
                            break;
                        case 'pay':
                            clonePayObj = new fnc.constructorPay(
                                fnc.returnSelectVal(closestDiv, 'payment'),
                                fnc.returnSelectVal(closestDiv, 'payState')
                            );
                            fnc.hideAndShowBtn(this, 'removeReadOnly');
                            $(this).closest(".modify-pay").find("select").removeAttr("disabled");
                            break;
                    }
                    break;
                // 수정 및 리셋
                case 'reset':
                    switch (type) {
                        case 'ship':
                            fnc.resetInputVal(closestDiv, 'name', cloneShipObj.name);
                            fnc.resetInputVal(closestDiv, 'zipcode', cloneShipObj.zipcode);
                            fnc.resetInputVal(closestDiv, 'address1', cloneShipObj.address1);
                            fnc.resetInputVal(closestDiv, 'address2', cloneShipObj.address2);
                            fnc.resetInputVal(closestDiv, 'phone', cloneShipObj.phone);
                            $(this).closest(".modify-ship").find("input").attr("disabled", "disabled");
                            break;
                        case 'pay':
                            fnc.resetSelectVal(closestDiv, 'payment', clonePayObj.payment);
                            fnc.resetSelectVal(closestDiv, 'payState', clonePayObj.state);
                            $(this).closest(".modify-pay").find("select").attr("disabled", "disabled");
                            break;
                    }
                    $(".error-well > .well").hide();
                    fnc.hideAndShowThisBtn(this, 'removeReadOnly');
                    break;
                case 'modify':
                    switch (type) {
                        case 'ship':
                            let sno = $(this).data("sno");
                            $(".modify-ship input").each(function () {
                                let input = $(this).val();
                                let name = $(this).attr("name");
                                let location = $(".error-well");

                                console.log("input: "+input+", name: "+name);

                                if (!orderValidate.checkNull(name, input, location)) {
                                    return false;
                                }

                                switch (name) {
                                    case 'name': case 'phone': case 'address2':
                                        if (!orderValidate.checkChange(name, input, location)) {
                                            console.log("invalidate");
                                            return false;
                                        }
                                        break;
                                }
                            })
                            let shipVO = new fnc.constructorShip(
                                fnc.returnInputVal(closestDiv, 'name'),
                                fnc.returnInputVal(closestDiv, 'zipcode'),
                                fnc.returnInputVal(closestDiv, 'address1'),
                                fnc.returnInputVal(closestDiv, 'address2'),
                                fnc.returnInputVal(closestDiv, 'phone')
                            );
                            shipVO.sno = sno;
                            orderController.modifyOrder({ship:shipVO});
                            break;
                        case 'pay':
                            let payVO = new fnc.constructorPay(
                                fnc.returnSelectVal(closestDiv, 'payment'),
                                fnc.returnSelectVal(closestDiv, 'payState')
                            );
                            let orderVO;
                            let thisOrder = $(this).parents(".hidden-tr").prev(".order-list-tr");
                            let ono = thisOrder.data("ono");
                            let stateSelect = thisOrder.find("select");
                            if (clonePayObj.state==0&&payVO.state==1) {
                                stateSelect.val(1).prop("selected", true);
                                orderVO = new fnc.OrderVO(ono, stateSelect.val());
                            } else if (payVO.state==2) {
                                stateSelect.val(4).prop("selected", true);
                                orderVO = new fnc.OrderVO(ono, stateSelect.val());
                            }
                            payVO.payNo = $(this).data("payno");
                            orderController.modifyOrder({pay:payVO, order:orderVO});
                            break;
                    }
                    break;
            }

            console.log("cloneShipObj: "+JSON.stringify(cloneShipObj));
            console.log("clonePayObj: "+JSON.stringify(clonePayObj));
        })

        $(document).on("change", ".modify-ship input", function () {
            let name = $(this).attr("name");
            let input = $(this).val();
            console.log("name: "+name); // name, phone, address2
            console.log("input: "+input);
            let location = $(".error-well");

            orderValidate.checkChange(name, input, location);
        })

        // 주문 상태 변경
        $(document).on("change", ".order-list-tr select", function () {
            let ono = $(this).parents(".order-list-tr").data("ono");
            let val = $(this).val();
            let payState;
            orderService.getOrderHistoryGet({type:'pay', ono:ono}, function (pay) {
                payState = pay.state;
                if (!orderValidate.checkPayState(payState, val)) {
                    alert('결제 상태를 다시 확인해주세요.');
                    return false;
                }
                let orderVO = new fnc.OrderVO(ono, val);
                orderController.modifyOrder({order:orderVO});
            })
        })

    })
</script>