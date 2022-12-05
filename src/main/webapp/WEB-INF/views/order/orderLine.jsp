<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>

<link href="/resources/css/orderCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">주문서 작성 페이지</h1>
    </div>
</div>

<div class="row page-body order-page">
    <div class="col-lg-8">
        <form role="form" method="post">
            <div class="panel panel-info">
                <div class="panel-heading">배송지 정보 입력</div>
                <div class="panel-body order-line-ship">
                    <div class="form-group">
                        <label>수신인</label>
                        <input class="form-control" name="name" type="text" required>
                    </div>
                    <div class="form-group">
                        <label>연락처</label>
                        <input class="form-control" name="phone" type="tel" required>
                    </div>
                    <div class="form-group">
                        <label>배송지</label>
                        <div class="address-form">
                            <input class="form-control" name="zipcode" id="zipcode" maxlength="7" readonly>
                            <div class="form-control" id="search_zipcode"><span>우편번호 검색</span></div>
                            <input class="form-control address" name="address1" id="address1" maxlength="70" readonly>
                            <input class="form-control address" name="address2" id="address2" maxlength="70" required>
                        </div>
                    </div>
                </div> <!-- div.class.panel-body order-line-ship -->
            </div> <!-- 배송지 정보 입력 끝. -->
            <div class="panel panel-default">
                <div class="panel-heading">주문 상품 정보</div>
                <div class="panel-body order-line-detailList">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>상품명</th>
                            <th>가격</th>
                            <th>구매수량</th>
                            <th>총액</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="detail" items="${detailList.getDetailList()}">
                        <tr data-pno="${detail.pno}" data-kor="${detail.korName}" data-qty="${detail.qty}"
                            data-total="${detail.totalPrice}" data-real="${detail.realPrice}">
                            <td>${detail.korName}</td>
                            <td><fmt:formatNumber value="${detail.realPrice}" type="currency" var="realPrice"/><c:out value="${realPrice}"/></td>
                            <td>${detail.qty}</td>
                            <td><fmt:formatNumber value="${detail.totalPrice}" type="currency" var="totalPrice"/><c:out value="${totalPrice}"/></td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <div class="sum">
                        <div>총액 : <fmt:formatNumber value="${detailList.sumPrice}" type="currency" var="sumPrice"/><c:out value="${sumPrice}"/></div>
                    </div>
                </div>
            </div> <!-- 주문 상품 정보 끝 -->
            <div class="panel panel-green">
                <div class="panel-heading">결제</div>
                <div class="panel-body order-line-pay">
                    <div class="form-group">
                        <label>결제 방법</label>
                        <select class="form-control" name="payment" id="paymentSelect">
                            <option value="무통장입금">무통장입금</option>
                        </select>
                    </div>
                </div>
            </div> <!-- 결제 방법 끝 -->
            <div class="order-btn">
                <button id="orderDoBtn" type="button" class="btn btn-success">주문하기</button>
                <button id="goCartPage" type="button" class="btn btn-default">취소</button>
            </div>
        </form>
    </div>
</div>

<div class="page-footer">

</div>

<%@include file="../includes/footer.jsp" %>

<script src="/resources/js/order.js"></script>
<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
<script src="/resources/js/addr.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        console.log(`<c:out value="${detailList}"/>`);
    })
</script>