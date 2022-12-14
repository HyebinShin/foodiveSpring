<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@include file="../includes/adminHeader.jsp" %>
<link href="/resources/css/productCustom.css" rel="stylesheet">
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
                <div class="form-group input-group col-lg-12" id="stateSelect">
                    <span class="input-group-addon">상품 상태</span>
                    <select class="form-control" id="stateSelectId">
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
                <div class="form-group input-group col-lg-12" id="keyword">
                    <span class="input-group-addon">상품 검색</span>
                    <input type="text" class="form-control" name="keyword" placeholder="상품명을 입력해주세요.">
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
                <div class="form-group input-group col-lg-12" id="categorySelect">
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
                <div class="form-group input-group col-lg-12" id="lowCategorySelect">
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
                <button id="addProductBtn" class="btn btn-primary btn-xs pull-right">상품 등록</button>
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

<%@include file="../includes/adminFooter.jsp" %>

<!-- modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 700px">
        <div class="modal-content">
            <form role="form" method="post">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">FOODIVE</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>상품 번호</label>
                        <input class="form-control" name="code" type="text" readonly>
                    </div>
                    <div class="form-group">
                        <label>상품 국문명</label>
                        <input class="form-control" name="name" type="text">
                        <span class="form-check" id="nameCheck"></span>
                        <input type="hidden" name="nameCheck">
                    </div>
                    <div class="form-group">
                        <label>상품 영문명</label>
                        <input class="form-control" name="eName" type="text">
                        <span class="form-check" id="eNameCheck"></span>
                        <input type="hidden" name="eNameCheck">
                    </div>
                    <div class="form-group" id="hCodeSelect">
                        <label>상위 카테고리</label>
                        <select class="form-control" name="hCode" id="hCodeSelected">
                            <option value="null">해당 카테고리의 상위 카테고리 없음</option>
                            <c:forEach items="${gnb.getList()}" var="category">
                                <option value='<c:out value="${category.code}"/>'><c:out
                                        value="${category.name}"/></option>
                            </c:forEach>
                        </select>
                        <span class="form-check" id="hCode"></span>
                    </div>
                    <div class="form-group" id="codeSelect">
                        <label>하위 카테고리</label>
                        <select class="form-control" name="code" id="codeSelected">

                        </select>
                        <span class="form-check" id="code"></span>
                    </div>
                    <div class="form-group" id="price">
                        <label>가격</label>
                        <input class="form-control" type="text" name="price">
                        <span class="form-check" id="priceCheck"></span>
                    </div>
                    <div class="form-group" id="discount">
                        <label>할인율</label>
                        <input class="form-control" type="text" name="discount">
                        <span class="form-check" id="discountCheck"></span>
                    </div>
                    <div class="form-group" id="image">
                        <label>상품 이미지</label>
                        <input class="form-control" type="file" name="image" multiple>
                        <span class="form-check" id="imageCheck"></span>
                    </div>
                    <div class="product-img">
                        <ul>

                        </ul>
                    </div>
                    <div class="uploadResult">
                        <ul>

                        </ul>
                    </div>
                    <div class="form-group" id="nation">
                        <label>원산지</label>
                        <input class="form-control" type="text" name="nation">
                        <span class="form-check" id="nationCheck"></span>
                    </div>
                    <div class="form-group" id="detail">
                        <label>상품 상세</label>
                        <div id="product-detail"></div>
                        <div id="smart-editor">
                            <textarea name="content" id="content" style="width:100%"></textarea>
                        </div>
                        <span class="form-check" id=""></span>
                    </div>
                    <div class="form-group" id="stock">
                        <label>재고</label>
                        <input class="form-control" type="text" name="stock">
                        <span class="form-check" id="stockCheck"></span>
                    </div>
                    <div class="form-group" id="dropSelect">
                        <label>판매 상태 변경</label>
                        <select class="form-control" name="state" id="dropSelected">
                            <option value="1">판매</option>
                            <option value="0">판매중지</option>
                            <option value="2" disabled>품절</option>
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
                        <label>판매중지 일자</label>
                        <input class="form-control" name="dropDate" type="text" readonly>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="modalRegisterBtn" type="button" class="btn btn-info submit">등록</button>
                    <button id="modalModBtn" type="button" class="btn btn-info">수정</button>
                    <button id="modalModDoBtn" type="button" class="btn btn-info submit">수정 완료</button>
                    <button id="modalResetBtn" type="reset" class="btn btn-default">취소</button>
                    <button id="modalDropBtn" type="button" class="btn btn-danger">비활성화</button>
                    <button id="modalCloseBtn" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="/resources/smart_editor_2/js/HuskyEZCreator.js"></script>
<script src="/resources/js/productService.js"></script>
<script src="/resources/js/productFunction.js"></script>

<script type="text/javascript">
    var oEditors = [];

    $(document).ready(function () {
        nhn.husky.EZCreator.createInIFrame({
            oAppRef: oEditors,
            elPlaceHolder: "content",
            sSkinURI: "/resources/smart_editor_2/SmartEditor2Skin.html",
            fCreator: "createSEditor2",
            fOnAppLoad: function () {
                $("iframe").css("width", "100%").css("height", "299px");
            }
        });

        $("#addProductBtn").click(function () {
            oEditors.getById["content"].exec("SET_IR", [""]);
            initPage().initRegisterPage();
        })

        let searchBtn = $(".searchBtn");
        let lowCategorySelectId = $("#lowCategorySelectId");
        let pageFooter = $('.panel-footer');

        let state = $("#stateSelect :selected").val();
        let hCode = $("#categorySelect :selected").val();
        let pageNum = 1;

        searchBtn.each(function () {
            $(this).on("click", function () {
                let closestDiv = $(this).closest("div").attr("id");
                if (closestDiv === 'categorySelect') {
                    console.log(closestDiv);

                    hCode = $(`#categorySelect :selected`).val();
                    console.log("선택한 상위 카테고리: " + hCode);

                    productService().getCategoryList(hCode, function (categoryCnt, list) {
                        console.log("받아온 리스트: " + JSON.stringify(list));
                        initPage().initLowCategoryList(list, lowCategorySelectId);
                    })

                } else {
                    initPage().initManageProduct(1);

                    pageFooter.on("click", "li a", function (e) {
                        e.preventDefault();

                        pageNum = $(this).attr("href");

                        initPage().initManageProduct(pageNum);
                    })
                }
            })

        })

        let modal = $(".modal");
        let tbody = $("tbody");
        let formObj = $("form[role='form']");

        let modalModBtn = $("#modalModBtn");
        let modalModDoBtn = $("#modalModDoBtn");
        let modalRegisterBtn = $("#modalRegisterBtn");
        let modalResetBtn = $("#modalResetBtn");
        let modalDropBtn = $("#modalDropBtn");

        let validateCnt = 0;

        // 상품 등록
        let addProductBtn = $("#addProductBtn");
        addProductBtn.click(function () {
            modal.find("input").val("");
            modal.find("select").val([]);
            $(".uploadResult ul").empty();

            modal.modal("show")

            validateCnt = 0;
        });

        $("#hCodeSelected").change(function () {
            let modalHCode = $(`#hCodeSelected option:selected`).val();
            let modalLocation = $("#codeSelected");
            console.log("선택한 상위 카테고리: " + modalHCode);

            productService().getCategoryList(modalHCode, function (categoryCnt, list) {
                console.log("받아온 리스트: " + JSON.stringify(list));
                initPage().initLowCategoryList(list, modalLocation);
            })
        })

        modal.find("input").each(function () {
            $(this).change(function () {
                modal.find("span[class!='fileResultSpan']").empty();

                let name = $(this).attr("name");
                let param = $(this).val();
                console.log(`name: \${name} & param: \${param}`);

                switch (name) {
                    case 'name':
                    case 'eName':
                        let duplicateParam = param;
                        let style, checkInput, checkCase;
                        if (name === 'name') {
                            style = $("#nameCheck");
                            checkInput = modal.find("input[name='nameCheck']");
                            checkCase = "K";
                        } else {
                            style = $("#eNameCheck");
                            checkInput = modal.find("input[name='eNameCheck']");
                            checkCase = "E";
                        }
                        let duplicateInfo = {
                            duplicateParam: duplicateParam,
                            duplicateCase: checkCase
                        }

                        console.log("duplicateInfo: " + JSON.stringify(duplicateInfo));

                        initPage().initCheck(duplicateInfo, style, checkInput)
                        break;
                    case 'image':
                        let formData = new FormData();
                        let inputFile = $(this);
                        let files = inputFile[0].files;

                        for (let i = 0; i < files.length; i++) {
                            if (!validate().checkImage(files[i].name, files[i].size)) {
                                return false;
                            }
                            formData.append("uploadFile", files[i]);
                        }

                        $.ajax({
                            url: '/uploadAjaxAction',
                            processData: false,
                            contentType: false,
                            data: formData,
                            type: 'post',
                            dataType: 'json',
                            success: function (result) {
                                console.log(result);

                                initPage().initUploadResult(result, $(".uploadResult ul"));
                            }
                        });
                        break;
                    case 'price':
                        validate().validatePrice(param);
                        break;
                    case 'discount':
                        validate().validateDiscount(param);
                        break;
                }
            });
        });

        let modalInputKorName = modal.find("input[name='name']");
        let modalInputKorNameCheck = modal.find("input[name='nameCheck']");
        let modalInputEngName = modal.find("input[name='eName']");
        let modalInputEngNameCheck = modal.find("input[name='eNameCheck']");
        let modalInputPrice = modal.find("input[name='price']");
        let modalInputDiscount = modal.find("input[name='discount']");
        let modalInputNation = modal.find("input[name='nation']");
        let modalInputDetail = $("#content");

        $(".uploadResult").on("click", "button", function (e) {
            console.log("delete file");

            let targetFile = $(this).data("file");
            let type = $(this).data("type");
            let targetLi = $(this).closest("li");

            $.ajax({
                url: '/deleteFile',
                data: {fileName: targetFile, type: type, isImage: true},
                dataType: 'text',
                type: 'post',
                success: function (result) {
                    alert(result);
                    targetLi.remove();
                }
            })
        })

        let submit = $(".submit");

        let cloneName;
        let cloneEName;

        submit.each(function () {
            $(this).click(function (e) {
                if (cloneName !== modalInputKorName.val() && !validate().validateName(modalInputKorName.val(), modalInputKorNameCheck.val())) {
                    return;
                }
                if (cloneEName !== modalInputEngName.val() && !validate().validateEName(modalInputEngName.val(), modalInputEngNameCheck.val())) {
                    return;
                }
                let modalInputCode = $("#codeSelect :selected");

                if (!validate().validateCode(modalInputCode.val())) {
                    console.log("카테고리 선택하지 않음");
                    return;
                }

                e.preventDefault();

                let str = "";
                let imageList = [];

                $(".uploadResult ul li").each(function (i, obj) {
                    let jobj = $(obj);
                    console.dir(jobj);

                    console.log(`name: \${jobj.data("name")}, uploadPath: \${jobj.data("path")}, fileType: \${jobj.data("type")}`);

                    str += `<input type='hidden' name='imageList[\${i}].fileName' value=\${jobj.data("name")}>`;
                    str += `<input type='hidden' name='imageList[\${i}].uuid' value=\${jobj.data("uuid")}>`;
                    str += `<input type='hidden' name='imageList[\${i}].uploadPath' value=\${jobj.data("path")}>`;
                    str += `<input type='hidden' name='imageList[\${i}].fileType' value=\${jobj.data("type")}>`;

                    let productImageVO = {
                        fileName: jobj.data("name"),
                        uuid: jobj.data("uuid"),
                        uploadPath: jobj.data("path"),
                        fileType: jobj.data("type")
                    }

                    imageList.push(productImageVO);
                })

                formObj.append(str);

                if ($("#content").length) {
                    oEditors.getById['content'].exec("UPDATE_CONTENTS_FIELD", []);
                }

                let product = {
                    korName: modalInputKorName.val(),
                    engName: modalInputEngName.val(),
                    code: modalInputCode.val(),
                    price: modalInputPrice.val(),
                    discount: modalInputDiscount.val(),
                    nation: modalInputNation.val(),
                    detail: modalInputDetail.val(),
                    imageList: imageList
                }

                console.log("product: " + JSON.stringify(product));

                let id = $(this).attr("id");

                if (id === 'modalRegisterBtn') {
                    productService().add(product, function (result) {
                        alert(result);

                        $("#stateSelectId").val(1).prop("selected", true);

                        modal.find("input").val("");
                        modal.modal("hide");

                        initPage().initManageProduct(1, modalInputCode.val());
                    })
                } else if (id === 'modalModDoBtn') {
                    product.stock = modal.find("input[name='stock']").val();
                    product.state = $("#dropSelect :selected").val();
                    product.pno = modal.find("input[name='code']").val();

                    if (product.detail === '<br>') {
                        product.detail = $("#product-detail").html();
                    }

                    console.log("modify product: " + JSON.stringify(product));

                    productService().modify(product, function (result) {
                        alert(result);
                        modal.modal("hide");
                        initPage().initManageProduct(pageNum)
                    })
                }
            })
        })

        let pno;

        tbody.on("click", "tr", function (e) {
            pno = $(this).data("pno");
            console.log("pno: " + pno);

            productService().get(pno, function (product) {
                initPage().initManageGet(product);
            })
        })

        modalModBtn.click(function () {
            initPage().initAtferModBtn();
            oEditors.getById["content"].exec("SET_IR", [""]);
            modalModBtn.hide();
            modalModDoBtn.show();
            modalResetBtn.show();

            cloneName = modalInputKorName.val();
            cloneEName = modalInputEngName.val();
        });

        modalDropBtn.click(function () {
            if (confirm("해당 상품을 판매 중지 하시겠습니까?")) {
                console.log("drop pno: "+pno);

                productService().drop(pno, function (result) {
                    alert(result);
                    modal.modal("hide");
                    initPage().initManageProduct(pageNum);
                })
            }
        })

    })
</script>