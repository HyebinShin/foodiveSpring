<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../includes/adminHeader.jsp"%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">고객 정보 관리</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-info">
            <div class="panel-heading">
                관리자 정보 관리
            </div>
            <div class="panel-body">
                <div class="form-group input-group col-lg-6" id="stateSelect">
                    <span class="input-group-addon">회원상태</span>
                    <select class="form-control">
                        <option value="2" selected>관리자</option>
                    </select>
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button" id="searchBtn">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                관리자 목록
                <button id="addAdminBtn" class="btn btn-primary btn-xs pull-right">관리자 등록</button>
            </div>
            <div class="panel-body">
                <table class="table table-striped table-bordered table-hover" id="userList">
                    <thead>
                    <tr>
                        <th>#번호</th>
                        <th>아이디</th>
                        <th>입사일자</th>
                        <th>상태</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            <div class="panel-footer">

            </div>
        </div>
    </div>
</div>

<%@include file="../includes/adminFooter.jsp"%>

<!-- modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">FOODIVE</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>아이디</label>
                    <input class="form-control" name="id" type="text">
                    <span class="form-check" id="idCheck"></span>
                    <input type="hidden" name="idCheck">
                </div>
                <div class="form-group">
                    <label>비밀번호</label>
                    <input class="form-control" name="password" type="password">
                    <span class="form-check" id="password"></span>
                </div>
                <div class="form-group">
                    <label>비밀번호 확인</label>
                    <input class="form-control" name="passwordCheck" type="password">
                    <span class="form-check" id="passwordCheck"></span>
                </div>
                <div class="form-group">
                    <label>이름</label>
                    <input class="form-control" name="name" type="text">
                    <span class="form-check" id="name"></span>
                </div>
                <div class="form-group">
                    <label>이메일</label>
                    <input class="form-control" name="email" type="text">
                    <span class="form-check" id="emailCheck"></span>
                    <input type="hidden" name="emailCheck">
                </div>
                <div class="form-group">
                    <label>전화번호</label>
                    <input type="tel" class="form-control" name="phone" placeholder="ex. 010-1234-5678">
                    <span class="form-check" id="phone"></span>
                </div>
            </div>
            <div class="modal-footer">
                <button id="modalRegisterBtn" type="button" class="btn btn-info">등록</button>
                <button id="modalModBtn" type="button" class="btn btn-info">수정</button>
                <button id="modalModDoBtn" type="button" class="btn btn-info">수정 완료</button>
                <button id="modalResetBtn" type="reset" class="btn btn-default">취소</button>
                <button id="modalCloseBtn" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script src="/resources/js/manageUser.js"></script>

<script type="text/javascript">
    const setCheckValue = function (duplicateCase) {
        return duplicateCase === "I" ? "아이디" : "이메일";
    }

    const check = function (duplicateParam, duplicateCase, style, checkInput) {
        console.log(`\${duplicateParam}, \${duplicateCase}, \${style}, \${checkInput}`);
        if(!userFunction().checkValidate(duplicateParam, duplicateCase)) {
            console.log("invalidated...")
            return;
        }

        let duplicateInfo = {
            duplicateParam:duplicateParam,
            duplicateCase:duplicateCase
        };

        console.log("duplicate info...")

        userService().check(duplicateInfo, function (result) {
            if(result === 'success') {
                style.attr("style", "color:#337ab7").html(`사용 가능한 \${setCheckValue(duplicateCase)}입니다.`);
                style.closest("div").attr("class", "has-success");
                checkInput.val(duplicateParam);
            } else {
                style.attr("style", "color:red").html(`이미 사용 중인 \${setCheckValue(duplicateCase)}입니다.`);
                style.closest("div").attr("class", "has-error");
            }
        })
    }

    $(document).ready(function () {
        let searchBtn = $("#searchBtn");
        let tbody = $("tbody");
        let pageNum = 1;
        let userPageFooter = $(".panel-footer");
        let state = $("#stateSelect :selected").val();

        function showUserPage(userCnt) {
            let endNum = Math.ceil(pageNum/10.0) * 10;
            let startNum = endNum - 9;

            let prev = startNum !== 1;
            let next = false;

            if (endNum * 10 >= userCnt) {
                endNum = Math.ceil(userCnt/10.0);
            }

            if (endNum * 10 < userCnt) {
                next = true;
            }

            let str = `<ul class='pagination pull-right'>`;

            if (prev) {
                str += `<li class='page-item'><a class='page-link' href='\${startNum-1}'>Previous</a></li>`;
            }

            for (let i=startNum; i<=endNum; i++) {
                let active = pageNum === i ? 'active' : '';
                str += `<li class='page-item \${active}'><a class='page-link' href='\${i}'>\${i}</a></li>`;
            }

            if (next) {
                str += `<li class='page-item'><a class='page-link' href='\${endNum+1}'>Previous</a></li>`;
            }

            str += '</ul>';

            userPageFooter.html(str);

            console.log(endNum);
        }

        function showList(page) {
            console.log("show list: "+page);

            userService().getList({state:state,page:page||1}, function (userCnt, list) {
                console.log("userCnt: "+userCnt);
                console.log("list: "+list);

                if (page == -1) {
                    pageNum = Math.ceil(userCnt/10.0);
                    showList(pageNum);
                    return;
                }

                let str = "";

                if (list == null || list.length === 0) {
                    return;
                }

                for (let i=0, len=list.length||0; i<len; i++) {
                    str += `<tr data-id='<c:out value="\${list[i].id}"/>'><td><c:out value="\${list[i].uno}"/></td>`;
                    str += `<td><c:out value="\${list[i].id}"/></td>`;
                    str += `<td><c:out value="\${userService().displayTime(list[i].regDate)}"/></td>`;
                    if(list[i].state === 2) {
                        str += `<td><c:out value="관리자"/></td></tr>`;
                    } else {
                        str += `<td><c:out value="탈퇴회원"/></td></tr>`;
                    }

                }

                tbody.html(str);
                showUserPage(userCnt);
            })
        }

        searchBtn.on("click", function () {
            state = $("#stateSelect :selected").val();
            console.log(state);

            showList(1);

            userPageFooter.on("click", "li a", function (e) {
                e.preventDefault();

                pageNum = $(this).attr("href");

                showList(pageNum);
            })

        })

        let modal = $(".modal");
        let addAdminBtn = $("#addAdminBtn");

        let modalInputId = modal.find("input[name='id']");
        let modalInputPassword = modal.find("input[name='password']");
        let modalInputName = modal.find("input[name='name']");
        let modalInputEmail = modal.find("input[name='email']");
        let modalInputPhone = modal.find("input[name='phone']");

        let modalModBtn = $("#modalModBtn");
        let modalModDoBtn = $("#modalModDoBtn");
        let modalRegisterBtn = $("#modalRegisterBtn");
        let modalResetBtn = $("#modalResetBtn");
        let validateCnt = 0;

        addAdminBtn.on("click", function () {
            modal.find("input").val("");
            modal.find("input").removeAttr("readOnly");
            modal.find("button[id!='modalCloseBtn']").hide();

            modalRegisterBtn.show();
            modalResetBtn.show();

            modal.modal("show");

            validateCnt = 0;
        })

        modal.find("input").each(function () {
            $(this).on("change", function () {
                modal.find("span").empty();

                let name = $(this).attr("name");
                let param = $(this).val();
                console.log("CHECK name: "+name);

                switch (name) {
                    case 'id': case 'email':
                        let duplicateParam = param
                        let style, checkInput, checkCase;
                        if(name === 'id') {
                            style = $("#idCheck");
                            checkInput = $("input[name='idCheck']");
                            checkCase = "I";
                        } else {
                            style = $("#emailCheck");
                            checkInput = $("input[name='emailCheck']");
                            checkCase = "E";
                        }
                        check(duplicateParam, checkCase, style, checkInput);
                        validateCnt++;
                        break;
                    case 'name':
                        userFunction().validateName(param);
                        validateCnt++;
                        break;
                    case 'phone':
                        userFunction().validatePhone(param);
                        validateCnt++;
                        break;
                    case 'password':
                        userFunction().validatePassword(param);
                        validateCnt++;
                        break;
                    case 'passwordCheck':
                        userFunction().validatePasswordCheck($("input[name='password']").val(), param);
                        validateCnt++;
                        break;
                }
            })
        })

        modalRegisterBtn.on("click", function () {
            if(validateCnt!==6) {
                console.log("입력 정보 부족");
                return;
            }

            let user = {
                id: modalInputId.val(),
                password: modalInputPassword.val(),
                name:modalInputName.val(),
                email:modalInputEmail.val(),
                phone:modalInputPhone.val(),
                state:2
            }

            userService().add(user, function (result) {
                if(result==='success') {
                    alert('새로운 관리자 등록 완료!');
                }

                modal.find("input").val("");
                modal.modal("hide");

                showList(-1);
            })
        })

        tbody.on("click", "tr", function (e) {
            let id = $(this).data("id");

            console.log("id: "+id);
            let user = {
                id:id
            }

            userService().getUserInfo(user, function (user) {
                modal.find("input[name='passwordCheck']").closest("div").hide();
                modalInputId.val(user.id);
                modalInputPassword.val("****");
                modalInputName.val(user.name);
                modalInputEmail.val(user.email);
                modalInputPhone.val(user.phone);
                modal.find("input").attr("readOnly", "readOnly");

                modal.find("button[id!='modalCloseBtn']").hide();
                modalModBtn.show();
                modal.modal("show");
            })
        })

        modalModBtn.on("click", function () {
            modalInputEmail.removeAttr("readOnly");
            modalInputPhone.removeAttr("readOnly");
            modalResetBtn.show();
            modalModBtn.hide();
            modalModDoBtn.show();

            let cloneEmail = modalInputEmail.val();
            let clonePhone = modalInputPhone.val();

            let validateCnt = 0;

            modal.find("input").each(function () {
                $(this).on("change", function () {
                    modal.find("span").empty();

                    let name = $(this).attr("name");
                    let param = $(this).val();
                    console.log("CHECK name: "+name);

                    switch (name) {
                        case 'email':
                            let duplicateParam = param
                            let style, checkInput, checkCase;
                            if(name === 'email') {
                                style = $("#emailCheck");
                                checkInput = $("input[name='emailCheck']");
                                checkCase = "E";
                            }
                            check(duplicateParam, checkCase, style, checkInput);
                            validateCnt++;
                            break;
                        case 'phone':
                            userFunction().validatePhone(param);
                            validateCnt++;
                            break;
                    }
                })
            })


            modalModDoBtn.on("click", function () {
                if(cloneEmail===modalInputEmail.val()) {
                    validateCnt++;
                }
                if(clonePhone===modalInputPhone.val()) {
                    validateCnt++;
                }
                console.log("수정시도: "+validateCnt);
                if(validateCnt>=2) {
                    let user = {
                        id:modalInputId.val(),
                        email:modalInputEmail.val(),
                        phone:modalInputPhone.val()
                    }
                    let node = {
                        user:user,
                        isPassword:false
                    }
                    console.log("수정완료");
                    userService().update(node, function (result) {
                        alert(result);
                        modal.modal("hide");
                        showList(pageNum);
                    })
                }
            })

        })


    })
</script>