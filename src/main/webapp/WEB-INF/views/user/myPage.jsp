<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/userCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">마이페이지</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <form role="form" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">회원 기본 정보</div>
                <div class="panel-body info-basic">
                    <div class="form-group">
                        <label class="col-sm-3">아이디</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="id"
                                   value='<c:out value="${userInfo.id}"/>' readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">비밀번호</label>
                        <div class="col-sm-8">
                            <input type="password" class="form-control" name="password"
                                   value='<c:out value="password"/>' readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">이름</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="name"
                                   value='<c:out value="${userInfo.name}"/>'
                                   readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">이메일</label>
                        <div class="col-sm-8">
                            <input type="email" class="form-control" name="email"
                                   value='<c:out value="${userInfo.email}"/>' readonly="readonly">
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">회원 추가 정보</div>
                <div class="panel-body info-additional">
                    <div class="form-group">
                        <label class="col-sm-3">생년월일</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="birthday"
                                   value='<c:out value="${userInfo.birthday}"/>' readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">성별</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="sex" value='<c:out value="${userInfo.sex}"/>'
                                   readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">전화번호</label>
                        <div class="col-sm-8">
                            <input type="tel" class="form-control" name="phone"
                                   value='<c:out value="${userInfo.phone}"/>'
                                   readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">주소</label>
                        <div class="col-sm-8 addr">
                            <input class="form-control" name="zipcode" id="zipcode" maxlength="7"
                                   value='<c:out value="${userInfo.zipcode}"/>' readonly="readonly">
                            <input class="form-control address" name="address1" id="address1" maxlength="70"
                                   value='<c:out value="${userInfo.address1}"/>' readonly="readonly">
                            <input class="form-control address" name="address2" id="address2" maxlength="70"
                                   value='<c:out value="${userInfo.address2}"/>' readonly="readonly">
                        </div>
                    </div>
                </div>
            </div>
            <button type="button" data-oper="modify" class="btn btn-info">회원정보 수정</button>
            <button type="button" data-oper="drop" class="btn btn-danger">회원 탈퇴</button>
        </form>
    </div>
</div>

<script type="text/javascript" src="/resources/js/user.js"></script>

<script>
    $(document).ready(function () {
        let formObj = $("form");

        $("button").on("click", function () {
            let operation = $(this).data("oper");

            console.log(operation);

            if (operation === 'modify') {
                location.replace("/user/modify");
            } else if(operation === 'drop') {
                if(confirm("정말 탈퇴하시겠습니까?")) {
                    formObj.attr("action", "/user/drop");

                    let idTag = $("input[name='id']").clone();

                    formObj.empty();
                    formObj.append(idTag);
                    formObj.submit();
                }
            }
        })
    });
</script>

<%@include file="../includes/footer.jsp" %>