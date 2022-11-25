<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@include file="../includes/adminHeader.jsp"%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">카테고리 관리</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-info">
            <div class="panel-heading">
                카테고리 관리
            </div>
            <div class="panel-body">
                <div class="form-group input-group col-lg-6" id="stateSelect">
                    <span class="input-group-addon">카테고리 상태</span>
                    <select class="form-control">
                        <option value="1" selected>활성화</option>
                        <option value="0">비활성화</option>
                    </select>
                    <span class="input-group-btn">
                        <button class="btn btn-default searchBtn" type="button">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
                </div>
                <div class="form-group input-group col-lg-6" id="categorySelect">
                    <span class="input-group-addon">상위 카테고리</span>
                    <select class="form-control">
                        <option value="null">-</option>
                        <c:forEach items="${gnb.getList()}" var="category">
                            <option value='<c:out value="${category.code}"/>'><c:out value="${category.name}"/></option>
                        </c:forEach>
                    </select>
                    <span class="input-group-btn">
                        <button class="btn btn-default searchBtn" type="button">
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
                카테고리 목록
                <button id="addCategoryBtn" class="btn btn-primary btn-xs pull-right">카테고리 등록</button>
            </div>
            <div class="panel-body">
                <table class="table table-striped table-bordered table-hover" id="userList">
                    <thead>
                    <tr>
                        <th>#번호</th>
                        <th>카테고리코드</th>
                        <th>카테고리명</th>
                        <th>등록일자</th>
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
                    <label>카테고리 국문명</label>
                    <input class="form-control" name="name" type="text">
                    <span class="form-check" id="nameCheck"></span>
                    <input type="hidden" name="nameCheck">
                </div>
                <div class="form-group">
                    <label>카테고리 영문명</label>
                    <input class="form-control" name="eName" type="text">
                    <span class="form-check" id="eNameCheck"></span>
                    <input type="hidden" name="eNameCheck">
                </div>
                <div class="form-group">
                    <label>상위 카테고리</label>
                    <select class="form-control" name="hCode">
                        <option value="null">해당 카테고리의 상위 카테고리 없음</option>
                        <c:forEach items="${gnb.getList()}" var="category">
                            <option value='<c:out value="${category.code}"/>'><c:out value="${category.name}"/></option>
                        </c:forEach>
                    </select>
                    <span class="form-check" id="hCode"></span>
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

<script src="/resources/js/category.js"></script>

<script type="text/javascript">
    const setCheckValue = function (duplicateCase) {
        return duplicateCase === "N" ? "카테고리 국문명" : "카테고리 영문명";
    }

    const check = function (duplicateParam, duplicateCase, style, checkInput) {
        let checkBoolean;
        console.log(`\${duplicateParam}, \${duplicateCase}, \${style}, \${checkInput}`);
        if(!categoryFunction().checkValidate(duplicateParam, duplicateCase)) {
            console.log("invalidated...");
            return;
        }
        let duplicateInfo = {
            duplicateParam:duplicateParam,
            duplicateCase:duplicateCase
        };

        categoryService().check(duplicateInfo, function (result) {
            let color, divClass;
            if(result.indexOf("가능")!==-1) {
                color = "color:#337ab7";
                divClass = "has-success";
                checkInput.val(duplicateParam);
                checkBoolean = true;
            } else {
                color = "color:red";
                divClass = "has-error";
                checkBoolean = false;
            }
            style.attr("style", color).html(result);
            style.closest("div").attr("class", divClass);
        })
        return checkBoolean;
    }

    $(document).ready(function () {
        let searchBtn = $(".searchBtn");
        let tbody = $("tbody");
        let pageNum = 1;
        let categoryPageFooter = $(".panel-footer");
        let state = $("#stateSelect:selected").val();

        function showCategoryPage(categoryCnt) {
            let endNum = Math.ceil(pageNum/10.0) * 10;
            let startNum = endNum - 9;

            let prev = startNum !== 1;
            let next = false;

            if(endNum * 10 >= categoryCnt) {
                endNum = Math.ceil(categoryCnt/10.0);
            } else {
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

            categoryPageFooter.html(str);

            console.log(endNum);
        }

        function showList(page) {

        }

        let addCategoryBtn = $("#addCategoryBtn");
        let modal = $(".modal");

        let modalInputName = modal.find("input[name='name']");
        let modalInputEName = modal.find("input[name='eName']");
        let modalInputHCode = modal.find("select[name='hCode']");

        let modalModBtn = $("#modalModBtn");
        let modalModDoBtn = $("#modalModDoBtn");
        let modalRegisterBtn = $("#modalRegisterBtn");
        let modalResetBtn = $("#modalResetBtn");
        let validateCnt = 0;

        addCategoryBtn.on("click", function () {
            modal.find("input").val("");
            modal.find("select").val([]);
            modal.find("button[id!='modalCloseBtn']").hide();

            modalRegisterBtn.show();
            modalResetBtn.show();

            modal.modal("show");

            validateCnt = 0;
        })

        modal.find("input"||"select").each(function () {
            $(this).on("change", function () {
                modal.find("span").empty();

                let name = $(this).attr("name");
                let param = $(this).val();
                console.log(`name: \${name} & param: \${param}`);

                switch(name) {
                    case 'name': case 'eName':
                        let duplicateParam = param;
                        let style, checkInput, checkCase;
                        if(name==='name') {
                            style = $("#nameCheck");
                            checkInput = modal.find("input[name='nameCheck']");
                            checkCase = "N";
                        } else {
                            style = $("#eNameCheck");
                            checkInput = modal.find("input[name='eNameCheck']");
                            checkCase = "E";
                        }
                        if(check(duplicateParam, checkCase, style, checkInput)) {
                            validateCnt++;
                        }
                        break;
                }
            })
        })


    });
</script>