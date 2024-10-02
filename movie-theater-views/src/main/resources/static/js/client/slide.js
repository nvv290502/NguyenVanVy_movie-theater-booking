$(document).ready(function () {
    var thumbsSwiper = new Swiper('.swiper-container-thumbs', {
        spaceBetween: 10,
        slidesPerView: 1,
        watchSlidesProgress: true,
        centerInsufficientSlides: true,
        centeredSlides: true,
        slideToClickedSlide: true,
        loop: true,
        loopedSlides: 3,
    });

    var autoplayConfig = false;
    if (window.innerWidth > 768) {
        autoplayConfig = {
            delay: 3000,
            disableOnInteraction: false,
        };
    }

    var mainSwiper = new Swiper('.swiper-container', {
        loop: true,
        spaceBetween: 30,
        effect: 'fade',
        fadeEffect: {
            crossFade: true
        },
        speed: 1000,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        thumbs: {
            swiper: thumbsSwiper,
        },
        autoplay: autoplayConfig,
    });

    mainSwiper.on('slideChange', function () {
        thumbsSwiper.slideToLoop(mainSwiper.realIndex, 300, false);
    });
});
