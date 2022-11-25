let categoryService = (function () {

    function check(duplicateInfo, callback, error) {
        $.ajax({
            type: 'post',
            url: '/category/check',
            data: JSON.stringify(duplicateInfo),
            contentType: "application/json; charset=utf-8",
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    function getList(param, callback, error) {
        console.log(`node: ${JSON.stringify(param)}`)

        let state = param.state
        let page = param.page || 1;
        let hCode = param.hCode || "all";

        $.getJSON(`/category/pages/${hCode}/${state}/${page}`,
            function (data) {
                if (callback) {
                    callback(data.categoryCnt, data.list);
                }
            }).fail(function (xhr, status, err) {
            error();
        })
    }

    function add(category, callback, error) {
        console.log(`category: ${JSON.stringify(category)}`);
        $.ajax({
            type: 'post',
            url: '/category/register',
            data: JSON.stringify(category),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    function get(cno, callback, error) {
        $.getJSON(`/category/${cno}`,
            function (data) {
                if (callback) {
                    callback(data);
                }
            }).fail(function (xhr, status, err) {
            error();
        })
    }

    function displayTime(timeValue) {
        let dateObj = new Date(timeValue);


        let yy = dateObj.getFullYear();
        let mm = dateObj.getMonth() + 1;
        let dd = dateObj.getDate();

        return [yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd].join('');
    }

    return {
        check: check,
        getList: getList,
        displayTime: displayTime,
        add: add,
        get:get
    }
})

let categoryFunction = (function () {

    function regex(regexCase, input) {
        console.log(`regexCase: ${regexCase}, input: ${input}`);
        let regex;

        switch (regexCase) {
            case 'name':
                regex = /^[가-힣 •]{1,20}/g;
                break;
            case 'eName':
                regex = /^[a-zA-Z •]{1,30}/g;
                break;
        }

        return regex.test(input);
    }

    const errorMsg = {
        NAME_NULL: "카테고리 국문명을 입력해주세요.",
        NAME_NOT_VALI: "카테고리 국문명은 20자 이내의 한글과 특수문자(•)만 가능합니다.",
        NAME_NOT_MATCH: "카테고리 국문명 중복검사가 진행되지 않았습니다.",
        ENAME_NULL: "카테고리 영문명을 입력해주세요.",
        ENAME_NOT_VALI: "카테고리 영문명은 30자 이내의 영문과 특수문자(•)만 가능합니다.",
        ENAME_NOT_MATCH: "카테고리 영문명 중복검사가 진행되지 않았습니다.",
        HCODE_NULL: "상위 카테고리를 선택해주세요."
    }

    const inputStyle = (styleCase, msg) => {
        $(`#${styleCase}`).attr("style", `color:red`).html(msg)
    }

    let modal = $(".modal");

    const inputForm = {
        NAME: modal.find("input[name='name']"),
        NAME_CHECK: modal.find("input[name='nameCheck']"),
        ENAME: modal.find("input[name='eName']"),
        ENAME_CHECK: modal.find("input[name='eNameCheck']"),
        HCODE: modal.find("select[name='hCode']")
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
            case "N":
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

    function validateHCode(hCode) {
        if (hCode === '') {
            inputStyle('hCode', errorMsg.HCODE_NULL);
            addInputError(inputForm.HCODE);
            return false;
        }
        addInputSuccess(inputForm.HCODE);
        return true;
    }

    return {
        checkValidate: checkValidate,
        validateName: validateName,
        validateEName: validateEName,
        validateHCode: validateHCode
    }
})