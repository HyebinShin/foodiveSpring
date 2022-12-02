<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/productCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"></h1>
    </div>
</div>

<div class="row product-detail">

</div>

<div class="center-block page-footer">

</div>

<%@include file="../includes/footer.jsp" %>

<script src="/resources/js/productService.js"></script>
<script src="/resources/js/product.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        let category = `<c:out value="${category}"/>`;
        let categoryName = `<c:out value="${categoryName}"/>`;
        let keyword = `<c:out value="${keyword}"/>`;
        let pno = `<c:out value="${pno}"/>`;

        console.log("code: "+category);
        console.log("keyword: "+keyword);
        console.log("pno: "+pno);
        console.log("name: "+categoryName);

        // 상품 정보 출력
        init().initGetHeader(categoryName, keyword);
        productController().get(pno);
        init().initGetFooter(category, keyword);

        // 상품 썸네일 클릭시 큰 이미지 오픈
        $(document).on("click", ".product-thumbnail", function (e) {
            e.preventDefault();

            let path = $(this).data("path");

            init().initBigImage(path);
            $(this).fadeTo("fast", 1);
            $(".product-thumbnail").not($(this)).fadeTo("fast", 0.3);
        })

        // 상품 수량 변경
        let i = 0;
        $(document).on("click", ".qty button", function (e) {
            e.preventDefault();

            let type = $(this).data("type");
            let stock = $(this).data("stock");
            let qty = $("input[name='qty']");

            console.log("type: "+type);
            console.log("stock: "+stock);
            getBeforeLog(i);

            switch (type) {
                case 'minus':
                    if (cart().minus(i)) {
                        i--;
                    }
                    break;
                case 'plus':
                    if (cart().plus(i, stock)) {
                        i++;
                    }
                    break;
            }

            qty.val(i);
            getAfterLog(i);
        })

        function getBeforeLog(param) {
            console.log("before i: "+param);
        }
        function getAfterLog(param) {
            console.log("after i: "+param);
        }

    })
</script>