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

    return {
        getCartList:getCartList,
        deleteCart:deleteCart,
        addCart:addCart,
        modifyCart:modifyCart
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
        html += `${initCartBody(cartList)}`;

        html += `</table>`;
        html += `</div>`; // div.class.table-responsive
        html += `</div>`; // div.class.panel-body
        html += `<div class='panel-footer'>`;



        html += `</div>`; //div.class.panel-footer
        html += `</div>`; // div.class.panel panel-default
        html += `</div>`; // div.class.col-lg-12

        pageBody.append(html);

    }

    function initCartTHead() {
        let html = `<thead><tr>`;
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
            html += `<tr data-cno=${cartList[i].cno} data-pno=${cartList[i].pno}>`;

            html += `<td>${i+1}</td>`;
            html += `<td>${cartList[i].korName}</td>`;
            html += `<td>${cartList[i].price}</td>`;
            html += `<td>${cartList[i].realPrice}</td>`;
            html += `<td>${initCartQty(cartList[i])}</td>`;
            html += `<td>${cartList[i].totalPrice}</td>`;
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

    function initCartFooter(cart) {

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

        let html = `<div class='modal-product-btn'>`;
        html += `<button type='button' class='btn btn-default' data-type="cart" data-pno=${product.pno}>장바구니</button>`;
        html += `<button type='button' class='btn btn-primary' data-type="order" data-pno=${product.pno}>주문하기</button>`;
        html += `</div>`;

        modalFooter.append(html);
    }

    return {
        initCart: initCart,
        initCartModal:initCartModal
    }
})