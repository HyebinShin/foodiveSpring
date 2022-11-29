const productService = (function () {

    function getCategoryList(hCode, callback, error) {
        let state = 1;

        $.getJSON(`/product/pages/${hCode}/${state}`,
            function (data) {
                if (callback) {
                    callback(data.categoryCnt, data.list);
                }
            }).fail(function (xhr, status, err) {
            error();
        })
    }


    return {
        getCategoryList:getCategoryList
    }
});