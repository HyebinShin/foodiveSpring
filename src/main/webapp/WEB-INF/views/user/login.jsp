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
                            <button id="findIdBtn" type="button" data-oper="findId" class="btn btn-default"
                                    data-toggle="collapse" data-target="#divFindId">아이디 찾기
                            </button>
                            <button id="findPasswordBtn" type="button" data-oper="findPassword" class="btn btn-default"
                                    data-toggle="collapse" data-target="#divFindPassword">비밀번호 찾기
                            </button>
                        </form>
                    </fieldset>
                </div>
            </div>
        </div>
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
                <button type="button" id="modalFindIdBtn" class="btn btn-info">아이디 찾기</button>
                <button type="button" id="modalFindPasswordBtn" class="btn btn-info">비밀번호 찾기</button>
                <button type="button" id="modalChangePasswordBtn" class="btn btn-info">비밀번호 재설정</button>
                <input type="hidden" name="userInfoId">
                <button type="button" id="modalCloseBtn" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<%@include file="../includes/footer.jsp" %>

<script src="/resources/js/user.js"></script>
<script src="/resources/js/redirectPage.js"></script>

<script type="text/javascript">
    function initModal(msg) {
        if (msg === '') {
            return;
        }

        $(".modal").find("button[id!='modalCloseBtn']").hide();
        $(".modal-body").html(msg);
    }

    $(document).ready(function () {
        let msg = "";

        let formObj = $("form");
        let id = formObj.find("input[name='id']");
        let password = formObj.find("input[name='password']");

        let loginBtn = $("#loginBtn");
        let findIdBtn = $("#findIdBtn");
        let findPasswordBtn = $("#findPasswordBtn");

        loginBtn.on("click", function (e) {
            if (id.val() === '') {
                msg = '아이디를 입력해주세요';
                initModal(msg);
                return;
            }
            if (password.val() === '') {
                msg = '비밀번호를 입력해주세요.';
                initModal(msg);
                return;
            }

            formObj.submit();
        });

        let modal = $(".modal");
        let myModal = $("#myModal");

        let modalCloseBtn = $("#modalCloseBtn");
        let modalFindIdBtn = $("#modalFindIdBtn");
        let modalFindPasswordBtn = $("#modalFindPasswordBtn");
        let modalChangePasswordBtn = $("#modalChangePasswordBtn");

        let userInfoId = $("input[name='userInfoId']");

        findIdBtn.on("click", function () {
            msg = init().email();

            initModal(msg);

            modalFindIdBtn.show();
            myModal.modal("show");
        })

        modalFindIdBtn.on("click", function () {
            let email = modal.find("input[name='email']").val();

            if(email === '') {
                $("#modalEmailStyle").attr("style", "color:red").html("이메일을 입력해주세요.");
                return;
            }

            let user = {
                email: email
            }

            userService().getUserInfo(user, function (userInfo) {
                modalCloseBtn.on("click");

                if(userInfo.length !== 0) {
                    msg = "가입하신 아이디는 <b>["+userInfo.id+"]</b> 입니다.";
                } else {
                    msg = "해당하는 회원 정보가 없습니다.";
                }

                initModal(msg);

                myModal.modal("show");
            })
        });

        findPasswordBtn.on("click", function () {
            msg = init().id();
            msg += init().email();

            initModal(msg);

            modalFindPasswordBtn.show();
            myModal.modal("show");
        });

        modalFindPasswordBtn.on("click", function () {
            let id = modal.find("input[name='id']").val();
            let email = modal.find("input[name='email']").val();

            if(id === '') {
                $("#modalIdStyle").attr("style", "color:red").html("아이디를 입력해주세요.");
                return;
            }
            if(email === '') {
                $("#modalEmailStyle").attr("style", "color:red").html("이메일을 입력해주세요.");
                return;
            }

            let user = {
                id:id,
                email:email
            }

            userService().getUserInfo(user, function(userInfo) {
                modalCloseBtn.on("click");

                if(userInfo.length !== 0) {
                    userInfoId.val(userInfo.id);

                    msg = init().password();
                    initModal(msg);
                    modalChangePasswordBtn.show();
                    myModal.modal("show");
                } else {
                    msg = "해당하는 회원 정보가 없습니다.";
                    initModal(msg);
                    myModal.modal("show");
                }
            })
        })

        modalChangePasswordBtn.on("click", function () {
            let password = modal.find("input[name='password']").val();
            let passwordCheck = modal.find("input[name='passwordCheck']").val()

            if(!userFunction().validatePassword(password, passwordCheck)) {
                return;
            }

            let user = {
                id:userInfoId.val(),
                password:password
            };
            let node = {
                user:user,
                isPassword:true
            }

            userService().update(node, function (result) {
                modalCloseBtn.on("click");

                if (result==='success') {
                    msg = "비밀번호를 재설정했습니다. 바뀐 비밀번호로 로그인 해주세요.";
                    initModal(msg);
                    myModal.modal("show");
                }
            })

        })

        modal.children().on("change", function () {
            modal.find("span").removeAttr("style").html("");
        })
    })
</script>

<script type="text/javascript">

    $(document).ready(function () {
        let result = '<c:out value="${result}"/>';

        checkModal(result);

        history.replaceState({}, null, null);

        function checkModal(result) {
            if (result === '' || history.state) {
                return;
            }

            $(".modal").find("button[id!='modalCloseBtn']").hide();
            $(".modal-body").html(result);
            $("#myModal").modal("show");
        }

    });
</script>