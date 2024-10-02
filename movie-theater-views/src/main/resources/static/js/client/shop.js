function callApiGetALlFood() {
    var settings = {
        url: "http://localhost:8080/api/pub/food",
        method: "GET",
        success: function (response, status, xhr) {
            let listProduct = $('.list-product');
            if (xhr.status === 204) {
                listProduct.append('<p>Xin lỗi tạm thời chúng tôi chưa có đồ ăn!</p>');
            }
            else {
                listProduct.empty();
                for (var index = 0; index < response.data.length; index++) {
                    renderFood(response.data[index], index);
                }
            }
        },
        error: function (xhr) {
            var response = JSON.parse(xhr.responseText);
            console.log(response.message);
            alert("Có lỗi khi tải dữ liệu!");
        }
    }
    $.ajax(settings);
}

//Hàm map data danh sách đồ ăn
function renderFood(food, index) {
    // Xác định xem sản phẩm có hết hàng không
    const isOutOfStock = parseInt(food.stock) === 0;
    const outOfStockClass = isOutOfStock ? 'out-of-stock-overlay' : '';
    const isHot = index < 3;

    const foodItem = `
        <div class="product" style="position: relative;" data-id="${food.id}">
            <div class="product-image">
                <img src="${food.image}" alt="${food.name}">
                <div class="out-of-stock" style="${isOutOfStock ? 'display: flex;' : 'display: none;'}">
                    <span>HẾT HÀNG</span>
                </div>
            </div>
            <div class="product-name">
                <p>${food.name}</p>
                <div class="number-buy">
                    <div class="choose-number">
                        <i class="fas fa-minus"></i>
                        <input type="hidden" class="stock" data-value="${food.stock}">
                        <input type="text" value="0" class="number-product" data-name="${food.name}">
                        <i class="fas fa-plus"></i>
                    </div>
                    <div class="btn-buy"
                            data-id="${food.id}"
                            data-name="${food.name}"
                            data-quantity="0"
                            data-price="${food.price}">
                        <button>Mua</button>
                    </div>
                </div>
            </div>
            <div class="custom-shape" id="food-price">${food.price}</div>
            <div class="hot-label" style="${isHot ? 'display:block;' : 'display:none;'}">HOT</div>
            <div class="${outOfStockClass}"></div>
        </div>`;
    $('.list-product').append(foodItem);
}

var swiper = new Swiper('.swiper-container', {
    loop: true,
    spaceBetween: 300,
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
    },
    autoplay: {
        delay: 3000,
        disableOnInteraction: false,
    },
});