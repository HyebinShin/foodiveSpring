<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/cartCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">장바구니 페이지</h1>
    </div>
</div>

<div class="row page-body cart-page">

</div>

<div class="center-block page-footer">

</div>

<%@include file="../includes/footer.jsp" %>

<script type="text/javascript" src="/resources/js/cart.js"></script>
<script type="text/javascript" src="/resources/js/order.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        orderController.testExist();

        let id = `<c:out value="${loginInfo.id}"/>`;
        let cartList = `<c:out value="${loginInfo.getCartList()}"/>`;

        console.log("cart page login info id: "+id);
        console.log("cart page login info cart: "+cartList);

        cartController().getCartList(id);

        // 장바구니 수량 변경
        $(document).on("click", ".qty button", function (e) {
            e.preventDefault();

            let type = $(this).data("type");
            let stock = $(this).data("stock");
            let qty = $(this).closest("div").find("input[name='qty']");
            let pno = $(this).closest("tr").data("pno");
            let thisTR = $(this).closest("tr");
            let qtyVal = Number(qty.val());

            console.log("type: "+type);
            console.log("stock: "+stock);
            console.log("qty: "+qty.val());
            console.log("pno: "+pno);

            switch (type) {
                case 'minus':
                    if (cartFunction().minus(qtyVal, pno, thisTR)) {
                        qtyVal--;
                    }
                    break;
                case 'plus':
                    if (cartFunction().plus(qtyVal, stock)) {
                        qtyVal++;
                    }
                    break;
            }

            qty.val(qtyVal);

            let cart = {
                pno:pno,
                qty:qty.val(),
                id:id
            }

            cartController().modifyCart(cart);
            cartInit().initModifyCart(thisTR, qtyVal);

            console.log("after qty: "+qty.val());
        });

        // 장바구니 삭제
        $(document).on("click", ".deleteBtn button", function (e) {
            e.preventDefault();

            let type = $(this).data("type");
            let thisTR = $(this).closest("tr");
            let pno = thisTR.data("pno");

            if (type==='delete') {
                if (!confirm("장바구니에서 상품을 삭제하시겠습니까?")) {
                    return;
                }
                console.log("delete one cart");

                let param = {
                    pno:pno,
                    thisTR:thisTR
                }

                cartController().deleteCart(param);
            }
        })

        // 장바구니 체크박스
        $(document).on("change", "input:checkbox", function (e) {
            let thisTR = $(this).closest("tr");
            let isChecked = false;
            if ($(this).is(':checked')) {
                isChecked = true;
                cartInit().printCartSum(thisTR, isChecked);
            } else {
                cartInit().printCartSum(thisTR, isChecked);
            }
        })

        $(document).on("click", ".cart-page-btn button", function (e) {
            e.preventDefault();

            let number = $(this).data("number");
            let type = $(this).data("type");
            let checkbox = $(".cart-page input:checkbox");

            if (number==='all') {
                checkbox.prop("checked", true);
            } else if(number==='none') {
                checkbox.prop("checked", false);
            }

            let cart = [];
            let detailList = [];

            $(".cart-page input[type='checkbox']").each(function () {
                if ($(this).is(':checked')) {
                    let pno = $(this).val();
                    let thisTR = $(this).closest("tr");
                    console.log("pno: "+pno);
                    console.log("tr: "+thisTR.data('pno'));

                    let korName = thisTR.data("kor");
                    let qty = thisTR.find("input[name='qty']").val();
                    let totalPrice = thisTR.find(".td-total-price").data("total");
                    let realPrice = thisTR.find(".td-real-price").data("realprice");
                    let stock = thisTR.find(".cart-stock .input-group-btn button").data("stock");

                    let param = {
                        pno:pno,
                        thisTR:thisTR
                    }
                    cart.push(param);
                    detailList.push({pno:pno, korName:korName, qty:qty, totalPrice:totalPrice, realPrice:realPrice, stock:stock});
                }
            })

            switch (type) {
                case 'delete':
                    cartController().deleteAll(cart);
                    break;
                case 'order':
                    let orderDetailListDTO = {detailList:detailList, sumPrice:$(".cart-total-page > div").data("sum")};
                    orderController.setOrderDetail(orderDetailListDTO);
                    break;
            }
        })
    })
</script>