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

    function login(user, callback, error) {
        console.log("login...");

        $.ajax({
            type: 'post',
            url: '/user/login',
            data: JSON.stringify(user),
            contentType: "application/json; charset=utf-8",
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            fail: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    return {
        check: check,
        login: login
    };

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
                regex = /^[가-힣]{2,5}/g;
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
            case "birthday":
                // regex = /^((19[5-9][0-9])|(200[0-8]))(0[1-9]|1[1-2])([0-2][1-9]|3[0-1])/;
                regex = /^\d{8}$/g;
                break;
        }

        return regex.test(input);
    }

    const errorMsg = {
        ID_NULL: "아이디를 입력해 주세요",
        ID_NOT_VALI: "아이디는 6 ~ 12자의 영문자와 숫자로 입력해주세요.",
        ID_NOT_MATCH: "아이디 중복 검사가 진행되지 않았습니다.",
        PASSWORD_NULL: "비밀번호를 입력해주세요.",
        PASSWORD_NOT_VALI: "비밀번호는 8 ~ 24자의 영문자와 숫자로 입력해주세요.",
        PASSWORD_NOT_MATCH: "비밀번호 확인란이 입력하신 비밀번호와 다릅니다.",
        NAME_NULL: "이름을 입력해주세요.",
        NAME_NOT_VALI: "이름은 한글로 2 ~ 5자만 입력 가능합니다.",
        EMAIL_NULL: "이메일을 입력해주세요.",
        EMAIL_NOT_VALI: "이메일 양식대로 입력해주세요.",
        EMAIL_NOT_MATCH: "이메일 중복 검사가 진행되지 않았습니다.",
        BIRTHDAY_NOT_VALI: "숫자 8자리의 생년월일을 입력해주세요.",
        BIRTHDAY_UNDER_14: "만 14세 미만은 가입하실 수 없습니다.",
        PHONE_NOT_VALI: "전화번호를 양식에 맞춰 입력해주세요."
    }

    const inputStyle = (styleCase, msg) => {
        $(`#${styleCase}`).attr("style", `color:red`).html(msg)
    }

    const inputForm = {
        ID: $("input[name='id']").val(),
        ID_CHECK: $("input[name='idCheck']").val(),
        PASSWORD: $("input[name='password']").val(),
        PASSWORD_CHECK: $("input[name='passwordCheck']").val(),
        NAME: $("input[name='name']").val(),
        EMAIL: $("input[name='email']").val(),
        EMAIL_CHECK: $("input[name='emailCheck']").val(),
        BIRTHDAY: $("input[name='birthday']").val(),
        PHONE: $("input[name='phone']").val()
    }

    function checkValidate(checkParam, checkCase) {
        switch (checkCase) {
            case "I":
                if (inputForm.ID === '') {
                    inputStyle('idCheck', errorMsg.ID_NULL);
                    return false;
                } else if (!regex('id', inputForm.ID)) {
                    inputStyle('idCheck', errorMsg.ID_NOT_VALI);
                    return false;
                }
                return true;
            case "E":
                if (inputForm.EMAIL === '') {
                    inputStyle('emailCheck', errorMsg.EMAIL_NULL);
                    return false;
                } else if (!regex('email', inputForm.EMAIL)) {
                    inputStyle('emailCheck', errorMsg.EMAIL_NOT_VALI);
                    return false;
                }
                return true;
        }
    }

    function validate() {

        if (inputForm.ID !== inputForm.ID_CHECK) {
            inputStyle('idCheck', errorMsg.ID_NOT_MATCH);
            return false;
        }

        if (inputForm.PASSWORD === '') {
            inputStyle('password', errorMsg.PASSWORD_NULL);
            return false;
        } else if (!regex('password', inputForm.PASSWORD)) {
            inputStyle('password', errorMsg.PASSWORD_NOT_VALI);
            return false;
        } else if (inputForm.PASSWORD !== inputForm.PASSWORD_CHECK) {
            inputStyle('password', errorMsg.PASSWORD_NOT_MATCH);
            return false;
        }

        if (inputForm.NAME === '') {
            inputStyle('name', errorMsg.NAME_NULL);
            return false;
        } else if (!regex('name', inputForm.NAME)) {
            inputStyle('name', errorMsg.NAME_NOT_VALI);
            return false;
        }

        if (inputForm.EMAIL !== inputForm.EMAIL_CHECK) {
            inputStyle('emailCheck', errorMsg.EMAIL_NOT_MATCH);
            return false;
        }

        if (inputForm.BIRTHDAY !== '') {
            let birthday = inputForm.BIRTHDAY;

            console.log("instance of ..." + (Number(birthday.substring(0, 4)) instanceof Number));

            if (!regex('birthday', birthday)) {
                inputStyle('birthday', errorMsg.BIRTHDAY_NOT_VALI);
                return false;
            } else if (Number(birthday.substring(0, 4)) > 2008) {
                inputStyle('birthday', errorMsg.BIRTHDAY_UNDER_14);
                return false;
            }
        }

        if (inputForm.PHONE !== '' && !regex('phone', inputForm.PHONE)) {
            inputStyle('phone', errorMsg.PHONE_NOT_VALI);
            return false;
        }

        return true;
    }

    return {
        checkValidate: checkValidate,
        validate: validate
    };

});