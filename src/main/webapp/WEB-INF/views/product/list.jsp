<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/productCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"></h1>
    </div>
</div>

<div class="row product-list">

</div>

<div class="center-block page-footer">

</div>

<%@include file="../includes/footer.jsp" %>


<!-- cart modal -->
<div class="modal fade" id="shopModal" tabindex="-1" role="dialog" aria-labelledby="shopModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span> </button>
                <h4 class="modal-title" id="shopModalLabel"></h4>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>

<script src="/resources/js/productService.js"></script>
<script src="/resources/js/product.js"></script>
<script src="/resources/js/cart.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        let category = `<c:out value="${category}"/>`;
        let keyword = `<c:out value="${keyword}"/>`;
        let pageFooter = $(".page-footer");
        let pageNum = 1;

        console.log("code: "+category);
        console.log("keyword: "+keyword);

        // 상품 목록 출력
        productController().getList(1, keyword, category);

        pageFooter.on("click", "button[id='more']", function (e) {
            e.preventDefault();

            pageNum = $(this).data("page");

            productController().getList(pageNum, keyword, category);
        })// 상품 목록 출력 END

        // 상세 페이지, 장바구니, 바로 주문 버튼
        let id = `<c:out value="${loginInfo.id}"/>`

        $(document).on("click", ".product-btn button", function (e) {
            e.preventDefault();

            let btnType = $(this).attr("id");
            let pno = $(this).data("pno");

            let thisClosestDiv = $(this).closest("div");

            let product = {
                pno:pno,
                korName:thisClosestDiv.data("kor"),
                stock:thisClosestDiv.data("stock"),
                price:thisClosestDiv.data("price"),
                discount:thisClosestDiv.data("discount")
            }

            switch (btnType) {
                case 'getDetail':
                    let categoryName = $(this).data("name");

                    let url = category === 'null' ?
                        `/product/get?keyword=\${keyword}&pno=\${pno}`
                        : `/product/get?category=\${category}&categoryName=\${categoryName}&pno=\${pno}`

                    location.href = url;
                    break;
                case 'getCart': case 'getOrder':
                    if(id===undefined||id==null||id==='null') {
                        alert('회원만 이용 가능합니다.');
                        return;
                    }
                    cartInit().initCartModal(product);
                    break;
            }

        })// 상세 페이지, 장바구니, 바로 주문 버튼 END

        // 모달 수량 조절 버튼
        $(document).on("click", ".qty button", function (e) {
            e.preventDefault();

            let type = $(this).data("type");
            let stock = $(this).data("stock");
            let qty = $(this).closest("div").find("input[name='qty']");
            let pno = $(this).closest(".col-lg-12").data("pno");
            let qtyVal = Number(qty.val());

            console.log("type: "+type);
            console.log("stock: "+stock);
            console.log("qty: "+qty.val());
            console.log("pno: "+pno);

            switch (type) {
                case 'minus':
                    if (productCart().minus(qtyVal)) {
                        qtyVal--;
                    }
                    break;
                case 'plus':
                    if (productCart().plus(qtyVal, stock)) {
                        qtyVal++;
                    }
                    break;
            }

            qty.val(qtyVal);
        })

        // 모달 버튼
        let modal = $(".modal");
        $(document).on("click", ".modal-product-btn button", function (e) {
            e.preventDefault();

            if(id===undefined||id==null||id==='null') {
                alert('회원만 이용 가능합니다.');
                return;
            }

            let type = $(this).data("type");
            let pno = $(this).data("pno");
            let qty = $("input[name='qty']").val();

            console.log("type: "+type);
            console.log("pno: "+pno);
            console.log("qty: "+qty);

            switch (type) {
                case 'cart':
                    let cart = {
                        pno:pno,
                        qty:qty,
                        id:id
                    }
                    cartController().addCart(cart);
                    modal.modal("hide");
                    break;
            }
        })


    })
</script>