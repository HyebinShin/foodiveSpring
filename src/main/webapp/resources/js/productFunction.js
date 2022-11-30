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

    return {
        initLowCategoryList:initLowCategoryList,
        initRegisterPage:initRegisterPage,
        initManageProduct:initManageProduct
    }
});