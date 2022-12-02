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

<script src="/resources/js/productService.js"></script>
<script src="/resources/js/product.js"></script>
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
        })

    })
</script>