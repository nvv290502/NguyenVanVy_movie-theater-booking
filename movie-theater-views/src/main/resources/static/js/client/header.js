
// document.addEventListener('DOMContentLoaded', function () {
//     var dropdownToggle = document.querySelector('.dropdown-toggle');
//     var dropdownMenu = document.querySelector('.show-menu');

//     console.log(dropdownToggle); // Kiểm tra xem dropdownToggle có được chọn không
//     console.log(dropdownMenu);

//     dropdownToggle.addEventListener('click', function () {
//         console.log("abc");
//         if (dropdownMenu.style.display === 'block') {
//             dropdownMenu.style.display = 'none';
//         } else {
//             dropdownMenu.style.display = 'block';
//         }
//     });
// });


$(document).ready(function () {

    $('#movie-show-today').addClass('active');

    $(document).on('click', '#movie-top, #movie-show-today, #movie-upcoming-show, #promotion', function (e) {
        e.preventDefault();
        moveActiveToTop();
    });

    $('.dropdown-toggle').on('click', function () {
        var $dropdownMenu = $(this).siblings('.show-menu');

        if ($dropdownMenu.is(':visible')) {
            $dropdownMenu.css('display', 'none');
        } else {
            $dropdownMenu.css('display', 'flex');
        }
    });

    $(document).on('click', '#cinema-list', function (e) {
        e.preventDefault();
        window.location.href = "/cinema";
    });

    $(document).on('click', '#list-movie', function (e) {
        e.preventDefault();
        window.location.href = "/movie/list";
    });

    $(document).on('click', '#shop-me', function (e) {
        e.preventDefault();
        window.location.href = "/shop-food";
    });

    $(document).on('click', '.yes-login-info', function (e) {
        e.preventDefault();
        window.location.href = "/user-info";
    });

    function moveActiveToTop() {
        var $menuList = $('.header-menu-mobile .menu-movie-list');
        var $activeItem = $menuList.find('.active');
        if ($activeItem.length) {
            $activeItem.prependTo($menuList);
        }
    }

    $('.menu-movie-list a').on('click', function (event) {
        if (this.hash !== "") {
            event.preventDefault();

            var hash = this.hash;

            $('html, body').animate({
                scrollTop: $(hash).offset().top - 170
            }, 0, function () {
                window.location.hash = hash;
            });

            $('.menu-movie-list li').removeClass('active');
            $(this).parent('li').addClass('active');
        }
    });

});

$(document).ready(function () {
    var listMovieName = [];
    var suggestionsList = $('#suggestions-list-header');

    callApiGetNameMovie();

    $('#search-movie-header').on('input', function () {
        var query = removeVietnameseTones($(this).val().toLowerCase());
        if (query.length >= 1) {
            displaySuggestions(query);
        } else {
            suggestionsList.empty();
        }
    });

    function displaySuggestions(query) {
        var filteredNames = listMovieName.filter(function (name) {
            // So sánh tên phim sau khi loại bỏ dấu
            return removeVietnameseTones(name.toLowerCase()).includes(query);
        });

        suggestionsList.empty();

        if (filteredNames.length === 0) {
            suggestionsList.append('<li>Không có gợi ý nào</li>');
        } else {
            for (var name of filteredNames) {
                suggestionsList.append(`<li>${name}</li>`);
            }
        }
    }

    suggestionsList.on('click', 'li', function () {
        var selectedName = $(this).text();
        $('#search-movie-header').val(selectedName);
        $('#suggestions-list-header').empty();

        //forcus vào thẻ input
        var inputElement = $('#search-movie-header')[0];
        inputElement.focus();
        inputElement.setSelectionRange(inputElement.value.length, inputElement.value.length);
    });

    $(document).on('click', function (event) {
        if (!$(event.target).closest('#search-movie-header, #suggestions-list-header').length) {
            $('#suggestions-list-header').empty();
        }
    });

    // Đóng card khi nhấn ra ngoài vùng card-movie
    $('.card-movie-search').on('click', function (e) {
        if ($(e.target).closest('.card-movie').length === 0) {
            closeCard();
        }
    });

    function closeCard() {
        $('.card-movie-search').css({ 'width': '0', 'height': '0' });
        $('.card-movie.card-search').css({ 'width': '0', 'height': '0' });
    }

    // Xử lý sự kiện khi nhấn phím Enter
    $('#search-movie-header').on('keypress', function (e) {
        if (e.which === 13) {
            e.preventDefault();
            $('.show-menu').css('display', 'none');
            var searchQuery = $(this).val();
            if (searchQuery.trim().length > 0) {
                $('.card-movie-search').css('width', '100vw');
                $('.card-movie-search').css('height', '100vh');
                $('.card-search').css('width', '350px');
                $('.card-search').css('height', '500px');
                callApiSearchMovieByName(searchQuery);
                performSearch(searchQuery);
            }

        }
    });

    function callApiGetNameMovie() {
        var settings = {
            url: "http://localhost:8080/api/pub/movie/getName",
            method: "GET",
            success: function (response) {
                listMovieName = response.data;
            },
            error: function (xhr) {
                var response = JSON.parse(xhr.responseText);
                toastr.error(response.message);
            }
        }
        $.ajax(settings);
    }

    // Hàm loại bỏ dấu tiếng Việt
    function removeVietnameseTones(str) {
        return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "")
            .replace(/đ/g, "d").replace(/Đ/g, "D");
    }

    function callApiSearchMovieByName(name) {
        var settings = {
            url: "http://localhost:8080/api/pub/movie/search?name=" + name,
            method: "GET",
            success: function (response) {
                renderInfoMovie(response.data);
            },
            error: function (xhr) {
                var response = JSON.parse(xhr.responseText);
                toastr.error(response.message);
            }
        }
        $.ajax(settings);
    }

    function renderInfoMovie(movie) {
        $('#image-movie-search').attr('src', movie.imageSmallUrl);
        $('#image-movie-search').attr('alt', movie.name);
        $('.btn-buy-ticket-search').attr('data-id', movie.id);
        $('.btn-detail-movie-search').attr('data-id', movie.id);
        $('#movie-name-search').text(movie.name);
        $('#show-full-name-search').text(movie.name);
        $('#movie-released-date-search').text(movie.releasedDate);
    }

    function performSearch(query) {
        if (!query) return;

        window.searchQuery = query;

        // Tìm tất cả các thẻ card-movie chứa tên phim khớp với truy vấn tìm kiếm
        let $results = $('.card-movie').filter(function () {
            return $(this).find('.card-movie-name').text().toLowerCase().includes(query.toLowerCase());
        });

        if ($results.length > 0) {
            // Di chuyển đến kết quả tìm kiếm đầu tiên
            $('html, body').animate({
                scrollTop: $results.first().offset().top
            }, 100, function () {
                $results.first().addClass('highlight');
            });
        } else {
            console.log("Không tìm thấy phim nào khớp với tìm kiếm của bạn.");
        }
    }

});
