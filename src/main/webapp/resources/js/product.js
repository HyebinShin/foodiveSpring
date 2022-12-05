const productController = (function () {
    function getList(page, keyword, category) {
        let param = {
            state:1,
            page:page,
            code:category,
            keyword:keyword
        };

        console.log("param: "+JSON.stringify(param));

        productService().getList(param, function (productCnt, list) {
            if (page === -1) {
                let pageNum = Math.ceil(productCnt / 10.0);
                getList(pageNum, keyword, category);
                return;
            }

            init().initList(productCnt, list, keyword);
            init().initListFooter(page, productCnt);
        })
    }

    function get(pno) {
        productService().get(pno, function (product) {
            init().initGet(product);
        })
    }

    return {
        getList:getList,
        get:get
    }
})

const init = (function () {
    let pageHeader = $(".page-header");
    let productList = $(".product-list");
    let pageFooter = $(".page-footer");

    // 상품 상세
    let productDetail = $(".product-detail");

    function initListFooter(pageNum, productCnt) {
        pageFooter.empty();

        let buttonHtml = "";

        buttonHtml += `<button type='button' class='btn btn-default center-block' id='more' data-page=${Number(pageNum)+1}>`;
        buttonHtml += `${pageNum} / ${Math.ceil(productCnt/10.0)}</button>`;

        pageFooter.append(buttonHtml);
    }

    function initList(productCnt, list, keyword) {
        pageHeader.empty();

        if (list==null || list.length === 0) {
            return;
        }

        console.log("list: "+JSON.stringify(list));

        let html = "";

        if(keyword==='null') {
            pageHeader.append(`${list[0].name}`);
        } else {
            pageHeader.append(keyword);
        }

        for (let i=0, len=list.length||0; i<len; i++) {
            html += `<div class='col-lg-5 product-one' data-pno=${list[i].pno}><div class="panel panel-info">`;
            html += `<div class='panel-heading'>${list[i].korName}</div>`;
            html += `<div class='panel-body product-one-info'>`;
            html += `${initThumbnail(list[i].imageList)}`;
            html += `${initMiniInfo(list[i])}`;
            html += `</div>`;
            html += `<div class='panel-footer'>`;
            html += `${initMiniButton(list[i])}`;
            html += `</div>`;
            html += `</div></div>`;
        }

        console.log("productCnt: "+productCnt);
        console.log("math ceil productCnt: "+Math.ceil(productCnt/10.0));

        productList.append(html);

        $(".panel-body img").css("width", "100px").css("height", "100px");
    }

    function initThumbnail(imageList) {
        if (!imageList || imageList.length === 0) {
            return `<div class="col-lg-5"><img class="img-thumbnail" src='/resources/foodive/attach.png' alt='상품이미지'></div>`;
        }

        let firstImage = imageList[0];

        let fileCallPath = encodeURIComponent(
            firstImage.uploadPath + "/s_" + firstImage.uuid + "_" + firstImage.fileName
        );

        return `<div class="col-lg-5"><img class="img-thumbnail" src='/display?isImage=true&fileName=${fileCallPath}' alt='상품이미지'></div>`;
    }

    function initMiniInfo(product) {
        let html = "";

        html += `<div class='mini-info col-lg-7'>`;

        html += `${initPrice(product)}`;

        html += `</div>`;

        return html;
    }

    function initPrice(product) {
        let html = "";

        let price = product.price;
        let discount = product.discount;
        let realPrice = discount!=0 ? price * (100-discount)/100 : price;

        if (discount==0) {
            html += `<div class="real-price">가격: ${price.toLocaleString(undefined, {maximumFractionDigits:0})}</div>`;
        } else {
            html += `<div class="price">정상가: ${price.toLocaleString(undefined, {maximumFractionDigits:0})}</div>`;
            html += `<div class="discount">${discount} % 할인</div>`;
            html += `<div class="real-price">할인가: ${realPrice.toLocaleString(undefined, {maximumFractionDigits:0})}</div>`;
        }

        return html;
    }

    function initMiniButton(product) {
        let html = "";

        html += `<div class='product-btn' data-kor=${product.korName} data-stock=${product.stock} data-price=${product.price} data-discount=${product.discount}>`;
        html += `<button type='button' id="getDetail" class="btn btn-default" data-pno=${product.pno} data-name=${product.name}>상세보기</button>`;
        html += `<button type='button' id="getCart" class='btn btn-primary' data-pno=${product.pno}>장바구니/바로주문</button>`;
        html += `</div>`;

        return html;
    }

    // 상품 상세 페이지
    function initGet(product) {
        if (product==null) {
            return;
        }

        productDetail.empty();

        let html = "";

        html += `<div class='col-lg-12 product-detail-info'>`;
        html += `${initProductImg(product.imageList)}`;
        html += `${initProductInfo(product)}`;
        html += `</div>`;

        html += `${initProductDetail(product)}`

        productDetail.append(html);
    }

    function initGetHeader(category, keyword) {
        pageHeader.empty();

        if (category==='null' || category===null) {
            pageHeader.append(`<i class='fa fa-search'> ${keyword}</i>`)
        } else {
            pageHeader.append(`<i class='fa fa-th'> ${category}</i>`)
        }
    }

    function initGetFooter(category, keyword) {
        let url = category==='null' || category==null ?
             `/product/list?keyword=${keyword}` : `/product/list?code=${category}`;

        pageFooter.empty();
        pageFooter.append(`<button type='button' class='btn btn-default center-block'><a href=${url}>목록</a></button>`);
    }

    function initProductImg(imageList) {
        if (!imageList || imageList.length === 0) {
            return `<div class='col-lg-6 product-img'><img class="img-thumbnail" src='/resources/foodive/attach.png' alt='상품이미지'></div>`;
        }

        let html = `<div class='col-lg-6 product-img'><div class="big-image"></div>`;

        $(imageList).each(function (i, obj) {
            if (obj.fileType) {
                let fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
                let originalFileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);

                html += `<div class='product-thumbnail' data-path=${originalFileCallPath}><img class="img-thumbnail" src='/display?isImage=true&fileName=${fileCallPath}' alt='상품이미지'></div>`;
            }

        })

        html += `</div>`;

        return html;
    }

    function initBigImage(fileCallPath) {
        $(".big-image").html(`<img src='/display?isImage=true&fileName=${fileCallPath}' alt='상품_큰_이미지'>`)
            .animate({'width': '100%', 'height': '100%'}, 1000, function () {
                let height = $(".big-image img").height();
                console.log("height: "+height);

                $(".product-img").height(`${height}px`);
            });

    }

    function initProductInfo(product) {
        let html = `<div class="panel panel-info detail-info">`;

        html += `<div class='panel-heading product-name'>${product.korName}</div>`;
        html += `${initPanelBody(product)}`;
        html += `<div class='panel-footer'>`;
        if (Number(product.stock) <= 0) {
            html += `<fieldset disabled>`;
            html += `${initProductBtn(product)}`;
            html += `</fieldset>`;
        } else {
            html += `${initProductBtn(product)}`;
        }
        html += `</div>`;

        html += `</div>`;

        return html;
    }

    function initProductBtn(product) {
        let html = "";

        let price = product.price;
        let discount = product.discount;
        let realPrice = discount!=0 ? price * (100-discount)/100 : price;

        html += `<div class='product-btn' data-kor=${product.korName} data-price=${realPrice} data-stock=${product.stock}>`;
        html += `<button type='button' class='btn btn-default' data-type="cart" data-pno=${product.pno}>장바구니</button>`;
        html += `<button type='button' class='btn btn-primary' data-type="order" data-pno=${product.pno}>주문하기</button>`;
        html += `</div>`;

        return html;
    }

    function initPanelBody(product) {
        let html = `<div class='panel-body'>`;
        html += `<div class='product-nation'>원산지: ${product.nation}</div>`;
        html += `<div class='product-price'>${initPrice(product)}</div>`;
        html += `${initStock(product)}`;
        html += `</div>`;

        return html;
    }

    function initStock(product) {
        let stock = Number(product.stock);
        let html = `<div class='product-stock'>`;

        if (stock > 0) {
            if (stock < 5) {
                html += `<div>품절 임박! 현재 재고가 ${stock}개 남았습니다.</div>`
            }
            html += `<div class='form-group input-group qty'>`;
            html += `<span class="input-group-btn"><button class="btn btn-default" data-type="minus" data-stock=${stock}><i class="fa fa-minus"></i></button></span>`
            html += `<input type='text' class="form-control" name='qty' id='qty' value='0' readonly>`;
            html += `<span class="input-group-btn"><button class="btn btn-default" data-type="plus" data-stock=${stock}><i class='fa fa-plus'></i></button></span>`
            html += `</div>`;
        } else {
            html += `<div>품절</div>`;
        }

        html += `</div>`;

        return html;
    }

    function initProductDetail(product) {
        let html = `<div class='col-lg-12'>`;

        html += `<div class='panel panel-default'>`;
        html += `<div class='panel-body'>`;
        html += `${initNavPills()}`;
        html += `${initTabPills(product)}`;

        html += `</div>`;

        return html;
    }

    function initNavPills() {
        let html = `<ul class='nav nav-pills'>`;

        html += `<li class="active"><a href="#detail-pills" data-toggle="tab">상세페이지</a></li>`;
        html += `<li><a href="#review-pills" data-toggle="tab">리뷰</a></li>`;

        html += `</ul>`;

        return html;
    }

    function initTabPills(product) {
        let html = `<div class='tab-content'>`;

        html += `<div class='tab-pane fade in active' id='detail-pills'>${product.detail}</div>`;
        html += `<div class='tab-pane fade' id='review-pills'>리뷰</div>`;

        html += `</div>`;

        return html;
    }


    return {
        initList:initList,
        initListFooter:initListFooter,
        initGet:initGet,
        initGetHeader:initGetHeader,
        initBigImage:initBigImage,
        initGetFooter:initGetFooter
    }
})

const productCart = (function () {

    function minus(qty, stock) {
        if (0 > (qty - 1)) {
            alert('구매 수량은 0보다 적을 수 없습니다.');
            return false;
        }
        return true;
    }

    function plus(qty, stock) {
        if (stock < (qty + 1)) {
            alert('상품 재고보다 더 많이 구매하실 수 없습니다.');
            return false;
        }
        return true;
    }

    return {
        minus: minus,
        plus: plus
    }
});