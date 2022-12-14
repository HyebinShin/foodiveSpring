let userService = (function () {

    function check(duplicateInfo, callback, error) {
        console.log("check...");

        $.ajax({
            type: 'post',
            url: '/user/check',
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
        });
    }

    function add(user, callback, error) {
        $.ajax({
            type:'post',
            url:'/manageUser/register',
            data: JSON.stringify(user),
            contentType: "application/json; charset=utf-8",
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error (er);
                }
            }
        })
    }

    function getList(param, callback, error) {
        let state = param.state;
        let page = param.page || 1;

        $.getJSON(`/manageUser/pages/${state}/${page}`,
            function (data) {
                if (callback) {
                    callback(data.userCnt, data.list);
                }
            }).fail(function (xhr, status, err) {
            if (error) {
                error();
            }
        })
    }

    function displayTime(timeValue) {
        let today = new Date();
        let gap = today.getTime() - timeValue;
        let dateObj = new Date(timeValue);
        let str = "";


        let yy = dateObj.getFullYear();
        let mm = dateObj.getMonth() + 1;
        let dd = dateObj.getDate();

        let hh = dateObj.getHours();
        let mi = dateObj.getMinutes();
        let ss = dateObj.getSeconds();

        return [yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd + ' ' +
        (hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi, ':', (ss > 9 ? '' : '0') + ss].join('');

    }

    function getUserInfo(user, callback, error) {
        console.log("get user info..."+JSON.stringify(user));

        $.ajax({
            type: 'post',
            url: '/user/find',
            data: JSON.stringify(user),
            contentType: "application/json; charset=utf-8",
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            fail: function (xhr, status, er) {
                if(error) {
                    error(er);
                }
            }
        });
    }

    function update(node, callback, error) {
        console.log("update...");

        $.ajax({
            type:'put',
            url:`/user/${node.user.id}`,
            data:JSON.stringify(node),
            contentType:"application/json; charset=utf-8",
            success: function (result, status, xhr) {
                if(callback) {
                    callback(result);
                }
            },
            fail: function (xhr, status, er) {
                if(error) {
                    error(er);
                }
            }
        });
    }

    return {
        update:update,
        getUserInfo:getUserInfo,
        add:add,
        check:check,
        getList: getList,
        displayTime: displayTime
    }
});

let userFunction = (function () {

    function regex(regexCase, input) {
        console.log("regex...");

        let regex;

        switch (regexCase) {
            case "id":
                regex = /^[a-zA-Z0-9]{6,12}/g;
                break;
            case "name":
                regex = /^[???-???]{2,5}/g;
                break;
            case "password":
                regex = /^[a-zA-Z0-9]{8,24}/g;
                break;
            case "phone":
                regex = /^01[0|1]-\d{3,4}-\d{4}$/;
                break;
            case "email":
                regex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
                break;
        }

        return regex.test(input);
    }

    const errorMsg = {
        ID_NULL: "???????????? ????????? ?????????",
        ID_NOT_VALI: "???????????? 6 ~ 12?????? ???????????? ????????? ??????????????????.",
        ID_NOT_MATCH: "????????? ?????? ????????? ???????????? ???????????????.",
        PASSWORD_NULL: "??????????????? ??????????????????.",
        PASSWORD_NOT_VALI: "??????????????? 8 ~ 24?????? ???????????? ????????? ??????????????????.",
        PASSWORD_NOT_MATCH: "???????????? ???????????? ???????????? ??????????????? ????????????.",
        NAME_NULL: "????????? ??????????????????.",
        NAME_NOT_VALI: "????????? ????????? 2 ~ 5?????? ?????? ???????????????.",
        EMAIL_NULL: "???????????? ??????????????????.",
        EMAIL_NOT_VALI: "????????? ???????????? ??????????????????.",
        EMAIL_NOT_MATCH: "????????? ?????? ????????? ???????????? ???????????????.",
        PHONE_NOT_VALI: "??????????????? ????????? ?????? ??????????????????."
    }

    const inputStyle = (styleCase, msg) => {
        $(`#${styleCase}`).attr("style", `color:red`).html(msg)
    }

    const inputForm = {
        ID: $("input[name='id']"),
        ID_CHECK: $("input[name='idCheck']"),
        PASSWORD: $("input[name='password']"),
        PASSWORD_CHECK: $("input[name='passwordCheck']"),
        NAME: $("input[name='name']"),
        EMAIL: $("input[name='email']"),
        EMAIL_CHECK: $("input[name='emailCheck']"),
        PHONE: $("input[name='phone']")
    }

    function addInputError(input) {
        $(input).closest("div").attr("class", "has-error");
    }

    function addInputSuccess(input) {
        $(input).closest("div").attr("class", "has-success");
    }

    function checkValidate(checkParam, checkCase) {
        switch (checkCase) {
            case "I":
                if (checkParam === '') {
                    inputStyle('idCheck', errorMsg.ID_NULL);
                    addInputError(inputForm.ID);
                    return false;
                } else if (!regex('id', checkParam)) {
                    inputStyle('idCheck', errorMsg.ID_NOT_VALI);
                    addInputError(inputForm.ID);
                    return false;
                }
                addInputSuccess(inputForm.ID);
                return true;
            case "E":
                if (checkParam === '') {
                    inputStyle('emailCheck', errorMsg.EMAIL_NULL);
                    addInputError(inputForm.EMAIL);
                    return false;
                } else if (!regex('email', checkParam)) {
                    inputStyle('emailCheck', errorMsg.EMAIL_NOT_VALI);
                    addInputError(inputForm.EMAIL);
                    return false;
                }
                addInputSuccess(inputForm.EMAIL);
                return true;
        }
    }

    function validatePassword(password) {
        if (password === '') {
            inputStyle('password', errorMsg.PASSWORD_NULL);
            addInputError(inputForm.PASSWORD);
            return false;
        } else if (!regex('password', password)) {
            inputStyle('password', errorMsg.PASSWORD_NOT_VALI);
            addInputError(inputForm.PASSWORD);
            return false;
        }
        addInputSuccess(inputForm.PASSWORD);
        return true;
    }

    function validatePasswordCheck(password, passwordCheck) {
        if (password !== passwordCheck) {
            inputStyle('password', errorMsg.PASSWORD_NOT_MATCH);
            addInputError(inputForm.PASSWORD_CHECK);
            return false;
        }
        addInputSuccess(inputForm.PASSWORD_CHECK);
        return true;
    }

    function validateEmail(email, emailCheck) {
        if (email !== emailCheck) {
            inputStyle('emailCheck', errorMsg.EMAIL_NOT_MATCH);
            addInputError(inputForm.EMAIL);
            return false;
        }
        addInputSuccess(inputForm.EMAIL);
        return true;
    }

    function validatePhone(phone) {
        if (phone !== '' && !regex('phone', phone)) {
            inputStyle('phone', errorMsg.PHONE_NOT_VALI);
            addInputError(inputForm.PHONE);
            return false;
        }
        addInputSuccess(inputForm.PHONE);
        return true;
    }

    function validateName(name) {
        if (name === '') {
            inputStyle('name', errorMsg.NAME_NULL);
            addInputError(inputForm.NAME);
            return false;
        } else if (!regex('name', name)) {
            inputStyle('name', errorMsg.NAME_NOT_VALI);
            addInputError(inputForm.NAME);
            return false;
        }
        addInputSuccess(inputForm.NAME);
        return true;
    }


    return {
        checkValidate: checkValidate,
        validatePassword:validatePassword,
        validateEmail:validateEmail,
        validatePhone:validatePhone,
        validateName:validateName,
        validatePasswordCheck:validatePasswordCheck
    };
})

