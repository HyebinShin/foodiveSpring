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
                고객 정보 관리
            </div>
            <div class="panel-body">
                <div class="form-group input-group col-lg-6" id="stateSelect">
                    <span class="input-group-addon">회원상태</span>
                    <select class="form-control">
                        <option>-</option>
                        <option value="1">회원</option>
                        <option value="0">탈퇴회원</option>
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
                고객 목록
            </div>
            <div class="panel-body">
                <table class="table table-striped table-bordered table-hover" id="userList">
                    <thead>
                    <tr>
                        <th>#번호</th>
                        <th>아이디</th>
                        <th>가입일자</th>
                        <th>가입상태</th>
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
                <button id="modalCloseBtn" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script src="/resources/js/manageUser.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        let searchBtn = $("#searchBtn");
        let tbody = $("tbody");
        let pageNum = 1;
        let userPageFooter = $(".panel-footer");

        searchBtn.on("click", function () {
            let state = $("#stateSelect :selected").val();
            console.log(state);

            showList(1);

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
                        if(list[i].state === 1) {
                            str += `<td><c:out value="회원"/></td></tr>`;
                        } else {
                            str += `<td><c:out value="탈퇴회원"/></td></tr>`;
                        }

                    }

                    tbody.html(str);

                    showUserPage(userCnt);

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

                    userPageFooter.on("click", "li a", function (e) {
                        e.preventDefault();

                        pageNum = $(this).attr("href");

                        showList(pageNum);
                    })
                })
            }

        })

        let modal = $(".modal");
        let addAdminBtn = $("#addAdminBtn");

        let modalInputId = modal.find("input[name='id']");
        let modalInputPassword = modal.find("input[name='password']");
        let modalInputName = modal.find("input[name='name']");
        let modalInputEmail = modal.find("input[name='email']");
        let modalInputPhone = modal.find("input[name='phone']");

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
                modal.modal("show");
            })
        })
    })
</script>