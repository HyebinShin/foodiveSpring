document.write('<script src="/resources/js/productService.js"></script>');

const initPage = (function () {

    let modal = $(".modal");
    let tbody = $("tbody");
    let pageFooter = $(".panel-footer");

    function hideClosestDiv(input) {
        modal.find(`input[name=${input}]`).closest("div").hide();
    }

    function hideButton(id) {
        modal.find(`button[id!=${id}]`).hide();
    }

    function showButton(id) {
        modal.find(`#${id}`).show();
    }

    function removeReadOnlyAll() {
        removeReadOnly("name");
        removeReadOnly("eName");
        removeReadOnly("price");
        removeReadOnly("discount");
        removeReadOnly("image");
        removeReadOnly("nation");
        removeReadOnly("stock");
        modal.find("select").removeAttr("readOnly");
        modal.find("textarea").removeAttr("readOnly");
    }

    function removeReadOnly(name) {
        modal.find(`input[name=${name}]`).removeAttr("readOnly");
    }

    function attrReadOnly() {
        modal.find("input").attr("readOnly", "readOnly");
        modal.find("select").attr("readOnly", "readOnly");
        modal.find("textarea").attr("readOnly", "readOnly");
    }

    function displayTime(timeValue) {
        let dateObj = new Date(timeValue);

        let yy = dateObj.getFullYear();
        let mm = dateObj.getMonth() + 1;
        let dd = dateObj.getDate();

        return [yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd].join('');
    }

    function initLowCategoryList(list, initLocation) {
        console.log("init List: "+JSON.stringify(list));

        if (list == null || list.length === 0) {
            return;
        }

        initLocation.empty();
        initLocation.append(`<option value=${list[0].hCode}>전체</option>`);
        for (let i=0, len=list.length||0; i<len; i++) {
            initLocation.append(`<option value=${list[i].code}>${list[i].name}</option>`);
        }
    }

    function reset() {
        modal.find("span").empty();
        modal.find("div").removeClass("has-success");
        modal.find("div").removeClass("has-error");
    }

    function initRegisterPage() {
        hideClosestDiv("code");
        hideClosestDiv("stock");
        modal.find("#dropSelect").hide();
        hideClosestDiv("regDate");
        hideClosestDiv("modDate");
        hideClosestDiv("dropDate");

        hideButton("modalCloseBtn");
        showButton("modalRegisterBtn");
        showButton("modalResetBtn");
        showButton("imageSubmitBtn");

        removeReadOnlyAll();
        reset();
    }

    function initPagination(productCnt, pageNum) {
        let endNum = Math.ceil(pageNum/10.0) * 10;
        let startNum = endNum - 9;

        let prev = startNum !== 1;
        let next = false;

        if (endNum * 10 >= productCnt) {
            endNum = Math.ceil(productCnt/10.0);
        } else {
            next = true;
        }

        let str = `<ul class='pagination pull-right'>`;

        if (prev) {
            str += `<li class='page-item'><a class='page-link' href='${startNum-1}'>Previous</a></li>`;
        }

        for (let i=startNum; i<=endNum; i++) {
            let active = pageNum == i ? 'active' : '';
            str += `<li class='page-item ${active}'><a class='page-link' href='${i}'>${i}</a></li>`;
        }

        if (next) {
            str += `<li class='page-item'><a class='page-link' href='${endNum+1}'>Previous</a></li>`;
        }

        str += '</ul>';

        pageFooter.html(str);
    }

    function initManageProduct(page) {
        tbody.empty();

        let state = $("#stateSelect :selected").val();
        let keyword = $("input[name='keyword']").val();
        let category = $("#lowCategorySelect :selected").val();

        let param = {
            state:state,
            page:page,
            code:category,
            keyword:keyword
        };

        productService().getList(param, function (productCnt, list) {
            console.log(`productCnt: ${productCnt}`);
            console.log(`list: ${list}`);

            let pageNum = 1;

            if (page === -1) {
                pageNum = Math.ceil(productCnt/10.0);
                initManageProduct(pageNum);
                return;
            }

            let str = "";

            if (list == null || list.length === 0) {
                return;
            }

            for (let i=0, len=list.length||0; i<len; i++) {
                str += `<tr data-pno=${list[i].pno}/>'><td>${list[i].pno}</td>`;
                str += `<td>${list[i].korName}</td>`;
                str += `<td>${list[i].code}</td>`;
                str += `<td>${displayTime(list[i].regDate)}</td>`;
                str += `<td>${list[i].stock}</td></tr>`;
            }

            tbody.html(str);
            initPagination(productCnt, page);
        })
    }

    function initUploadResult(uploadResultArr) {
        if (!uploadResultArr || uploadResultArr.length===0) {
            return;
        }

        console.log(`upload result arr: `+JSON.stringify(uploadResultArr));

        let uploadUL = $(".uploadResult ul");
        let str = "";

        $(uploadResultArr).each(function (i, obj) {
            let fileCallPath = "";

            // image type
            if (obj.image) {
                fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
                let fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");

                console.log(fileLink);

                str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' " +
                    "data-name='"+obj.fileName+"' data-type='"+obj.image+"'><div>";
                str += "<span>"+obj.fileName+"</span>";
                str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                str += "<img src='/display?isImage=true&fileName="+fileCallPath+"'>";
                str += "</div></li>";
            } else {
                fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
                let fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");

                str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' " +
                    "data-name='"+obj.fileName+"' data-type='"+obj.image+"'><div>";
                str += "<span>"+obj.fileName+"</span>";
                str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                str += "<img src='/resources/foodive/attach.png'></a>";
                str += "</div></li>";
            }
        });

        uploadUL.append(str);
    }

    function initCheck(duplicateInfo, style, checkInput) {
        let duplicateParam = duplicateInfo.duplicateParam;
        let duplicateCase = duplicateInfo.duplicateCase;

        if (!validate().checkValidate(duplicateParam, duplicateCase)) {
            console.log("invalidate");
            return;
        }
        console.log("validate");

        productService().check(duplicateInfo, function (result) {
            console.log("result: "+result);

            let color, divClass;

            if (result.indexOf("가능")!==-1) {
                color = "color:#337ab7";
                divClass = "has-success";
                checkInput.val(duplicateParam);
            } else {
                color = "color:red";
                divClass = "has-error";
            }
            style.attr("style", color).html(result);
            style.closest("div").attr("class", divClass);
        })
    }

    return {
        initLowCategoryList:initLowCategoryList,
        initRegisterPage:initRegisterPage,
        initManageProduct:initManageProduct,
        initUploadResult:initUploadResult,
        initCheck:initCheck
    }
});

const validate = (function () {

    let modal = $(".modal");

    let imageRegex = new RegExp("(.*?)\.(jpg|png)$");
    let maxSize = 5242880;

    function checkImage(fileName, fileSize) {
        if (fileSize >= maxSize) {
            inputStyle("imageCheck", errorMsg.FILE_SIZE);
            addInputError("image");
            return false;
        }
        if (!imageRegex.test(fileName)) {
            inputStyle("imageCheck", errorMsg.FILE_TYPE);
            addInputError("image");
            return false;
        }
        addInputSuccess("image");
        return true;
    }

    function regex(regexCase, input) {
        let regex;

        switch (regexCase) {
            case 'name': case 'nation':
                regex = /^[가-힣•]{1,20}/g;
                break;
            case 'eName':
                regex = /^[a-zA-Z•]{1,30}/g;
                break;
            case 'price': case 'stock': case 'discount':
                regex = /^\d/g;
                break;
        }

        return regex.test(input);
    }

    const errorMsg = {
        NAME_NULL: "상품 국문명을 입력해주세요.",
        NAME_NOT_VALI: "상품 국문명은 20자 이내의 한글과 특수문자(•)만 가능합니다.",
        NAME_NOT_MATCH: "상품 국문명 중복검사가 진행되지 않았습니다.",
        ENAME_NULL: "상품 영문명을 입력해주세요.",
        ENAME_NOT_VALI: "상품 영문명은 30자 이내의 영문과 특수문자(•)만 가능합니다.",
        ENAME_NOT_MATCH: "상품 영문명 중복검사가 진행되지 않았습니다.",
        CODE_NULL: "카테고리를 선택해주세요.",
        PRICE_NULL: "가격을 입력해주세요.",
        PRICE_NOT_VALI: "가격은 0 이상의 숫자만 입력해주세요.",
        DISCOUNT_NOT_VALI: "할인율은 0부터 100까지만 입력 가능합니다.",
        STOCK_NOT_VALI: "재고는 0 이상의 숫자만 입력해주세요.",
        FILE_SIZE: "파일 사이즈 초과! 5MB 이하의 파일만 업로드 가능합니다.",
        FILE_TYPE: "이미지는 JPG, PNG 타입만 업로드 가능합니다."
    }

    const inputStyle = (styleCase, msg) => {
        $(`#${styleCase}`).attr("style", `color:red`).html(msg)
    }

    const inputForm = {
        NAME: modal.find("input[name='name']"),
        NAME_CHECK: modal.find("input[name='nameCheck']"),
        ENAME: modal.find("input[name='eName']"),
        ENAME_CHECK: modal.find("input[name='eNameCheck']"),
        CODE: modal.find("select[name='code']"),
        PRICE: modal.find("input[name='price']"),
        DISCOUNT: modal.find("input[name='discount']"),
        IMAGE: modal.find("input[name='image']"),
        NATION: modal.find("input[name='nation']"),
        STOCK: modal.find("input[name='stock']"),
        STATE: modal.find("input[name='state']")
    }

    function addInputError(input) {
        $(input).closest("div").attr("class", "has-error");
    }

    function addInputSuccess(input) {
        $(input).closest("div").attr("class", "has-success");
    }

    function checkValidate(checkParam, checkCase) {
        let inputStyleCase;
        let input;
        switch (checkCase) {
            case "K":
                inputStyleCase = 'nameCheck';
                input = inputForm.NAME;
                if (checkParam === '') {
                    inputStyle(inputStyleCase, errorMsg.NAME_NULL);
                    addInputError(input);
                    return false;
                } else if (!regex('name', checkParam)) {
                    inputStyle(inputStyleCase, errorMsg.NAME_NOT_VALI);
                    addInputError(input);
                    return false;
                }
                addInputSuccess(input);
                return true;
            case "E":
                inputStyleCase = 'eNameCheck';
                input = inputForm.ENAME;
                if (checkParam === '') {
                    inputStyle(inputStyleCase, errorMsg.ENAME_NULL);
                    addInputError(input);
                    return false;
                } else if (!regex('eName', checkParam)) {
                    inputStyle(inputStyleCase, errorMsg.ENAME_NOT_VALI);
                    addInputError(input);
                    return false;
                }
                addInputSuccess(input);
                return true;
        }
    }

    function validateName(name, nameCheck) {
        if (name !== nameCheck) {
            inputStyle('nameCheck', errorMsg.NAME_NOT_MATCH);
            addInputError(inputForm.NAME);
            return false;
        }
        addInputSuccess(inputForm.NAME);
        return true;
    }

    function validateEName(eName, eNameCheck) {
        if (eName !== eNameCheck) {
            inputStyle('eNameCheck', errorMsg.ENAME_NOT_MATCH);
            addInputError(inputForm.ENAME);
            return false;
        }
        addInputSuccess(inputForm.ENAME);
        return true;
    }

    function validateCode(code) {
        if (code === '') {
            inputStyle('code', errorMsg.CODE_NULL);
            addInputError(inputForm.CODE);
            return false;
        }
        addInputSuccess(inputForm.CODE);
        return true;
    }

    function validatePrice(price) {
        let inputPrice = inputForm.PRICE;
        if (price === '') {
            inputStyle('priceCheck', errorMsg.PRICE_NULL);
            addInputError(inputPrice);
            return false;
        } else if (!regex('price', price) || (Number(price) < 0)) {
            inputStyle('priceCheck', errorMsg.PRICE_NOT_VALI);
            addInputError(inputPrice);
            return false;
        }
        addInputSuccess(inputPrice);
        return true;
    }

    function validateDiscount(discount) {
        console.log("discount: "+discount);
        let inputDiscount = inputForm.DISCOUNT;
        if (discount === '') {

        } else if (!regex('discount', discount) || (Number(discount) < 0 || Number(discount) > 100)) {
            inputStyle('discountCheck', errorMsg.DISCOUNT_NOT_VALI);
            addInputError(inputDiscount);
            return false;
        }
        addInputSuccess(inputDiscount);
        return true;
    }

    return {
        checkValidate: checkValidate,
        validateName: validateName,
        validateEName: validateEName,
        validateCode: validateCode,
        checkImage:checkImage,
        validatePrice:validatePrice,
        validateDiscount:validateDiscount
    }
})