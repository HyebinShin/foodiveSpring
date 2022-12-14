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
        check: check,
        login: login,
        getUserInfo:getUserInfo,
        update:update
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
            case "birthday":
                // regex = /^((19[5-9][0-9])|(200[0-8]))(0[1-9]|1[1-2])([0-2][1-9]|3[0-1])/;
                regex = /^\d{8}$/g;
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
        BIRTHDAY_NOT_VALI: "?????? 8????????? ??????????????? ??????????????????.",
        BIRTHDAY_UNDER_14: "??? 14??? ????????? ???????????? ??? ????????????.",
        PHONE_NOT_VALI: "??????????????? ????????? ?????? ??????????????????."
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
                if (checkParam === '') {
                    inputStyle('idCheck', errorMsg.ID_NULL);
                    return false;
                } else if (!regex('id', checkParam)) {
                    inputStyle('idCheck', errorMsg.ID_NOT_VALI);
                    return false;
                }
                return true;
            case "E":
                if (checkParam === '') {
                    inputStyle('emailCheck', errorMsg.EMAIL_NULL);
                    return false;
                } else if (!regex('email', checkParam)) {
                    inputStyle('emailCheck', errorMsg.EMAIL_NOT_VALI);
                    return false;
                }
                return true;
        }
    }

    function validatePassword(password, passwordCheck) {
        if (password === '') {
            inputStyle('password', errorMsg.PASSWORD_NULL);
            return false;
        } else if (!regex('password', password)) {
            inputStyle('password', errorMsg.PASSWORD_NOT_VALI);
            return false;
        } else if (password !== passwordCheck) {
            inputStyle('password', errorMsg.PASSWORD_NOT_MATCH);
            return false;
        }
        return true;
    }

    function validateEmail(email, emailCheck) {
        if (email !== emailCheck) {
            inputStyle('emailCheck', errorMsg.EMAIL_NOT_MATCH);
            return false;
        }
        return true;
    }

    function validatePhone(phone) {
        if (phone !== '' && !regex('phone', phone)) {
            inputStyle('phone', errorMsg.PHONE_NOT_VALI);
            return false;
        }
        return true;
    }

    function validate() {

        if (inputForm.ID !== inputForm.ID_CHECK) {
            inputStyle('idCheck', errorMsg.ID_NOT_MATCH);
            return false;
        }

        if(!validatePassword(inputForm.PASSWORD, inputForm.PASSWORD_CHECK)) {
            console.log("invalidate password");
            return false;
        }

        if (inputForm.NAME === '') {
            inputStyle('name', errorMsg.NAME_NULL);
            return false;
        } else if (!regex('name', inputForm.NAME)) {
            inputStyle('name', errorMsg.NAME_NOT_VALI);
            return false;
        }

        if(!validateEmail(inputForm.EMAIL, inputForm.EMAIL_CHECK)) {
            console.log("invalidate email")
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

        if(!validatePhone(inputForm.PHONE)) {
            console.log("invalidate phone");
            return false;
        }

        return true;

    }

    return {
        checkValidate: checkValidate,
        validate: validate,
        validatePassword:validatePassword,
        validateEmail:validateEmail,
        validatePhone:validatePhone
    };

});

let init = (function () {
   function id() {
       let str = "";

       str += '<div class="form-group"><label>?????????</label>';
       str += '<input class="form-control" name="id" placeholder="???????????? ???????????? ??????????????????.">';
       str += '<span id="modalIdStyle"></span>';
       str += '</div>';

       return str;
   }

   function email() {
       let str = "";

       str += '<div class="form-group"><label>?????????</label>';
       str += '<input class="form-control" name="email" placeholder="???????????? ???????????? ??????????????????.">';
       str += '<span id="modalEmailStyle"></span>';
       str += '</div>';

       return str;
   }

   function password() {
       let str = "";

       str += '<div class="form-group"><label>????????????</label>';
       str += '<input class="form-control" type="password" name="password" placeholder="8 ~ 24?????? ???????????? ????????? ??????????????????.">';
       str += '<span id="password"></span>';
       str += '</div>';
       str += '<div class="form-group"><label>???????????? ??????</label>';
       str += '<input class="form-control" type="password" name="passwordCheck" placeholder="??????????????? ??? ??? ??? ????????? ?????????.">';
       str += '</div>';

       return str;
   }

   return {
       id:id,
       password:password,
       email:email
   }
});