<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
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

<%@include file="../includes/footer.jsp" %>

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

    })
</script>