$(document).ready(function () {
    $('.shop-cinema').load("/shop", function () {
        $.getScript("/js/client/shop.js");
    })

    var numberProduct = 0;


    $(document).on('click', ".fa-minus", function () {
        var $product = $(this).closest('.choose-number');
        var $numberProductInput = $product.find('.number-product');
        numberProduct = parseInt($numberProductInput.val()) || 0;
        if (numberProduct > 0) {
            numberProduct--;
        }
        $numberProductInput.val(numberProduct);
    });

    $(document).on('click', ".fa-plus", function () {
        var $product = $(this).closest('.choose-number');
        var $numberProductInput = $product.find('.number-product');
        const $stock = $product.find('.stock');
        numberProduct = parseInt($numberProductInput.val()) || 0;
        if (numberProduct < $stock.data("value")) {
            numberProduct++;
        }
        $numberProductInput.val(numberProduct);
    });


});