<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/header.jsp" %>
<link href="/resources/css/userCustom.css" rel="stylesheet">

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Login</h3>
                </div>
                <div class="panel-body">
                    <fieldset>
                        <form role="form" action="/user/login" method="post">
                            <div class="form-group">
                                <input class="form-control" placeholder="아이디를 입력해주세요." name="id" type="text" autofocus>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="비밀번호를 입력해주세요." name="password" type="password">
                            </div>
                            <button id="loginBtn" type="button" data-oper="login" class="btn btn-primary">로그인</button>
                            <button id="findIdBtn" type="button" data-oper="findId" class="btn btn-default">아이디 찾기</button>
                            <button id="findPasswordBtn" type="button" data-oper="findPassword" class="btn btn-default">아이디
                                찾기
                            </button>
                        </form>
                    </fieldset>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 classs="modal-title" id="myModalLabel">FOODIVE</h4>
            </div>
            <div class="modal-body">모달 바디</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<%@include file="../includes/footer.jsp" %>

<script src="/resources/js/user.js"></script>
<script src="/resources/js/redirectPage.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        let result = '<c:out value="${result}"/>';

        checkModal(result);

        history.replaceState({}, null, null);

        function checkModal(result) {
            if (result === '' || history.state) {
                return;
            }

            $(".modal-body").html(result);
            $("#myModal").modal("show");
        }

        let formObj = $("form");
        let id = formObj.find("input[name='id']");
        let password = formObj.find("input[name='password']");

        let loginBtn = $("#loginBtn");
        let findIdBtn = $("#findIdBtn");
        let findPasswordBtn = $("#findPasswordBtn");

        loginBtn.on("click", function (e) {
            if(id.val()==='') {
                result = '아이디를 입력해주세요';
                return;
            }
            if(password.val()==='') {
                result = '비밀번호를 입력해주세요.';
                return;
            }

            formObj.submit();
        })
    });
</script>