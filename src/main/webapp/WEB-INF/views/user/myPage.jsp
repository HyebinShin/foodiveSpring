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
        <form role="form" action="/user/register" method="post">
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
                            <input type="text" class="form-control" name="name" value='<c:out value="${userInfo.name}"/>' readonly="readonly">
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
                            <input type="text" class="form-control" name="birthday" value='<c:out value="${userInfo.birthday}"/>' readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">성별</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="sex" value='<c:out value="${userInfo.sex}"/>' readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">전화번호</label>
                        <div class="col-sm-8">
                            <input type="tel" class="form-control" name="phone" value='<c:out value="${userInfo.phone}"/>' readonly="readonly">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">주소</label>
                        <div class="col-sm-8 addr">
                            <input class="form-control" name="zipcode" id="zipcode" maxlength="7" value='<c:out value="${userInfo.zipcode}"/>' readonly="readonly">
                            <input class="form-control address" name="address1" id="address1" maxlength="70"
                                   value='<c:out value="${userInfo.address1}"/>' readonly="readonly">
                            <input class="form-control address" name="address2" id="address2" maxlength="70" value='<c:out value="${userInfo.address2}"/>' readonly="readonly">
                        </div>
                    </div>
                </div>
            </div>
            <button type="button" data-oper="modify" class="btn btn-info">회원정보 수정</button>
            <button type="button" data-oper="drop" class="btn btn-danger">회원 탈퇴</button>
        </form>
    </div>
</div>


<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
<script src="/resources/js/addr.js"></script>

<script type="text/javascript" src="/resources/js/user.js"></script>

<script>
    $(document).ready(function () {
        $("input[name='id']").on("focusout", function () {
            let id = $("input[name='id']").val();

            if (!userFunction().idValidate(id)) {
                return;
            }

            userService().idCheck(id, function (result) {
                if (result === 'success') {
                    $("#idCheck").attr("style", "color:#337ab7").html("사용 가능한 아이디입니다.");
                    $("input[name='idCheck']").val(id);
                } else {
                    $("#idCheck").attr("style", "color:red").html("이미 사용 중인 아이디입니다.");
                }
            });
        });


        $(".submit").on("click", function () {

            if (!userFunction().validate()) {
                return;
            }

            $("input[name='idCheck']").remove();
            $("input[name='passwordCheck']").remove();

            let birthday = $("input[name='birthday']").val();

            if(birthday !== '') {
                try {
                    if(birthday.length === 8) {
                        birthday = birthday.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3');
                    }
                } catch (e) {
                    console.log(e);
                }

                $("input[name='birthday']").val(birthday);
            }


            $("input").each(function () {
                if ($(this).val() === '') {
                    $(this).remove();
                }
            })

            $("form").submit();
        });

        $("input[type!='hidden']").on("change", function () {
            $(".form-check").empty();
        })
    });
</script>

<%@include file="../includes/footer.jsp" %>