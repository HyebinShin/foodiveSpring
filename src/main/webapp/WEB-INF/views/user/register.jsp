<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp"%>
<link href="/resources/css/userCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">회원가입</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">회원가입</div>
            <div class="panel-body">
                <form role="form" action="/user/register" method="post">
                    <div class="info-basic">
                        <div class="form-group">
                            <label class="col-sm-6">아이디</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="id" placeholder="validate id">
                                <input type="hidden" name="idCheck">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">비밀번호</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" name="password">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">비밀번호 확인</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" name="passwordCheck">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">이름</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="name">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">이메일</label>
                            <div class="col-sm-6">
                                <input type="email" class="form-control" name="email">
                            </div>
                        </div>
                    </div>
                    <div class="info-additional">
                        <div class="form-group">
                            <label class="col-sm-6">생년월일</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="birthday">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">성별</label>
                            <div class="col-sm-6">
                                <input type="radio" name="sex" value="남"> 남
                                <input type="radio" name="sex" value="여"> 여
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">전화번호</label>
                            <div class="col-sm-6">
                                <input type="tel" class="form-control" name="phone">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-6">주소</label>
                            <div class="col-sm-6 addr">
                                <input class="form-control" name="zipcode" id="zipcode" maxlength="7" readonly>
                                <div class="form-control" id="search_zipcode"><span>우편번호 검색</span></div>
                                <input class="form-control address" name="address1" id="address1" maxlength="70" readonly>
                                <input class="form-control address" name="address2" id="address2" maxlength="70">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
<script src="/resources/js/addr.js"></script>

<%@include file="../includes/footer.jsp"%>