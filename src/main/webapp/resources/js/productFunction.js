const initPage = (function () {

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

    return {
        initLowCategoryList:initLowCategoryList
    }
});