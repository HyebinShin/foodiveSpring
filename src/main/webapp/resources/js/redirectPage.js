let redirectPage = (function () {

    function alreadyLogin(id) {
        if(id !== '' || id !== undefined) {
            location.replace("/main");
        }
    }

    return {
        alreadyLogin:alreadyLogin
    }
})