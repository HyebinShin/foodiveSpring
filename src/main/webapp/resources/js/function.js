let fnc = (function () {
    function displayTime(timeValue) {
        let dateObj = new Date(timeValue);

        let yy = dateObj.getFullYear();
        let mm = dateObj.getMonth() + 1;
        let dd = dateObj.getDate();

        let hh = dateObj.getHours();
        let mi = dateObj.getMinutes();
        let ss = dateObj.getSeconds();

        return [yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd + ' ' +
        (hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi, ':', (ss > 9 ? '' : '0') + ss].join('');
    }

    function initPagination(cnt, pageNum, location) {
        location.empty();

        let endNum = Math.ceil(pageNum/10.0) * 10;
        let startNum = endNum - 9;

        let prev = startNum !== 1;
        let next = false;

        if (endNum * 10 >= cnt) {
            endNum = Math.ceil(cnt/10.0);
        } else {
            next = true;
        }

        let str = `<ul class='pagination'>`;

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

        location.html(str);
    }

    // select 생성자
    function constructorSelect(name, value) {
        this.name = name;
        this.value = value;
    }

    // input 생성자
    function constructorInput(label, name, data) {
        this.label = label;
        this.name = name;
        this.data = data;
    }

    return {
        displayTime:displayTime,
        initPagination:initPagination,
        constructorSelect:constructorSelect,
        constructorInput:constructorInput
    }
})();