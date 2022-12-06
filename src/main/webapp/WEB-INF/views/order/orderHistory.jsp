<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/orderCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">주문 내역 페이지</h1>
    </div>
</div>

<div class="row page-body">
    <div class="col-lg-12">
        <div class="panel panel-info">
            <div class="panel-heading">
                기간 단위별 주문 내역
            </div> <!-- div.class.panel-heading -->
            <div class="panel-body">
                <div class="col-lg-4">
                    <div class="form-group input-group">
                        <span class="input-group-addon">기간 단위</span>
                        <select class="form-control" name="date" id="dateSelect">
                            <option value="7" selected>7일 이내</option>
                            <option value="14">14일 이내</option>
                            <option value="30">30일 이내</option>
                        </select>
                    </div>
                </div> <!-- 기간 단위 선택. 끝 -->
                <div class="col-lg-4">
                    <div class="form-group input-group">
                        <span class="input-group-addon">기간</span>
                        <div class="period input-group-addon">
                            2022.12.06~
                        </div>
                    </div>
                </div>
                <div class="order-history">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>#주문번호</th>
                            <th>주문 날짜</th>
                            <th>주문 금액</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div> <!-- 기간 단위별 결제 내역. 끝 -->
                <div class="order-history-btn">

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
        let id = `<c:out value="${loginInfo.id}"/>`;
        let pageNum = 1;

        orderController.getOrderHistory(7, 1);

        $("#dateSelect").change(function () {
            let date = $(`#dateSelect option:selected`).val();
            console.log("date: "+date);

            orderController.getOrderHistory(date, 1);
        });

        $(document).on("click", ".order-history-btn button", function (e) {
            pageNum = $(this).data("page");
            let date = $(this).data("date");

            console.log("pageNum: "+pageNum+", date: "+date);

            orderController.getOrderHistory(date, pageNum);
        });

        // 주문 상세 정보 확인
        $(document).on("click", ".order-history tbody tr", function (e) {
            let type = $(this).data("type");
            let ono = $(this).data("ono");

            if (type==='detailList') {
                let closestHidden = $(this).next(".hidden-tr").find("td");
                $(this).siblings(".hidden-tr").hide();

                orderController.getOrderHistoryGet(type, ono, closestHidden);
            }
        })

        // 배송 정보 및 결제 정보 확인
        $(document).on("click", ".order-history-get-btn button", function (e) {
            e.preventDefault();

            let type = $(this).data("type");
            let ono = $(this).data("ono");

            console.log("type: "+type+", ono: "+ono);
            let closestHidden = $(this).closest("div").prev("table").find("."+type+" td");

            orderController.getOrderHistoryGet(type, ono, closestHidden);
        })

    })
</script>