<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/userCustom.css" rel="stylesheet">

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">회원정보 수정</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <form role="form" action="/user/modify" method="post">
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
                                   value='<c:out value="${userInfo.email}"/>'>
                            <span class="form-check" id="emailCheck"></span>
                            <input type="hidden" name="emailCheck" value='<c:out value="${userInfo.email}"/>'>
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
                            <input type="tel" class="form-control" name="phone" value='<c:out value="${userInfo.phone}"/>'>
                            <span class="form-check" id="phone"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3">주소</label>
                        <div class="col-sm-8 addr">
                            <input class="form-control" name="zipcode" id="zipcode" maxlength="7" value='<c:out value="${userInfo.zipcode}"/>' readonly="readonly">
                            <div class="form-control" id="search_zipcode"><span>우편번호 검색</span></div>
                            <input class="form-control address" name="address1" id="address1" maxlength="70"
                                   value='<c:out value="${userInfo.address1}"/>' readonly="readonly">
                            <input class="form-control address" name="address2" id="address2" maxlength="70" value='<c:out value="${userInfo.address2}"/>'>
                        </div>
                    </div>
                </div>
            </div>
            <button type="button" data-oper="modify" class="btn btn-info">회원정보 수정</button>
            <button type="reset" class="btn btn-default">수정 취소</button>
            <button type="button" data-oper="drop" class="btn btn-danger">회원 탈퇴</button>
        </form>
    </div>
</div>

<!-- result modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 classs="modal-title" id="myModalLabel">FOODIVE</h4>
            </div>
            <div class="modal-body">모달 바디</div>
            <div class="modal-footer">
                <button type="button" id="modalCloseBtn" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
<script src="/resources/js/addr.js"></script>

<script type="text/javascript" src="/resources/js/user.js"></script>

<script>
    function initModal(msg) {
        if (msg === '') {
            return;
        }

        $(".modal-body").html(msg);
    }

    $(document).ready(function () {
        let modal = $(".modal");
        let myModal = $("#myModal");

        $("button").on("click", function () {
            let operation = $(this).data("oper");

            console.log(operation);

            if(operation === 'modify') {
                console.log("modify!!!");

                let email = $("input[name='email']").val();
                let emailCheck = $("input[name='emailCheck']").val();
                let phone = $("input[name='phone']").val();
                let zipcode = $("input[name='zipcode']").val();
                let address1 = $("input[name='address1']").val();
                let address2 = $("input[name='address2']").val();

                if(!userFunction().validateEmail(email, emailCheck)) {
                    console.log("invalidate email")
                    return false;
                }
                if(!userFunction().validatePhone(phone)) {
                    console.log("invalidate phone");
                    return false;
                }

                let user = {
                    id:$("input[name='id']").val(),
                    email:email,
                    phone:phone,
                    zipcode:zipcode,
                    address1:address1,
                    address2:address2
                };
                let node = {
                    user:user,
                    isPassword: false
                };

                console.log("node.user.id:"+node.user.id);

                userService().update(node, function (result) {
                    if(result === 'success') {
                        let msg = "회원정보를 수정했습니다.";
                        initModal(msg);
                        myModal.modal("show");
                    }
                })
            } else if(operation === 'drop') {
                if(confirm("정말 탈퇴하시겠습니까?")) {
                    let formObj = $("form");

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