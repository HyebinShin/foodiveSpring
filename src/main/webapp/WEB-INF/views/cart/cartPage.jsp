<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/cartCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">장바구니 페이지</h1>
    </div>
</div>

<div class="row page-body">

</div>

<div class="center-block page-footer">

</div>

<%@include file="../includes/footer.jsp" %>

<script src="/resources/js/cart.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
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
            let qtyVal = Number(qty.val());

            console.log("type: "+type);
            console.log("stock: "+stock);
            console.log("qty: "+qty.val());
            console.log("pno: "+pno);

            switch (type) {
                case 'minus':
                    if (cartFunction().minus(qtyVal, pno)) {
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

            console.log("after qty: "+qty.val());
        })
    })
</script>