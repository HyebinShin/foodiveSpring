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
            <li class="gnb-main">
                <a href="#">카테고리</a>
                <div>
                    <ul class="gnb-sub gnb-transition-before bg-white open-menu">
                        <li>상위 카테고리1
                            <ul class="bg-blue gnb-transition-before">
                                <li>하위 카테고리1-1</li>
                                <li class="divider"></li>
                                <li>하위 카테고리2</li>
                                <li class="divider"></li>
                                <li>하위 카테고리3</li>
                                <li class="divider"></li>
                                <li>하위 카테고리4</li>
                            </ul>
                        </li>
                        <li class="divider"></li>
                        <li>상위 카테고리2
                            <ul class="bg-blue gnb-transition-before">
                                <li>하위 카테고리2-1</li>
                                <li class="divider"></li>
                                <li>하위 카테고리2</li>
                                <li class="divider"></li>
                                <li>하위 카테고리3</li>
                                <li class="divider"></li>
                                <li>하위 카테고리4</li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </li>
        </ul>
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
                        </c:when>
                        <c:when test="${!empty state && state ne '0'}">
                            <li>
                                <a href="#">장바구니</a>
                            </li>
                            <li>
                                <a href="/user/myPage">마이페이지</a>
                            </li>
                            <li>
                                <a href="#">로그아웃</a>
                            </li>
                            <c:if test="${state eq '2'}">
                                <li href="#">관리자 페이지</li>
                            </c:if>
                        </c:when>
                    </c:choose>
                </ul>
            </li>
        </ul>
    </nav>

    <div id="page-wrapper" style="margin: 0 auto;">

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js" type="text/javascript"></script>

        <script type="text/javascript">
            console.log('state: <c:out value="${loginInfo.state}"/>');
        </script>