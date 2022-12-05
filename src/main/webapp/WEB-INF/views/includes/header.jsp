<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C/DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Foodive</title>

    <!-- Bootstrap Core CSS -->
<%--    <link href="/resources/vendor/bootstrap-2.3.2/bootstrap-2.3.2/docs/assets/css/bootstrap.css" rel="stylesheet">--%>
    <link href="/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link href="/resources/vendor/datatables-plugins/dataTables.bootstrap.css" rel="stylesheet">

    <!-- DataTables Responsive CSS -->
    <link href="/resources/vendor/datatables-responsive/dataTables.responsive.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/resources/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link href="/resources/css/bootstrapCustom.css" rel="stylesheet">
    <link href="/resources/css/custom.css" rel="stylesheet">

</head>
<body>
<div id="wrapper">
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-top: 0">
        <div class="navbar-header">
            <a class="navbar-brand" href="/main">FOODIVE</a>
        </div>
        <!-- END .navbar-header -->
        <ul class="nav navbar-top-links navbar-left">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-th-list fa-fw"></i> 카테고리 <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu high-menu" role="menu" aria-labelledby="dropdownMenu">
                    <c:forEach var="high" items="${highGnb}" varStatus="status">
                        <c:set var="lows" value="${lowGnb.get(status.index)}"/>
                        <li class="dropdown-submenu">
                            <a tabindex="-1" href="#">
                                <c:out value="${high.getName()}"/>
                            </a>
                            <ul class="dropdown-menu">
                                <c:forEach var="low" items="${lows}">
                                    <li><a href="${low.getCode()}"><c:out value="${low.getName()}"/></a></li>
                                    <li class="divider"></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </li>
        </ul>
        <div class="input-group col-lg-4" style="float: left; margin-top: 10px">
            <input type="text" class="form-control" name="keyword" placeholder="검색어를 입력해주세요.">
            <span class="input-group-btn"><button class="btn btn-default" type="button" id="keyword-search"><i class="fa fa-search"></i></button></span>
        </div>
        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <c:set value="${loginInfo.state}" var="state"/>
                    <c:choose>
                        <c:when test="${empty state || state eq '0'}">
                            <li>
                                <a href="/user/login">로그인</a>
                            </li>
                            <li>
                                <a href="/user/register">회원가입</a>
                            </li>
                </ul>
            </li>
                        </c:when>
                        <c:when test="${!empty state && state ne '0'}">
                            <li>
                                <a href="/user/myPage">마이페이지</a>
                            </li>
                            <li>
                                <a href="/user/logout">로그아웃</a>
                            </li>
                </ul>
            </li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-shopping-cart fa-fw cart-size"></i><span></span> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li>
                        <a href="/cart/cartPage"> 장바구니</a>
                    </li>
                    <li>
                        <a href="/order/orderHistory"> 주문 내역</a>
                    </li>
                </ul>
            </li>
                        </c:when>
                    </c:choose>
            <c:if test="${state eq '2'}">
                <li>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-cog fa-fw"></i> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="/adminMain">관리자 페이지</a></li>
                    </ul>
                </li>
            </c:if>
        </ul>
    </nav>

    <div id="page-wrapper" style="margin: 0 auto;">

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js" type="text/javascript"></script>

        <script type="text/javascript">
            console.log('state: <c:out value="${loginInfo.state}"/>');

            $(document).ready(function () {
                let subMenu = $(".dropdown-submenu a");

                subMenu.each(function () {
                    $(this).click(function (e) {
                        e.preventDefault();

                        let code = $(this).attr("href");

                        location.replace("/product/list?code="+code);
                    })
                });

                let keywordSearch = $("#keyword-search");
                let inputKeyword = $("input[name='keyword']");

                keywordSearch.on("click", function (e) {
                    e.preventDefault();

                    let keyword = inputKeyword.val();

                    location.replace("/product/list?keyword="+keyword);
                })

                inputKeyword.on("keyup", function (e) {
                    let keyword = $(this).val();

                    if (e.keyCode===13) {
                        location.replace("/product/list?keyword="+keyword);
                    }

                })

                // 장바구니
                let cartSize = `<c:out value="${loginInfo.getCartList().size()}"/>` || 0;

                $(".cart-size").next("span").html(`\${cartSize}`);
            })
        </script>