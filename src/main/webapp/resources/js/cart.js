const cartFunction = (function () {

    function minus(qty, pno, thisTR) {
        if (0 > (qty - 1)) {
            if(confirm('구매 수량은 0보다 적을 수 없습니다. 장바구니에서 삭제하시겠습니까?')) {
                let param = {
                    pno:pno,
                    thisTR:thisTR
                }

                cartController().deleteCart(param);
                return;
            }
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

const cartController = (function () {

    function getCartList(id) {
        console.log("controller get list");
        cartService().getList(id, function (cartList) {
            console.log("service get list callback");
            cartInit().initCart(cartList);
        })
    }

    function deleteCart(param) {
        let pno = param.pno;
        let thisTR = param.thisTR;

        cartService().deleteCart(pno, function (result) {
            alert(result);

            thisTR.remove();
        })
    }

    function addCart(cart) {
        cartService().addCart(cart, function (result) {
            alert(result);

            if (confirm('장바구니 페이지로 이동하시겠습니까?')) {
                location.href = "/cart/cartPage";
            }
        })
    }

    function modifyCart(cart) {
        cartService().modify(cart, function (result) {
            alert(result);
        })
    }

    function deleteAll(cart) {
        if (cart.length === 0) {
            return;
        }
        for (let i=0, len=cart.length; i<len; i++) {
            let pno = cart[i].pno;
            let thisTR = cart[i].thisTR;

            console.log(`${i}번째 tr: `+JSON.stringify(thisTR));

            cartService().deleteCart(pno, function (result) {
                thisTR.remove();
            })
        }
        alert('장바구니 삭제를 완료했습니다.');
        location.reload();
    }

    return {
        getCartList:getCartList,
        deleteCart:deleteCart,
        addCart:addCart,
        modifyCart:modifyCart,
        deleteAll:deleteAll
    }
})

const cartService = (function () {

    function getList(id, callback, error) {
        $.getJSON(`/cart/get/${id}`,
            function (data) {
                if (callback) {
                    callback(data);
                }
            }).fail(function (xhr, status, err) {
            error();
        })
    }

    function deleteCart(pno, callback, error) {
        $.ajax({
            type:'delete',
            url: `/cart/${pno}`,
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

    function addCart(cart, callback, error) {
        $.ajax({
            type:'post',
            url: '/cart/add',
            data: JSON.stringify(cart),
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

    function modify(cart, callback, error) {
        $.ajax({
            type: 'put',
            url: `/cart/modify/${cart.id}`,
            data:JSON.stringify(cart),
            contentType: 'application/json; charset=utf-8',
            success: function (result, status, xhr) {
                if (callback) {
                    callback(result);
                }
            },
            error: function (xhr, status, er) {
                if(error) {
                    error(er);
                }
            }
        })
    }

    return {
        getList: getList,
        deleteCart:deleteCart,
        addCart:addCart,
        modify:modify
    }
})

const cartInit = (function () {

    let pageBody = $(".page-body");
    let pageFooter = $(".page-footer");
    let sum = 0;

    function initCart(cartList) {
        pageBody.empty();

        if (!cartList || cartList.length === 0) {
            pageBody.append(`장바구니에 담은 상품이 없습니다.`);
            return;
        }

        let html = `<div class='col-lg-12'>`;
        html += `<div class='panel panel-default'>`;
        html += `<div class='panel-heading'></div>`;
        html += `<div class='panel-body'>`;
        html += `<div class='table-responsive'>`;
        html += `<table class='table table-hover'>`;

        html += `${initCartTHead()}`;
        html += `${initCartBody(cartList, sum)}`;

        html += `</table>`;
        html += `</div>`; // div.class.table-responsive
        html += `</div>`; // div.class.panel-body
        html += `<div class='panel-footer'>`;

        html += `${initCartPanelFooter()}`;

        html += `</div>`; //div.class.panel-footer
        html += `</div>`; // div.class.panel panel-default
        html += `</div>`; // div.class.col-lg-12

        pageBody.append(html);

        initCartFooter();

    }

    function initCartPanelFooter() {
        console.log("sum: "+sum);
        let html = `<div class='cart-total-page'>`;

        html += `<div data-sum=${sum}>총액 : ${sum.toLocaleString(undefined, {maximumFractionDigits:0})} 원</div>`

        html += `</div>`; // div.class.center-block

        return html;
    }

    function initCartTHead() {
        let html = `<thead><tr>`;
        html += `<th></th>`;
        html += `<th>#번호</th>`;
        html += `<th>상품명</th>`;
        html += `<th>정상가</th>`;
        html += `<th>할인가</th>`;
        html += `<th>수량</th>`;
        html += `<th>총액</th>`;
        html += `<th></th>`;
        html += `</tr></thead>`;

        return html;
    }

    function initCartBody(cartList) {
        if (cartList==null||cartList.length===0) {
            console.log("카트 없음");
            return;
        }

        let html = `<tbody>`;

        for (let i=0, len=cartList.length||0; i<len; i++) {
            console.log("cart list pno: "+cartList[i].pno);

            html += `<tr data-cno=${cartList[i].cno} data-pno=${cartList[i].pno} data-kor=${cartList[i].korName}>`;

            html += `<td><input type="checkbox" id=${cartList[i].pno} value=${cartList[i].pno}><label for=${cartList[i].pno} class="fa"></label></td>`
            html += `<td>${i+1}</td>`;
            html += `<td>${cartList[i].korName}</td>`;
            html += `<td class="td-price" data-price=${cartList[i].price}>${cartList[i].price.toLocaleString(undefined, {maximumFractionDigits:0})} 원</td>`;
            html += `<td class="td-real-price" data-realprice=${cartList[i].realPrice}>${cartList[i].realPrice.toLocaleString(undefined, {maximumFractionDigits:0})} 원</td>`;
            html += `<td>${initCartQty(cartList[i])}</td>`;
            html += `<td class="td-total-price" data-total=${cartList[i].totalPrice}>${cartList[i].totalPrice.toLocaleString(undefined, {maximumFractionDigits:0})} 원</td>`;
            html += `<td class="deleteBtn"><button type="button" class="btn btn-default" data-type="delete"><span class="glyphicon glyphicon-remove"></span></button></td>`

            html += `</tr>`;
        }

        html += `</tbody>`;

        return html;
    }

    function initCartQty(cart) {
        let stock = cart.stock;

        let html = `<div class='cart-stock'>`;
        html += `<div class='form-group input-group qty'>`;

        html += `<span class="input-group-btn"><button class="btn btn-default" data-type="minus" data-stock=${stock}><i class="fa fa-minus"></i></button></span>`
        html += `<input type='text' class="form-control" name='qty' value=${cart.qty} readonly>`;
        html += `<span class="input-group-btn"><button class="btn btn-default" data-type="plus" data-stock=${stock}><i class='fa fa-plus'></i></button></span>`

        html += `</div>`; //div.class.form-group
        html += `</div>`; //div.class.cart-stock

        return html;
    }

    function initCartFooter() {
        pageFooter.empty();

        let html = `<div class='cart-page-btn'>`;

        html += `<button type="button" class="btn btn-default" data-number="none">전체 선택 해제</button>`;
        html += `<button type="button" class="btn btn-default" data-number="some" data-type="delete">선택 삭제</button>`
        html += `<button type="button" class="btn btn-default" data-number="all" data-type="delete">전체 삭제</button>`
        html += `<button type="button" class="btn btn-primary" data-number="some" data-type="order">선택 주문</button>`
        html += `<button type="button" class="btn btn-primary" data-number="all" data-type="order">전체 주문</button>`

        html += `</div>`; //div.class.cart-page-btn

        pageFooter.append(html);
    }

    // 상품 목록 페이지에서 상품 수량 모달창 오픈
    let modal = $(".modal");
    let modalTitle = $("#shopModal .modal-title");
    let modalBody = $("#shopModal .modal-body");
    let modalFooter = $("#shopModal .modal-footer");
    function initCartModal(product) {
        initCartModalTitle(product);
        initCartModalBody(product);
        initCartModalFooter(product);
        modal.modal("show");
    }

    function initCartModalTitle(product) {
        modalTitle.empty();
        modalTitle.append(`${product.korName}`);
    }

    function initCartModalBody(product) {
        modalBody.empty();

        let html = `<div class='col-lg-12' data-pno=${product.pno}>`;

        // 가격
        let price = product.price;
        let discount = product.discount;
        let realPrice = discount!=0 ? price * (100-discount)/100 : price;

        if (discount==0) {
            html += `<div class="real-price">가격: ${price.toLocaleString(undefined, {maximumFractionDigits:0})}</div>`;
        } else {
            html += `<div class="real-price">가격: ${realPrice.toLocaleString(undefined, {maximumFractionDigits:0})}</div>`;
        }

        // 수량 조절 버튼
        let stock = product.stock;
        let cart = {stock:stock, qty:0};
        html += `${initCartQty(cart)}`;

        html += `</div>`; //div.class.col-lg-12

        modalBody.append(html);
        modalBody.height("100px");
    }

    function initCartModalFooter(product) {
        modalFooter.empty();

        let price = product.price;
        let discount = product.discount;
        let realPrice = discount!=0 ? price * (100-discount)/100 : price;

        let html = `<div class='modal-product-btn' data-kor=${product.korName} data-price=${realPrice} data-stock=${product.stock}>`;
        html += `<button type='button' class='btn btn-default' data-type="cart" data-pno=${product.pno}>장바구니</button>`;
        html += `<button type='button' class='btn btn-primary' data-type="order" data-pno=${product.pno}>주문하기</button>`;
        html += `</div>`;

        modalFooter.append(html);
    }

    // 장바구니 페이지 변경
    let cartTotalPage = $(".cart-total-page");

    function initModifyCart(thisTR, qtyVal) {
        let realPrice = Number(thisTR.find(".td-real-price").data("realprice"));
        let oldTotal = Number(thisTR.find(".td-total-price").data("total"));
        let newTotal = realPrice * qtyVal;
        thisTR.find(".td-total-price").empty().append(`${newTotal.toLocaleString(undefined, {maximumFractionDigits:0})} 원`).data("total", newTotal);

        let checkbox = thisTR.find("input:checkbox");
        if (checkbox.is(':checked')) {
            let cloneSum = Number(cartTotalPage.find("div").data("sum"));
            let newSum = cloneSum - oldTotal + newTotal;

            cartTotalPage.empty();
            cartTotalPage.append(`<div data-sum=${newSum}>총액 : ${newSum.toLocaleString(undefined, {maximumFractionDigits:0})} 원</div>`);
        }
    }

    // 장바구니 체크하면 총액 출력
    function printCartSum(thisTR, isChecked) {
        let cloneSum = Number(cartTotalPage.find("div").data("sum"));
        let totalPrice = Number(thisTR.find(".td-total-price").data("total"));
        totalPrice = isChecked ? totalPrice : -totalPrice;
        let newSum = cloneSum + totalPrice;

        cartTotalPage.empty();
        cartTotalPage.append(`<div data-sum=${newSum}>총액 : ${newSum.toLocaleString(undefined, {maximumFractionDigits:0})} 원</div>`);
    }

    return {
        initCart: initCart,
        initCartModal:initCartModal,
        initModifyCart:initModifyCart,
        printCartSum:printCartSum
    }
})