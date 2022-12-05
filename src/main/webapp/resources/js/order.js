const orderController = (function () {
    function setOrderDetail(detailList) {
        orderService().setOrderDetail(detailList, function (result) {
            location.href=result;
        })
    }

    return {
        setOrderDetail:setOrderDetail
    }
})

const orderService = (function () {

    function setOrderDetail(detailList, callback, error) {
        $.ajax({
            type:'post',
            url: '/order/setOrderDetail',
            data: JSON.stringify(detailList),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if(callback) {
                    callback (result);
                }
            },
            error: function (xhr, status, er) {
                if (error) {
                    error(er);
                }
            }
        })
    }

    return {
        setOrderDetail:setOrderDetail
    }

})

const orderInit = (function () {

})