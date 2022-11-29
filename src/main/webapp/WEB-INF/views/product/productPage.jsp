<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@include file="../includes/adminHeader.jsp"%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">상품 관리</h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-6">
        <div class="panel panel-info">
            <div class="panel-heading">
                상품 관리
            </div>
            <div class="panel-body">
                <div class="form-group input-group col-lg-8" id="stateSelect">
                    <span class="input-group-addon">상품 상태</span>
                    <select class="form-control">
                        <option value="1" selected>판매</option>
                        <option value="0">판매중지</option>
                        <option value="2">품절</option>
                    </select>
                    <span class="input-group-btn">
                        <button class="btn btn-default searchBtn" type="button">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
                </div>
                <div class="form-group input-group col-lg-8" id="keyword">
                    <span class="input-group-addon">상품 검색</span>
                    <input type="text" class="form-control" placeholder="상품명을 입력해주세요.">
                    <span class="input-group-btn">
                        <button class="btn btn-default searchBtn" type="button">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="panel panel-info">
            <div class="panel-heading">
                카테고리 선택
            </div>
            <div class="panel-body">
                <div class="form-group input-group col-lg-8" id="categorySelect">
                    <span class="input-group-addon">상위 카테고리</span>
                    <select class="form-control" id="categorySelectId">
                        <option value="all" selected>-</option>
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
                <div class="form-group input-group col-lg-8" id="lowCategorySelect">
                    <span class="input-group-addon">하위 카테고리</span>
                    <select class="form-control" id="lowCategorySelectId">

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
                상품 목록
                <button id="addCategoryBtn" class="btn btn-primary btn-xs pull-right">상품 등록</button>
            </div>
            <div class="panel-body">
                <table class="table table-striped table-bordered table-hover" id="userList">
                    <thead>
                    <tr>
                        <th>#번호</th>
                        <th>상품명</th>
                        <th>카테고리 코드</th>
                        <th>등록일자</th>
                        <th>재고</th>
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
                    <label>카테고리 번호</label>
                    <input class="form-control" name="code" type="text" readonly>
                </div>
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
                <div class="form-group" id="hCodeSelect">
                    <label>상위 카테고리</label>
                    <select class="form-control" name="hCode" id="hCodeSelected">
                        <option value="null">해당 카테고리의 상위 카테고리 없음</option>
                        <c:forEach items="${gnb.getList()}" var="category">
                            <option value='<c:out value="${category.code}"/>'><c:out value="${category.name}"/></option>
                        </c:forEach>
                    </select>
                    <span class="form-check" id="hCode"></span>
                </div>
                <div class="form-group" id="dropSelect">
                    <label>활성화/비활성화</label>
                    <select class="form-control" id="dropSelected">
                        <option value="1">활성화</option>
                        <option value="0">비활성화</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>등록일자</label>
                    <input class="form-control" name="regDate" type="text" readonly>
                </div>
                <div class="form-group">
                    <label>수정일자</label>
                    <input class="form-control" name="modDate" type="text" readonly>
                </div>
                <div class="form-group">
                    <label>비활성화 일자</label>
                    <input class="form-control" name="dropDate" type="text" readonly>
                </div>
            </div>
            <div class="modal-footer">
                <button id="modalRegisterBtn" type="button" class="btn btn-info">등록</button>
                <button id="modalModBtn" type="button" class="btn btn-info">수정</button>
                <button id="modalModDoBtn" type="button" class="btn btn-info">수정 완료</button>
                <button id="modalResetBtn" type="reset" class="btn btn-default">취소</button>
                <button id="modalDropBtn" type="button" class="btn btn-danger">비활성화</button>
                <button id="modalCloseBtn" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script src="/resources/js/productService.js"></script>
<script src="/resources/js/productFunction.js"></script>

<script type="text/javascript">
$(document).ready(function () {
    let searchBtn = $(".searchBtn");
    let lowCategorySelectId = $("#lowCategorySelectId");

    let state = $("#stateSelect :selected").val();
    let hCode = $("#categorySelect :selected").val();

    searchBtn.each(function () {
        $(this).on("click", function () {
            let closestDiv = $(this).closest("div").attr("id");
            if(closestDiv==='categorySelect') {
                console.log(closestDiv);

                hCode = $(`#categorySelect :selected`).val();
                console.log("선택한 상위 카테고리: "+hCode);

                productService().getCategoryList(hCode, function (categoryCnt, list) {
                    console.log("받아온 리스트: "+JSON.stringify(list));
                    initPage().initLowCategoryList(list, lowCategorySelectId);
                })
            }
        })

    })

})
</script>