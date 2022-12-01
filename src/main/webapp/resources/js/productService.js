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

    function getList(param, callback, error) {
        let state = param.state;
        let page = param.page || 1;
        let code = param.code;
        let keyword = param.keyword;

        console.log(`state: ${state}, page: ${page}, code: ${code}, keyword: ${keyword}`);

        let url = (code === '' || code === undefined) ?
            `/product/list/${state}/${page}?keyword=${keyword}` : `/product/list/${state}/${page}?code=${code}`;


        $.getJSON(url,
            function (data) {
                if (callback) {
                    callback(data.productCnt, data.list);
                }
            }).fail(function (xhr, status, err) {
            error();
        })
    }

    function check(duplicateInfo, callback, error) {
        $.ajax({
            type: 'post',
            url: '/product/check',
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

    function add(product, callback, error) {
        console.log("product: " + JSON.stringify(product))

        $.ajax({
            type: 'post',
            url: '/product/register',
            data: JSON.stringify(product),
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

    function get(pno, callback, error) {
        $.getJSON(`/product/admin/${pno}`,
            function (data) {
                if (callback) {
                    callback(data);
                }
            }).fail(function (xhr, status, err) {
            error();
        })
    }


    return {
        getCategoryList: getCategoryList,
        getList: getList,
        check: check,
        add: add,
        get:get
    }
});