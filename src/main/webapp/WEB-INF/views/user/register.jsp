<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/userCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">회원가입</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <form role="form" action="/user/register" method="post">
            <div class="panel panel-default">
                <div class="panel-heading">기본 정보 입력</div>
                <div class="panel-body info-basic">
                    <div class="form-group">
                        <label class="col-sm-3">아이디</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="id"
                                   placeholder="6 ~ 12자의 영문자와 숫자로 입력해주세요.">
                            <span class="form-check" id="idCheck"></span>
                            <input type="hidden" name="idCheck">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">비밀번호</label>
                        <div class="col-sm-8">
                            <input type="password" class="form-control" name="password"
                                   placeholder="8 ~ 24자의 영문자와 숫자로 입력해주세요.">
                            <span class="form-check" id="password"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">비밀번호 확인</label>
                        <div class="col-sm-8">
                            <input type="password" class="form-control" name="passwordCheck"
                                   placeholder="비밀번호를 한 번 더 입력해주세요.">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">이름</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="name" placeholder="이름을 입력해주세요.">
                            <span class="form-check" id="name"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">이메일</label>
                        <div class="col-sm-8">
                            <input type="email" class="form-control" name="email"
                                   placeholder="ex. foodive123@foodive.com">
                            <span class="form-check" id="email"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-default collapse" id="toggle">
                <div class="panel-heading">추가 정보 입력</div>
                <div class="panel-body info-additional">
                    <div class="form-group">
                        <label class="col-sm-3">생년월일</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="birthday" placeholder="생년월일 8자리를 입력해주세요.">
                            <span class="form-check" id="birthday"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">성별</label>
                        <div class="col-sm-8">
                            <input type="radio" name="sex" value="남"> 남
                            <input type="radio" name="sex" value="여"> 여
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">전화번호</label>
                        <div class="col-sm-8">
                            <input type="tel" class="form-control" name="phone" placeholder="ex. 010-1234-5678">
                            <span class="form-check" id="phone"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">주소</label>
                        <div class="col-sm-8 addr">
                            <input class="form-control" name="zipcode" id="zipcode" maxlength="7" readonly>
                            <div class="form-control" id="search_zipcode"><span>우편번호 검색</span></div>
                            <input class="form-control address" name="address1" id="address1" maxlength="70"
                                   readonly>
                            <input class="form-control address" name="address2" id="address2" maxlength="70">
                        </div>
                    </div>
                </div>
            </div>
            <button type="button" class="btn btn-default" data-toggle="collapse" data-target="#toggle">추가 정보 입력</button>
            <button type="button" class="btn btn-info submit">회원가입 완료</button>
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