$(document).ready(function () {
    var listMovieName = [];
    var suggestionsList = $('#suggestions-list');

    callApiGetNameMovie();

    $('#search-movie').on('input', function () {
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
        $('#search-movie').val(selectedName);
        $('#suggestions-list').empty();

        //forcus vào thẻ input
        var inputElement = $('#search-movie')[0];
        inputElement.focus();
        inputElement.setSelectionRange(inputElement.value.length, inputElement.value.length);
    });

    $(document).on('click', function (event) {
        if (!$(event.target).closest('#search-movie, #suggestions-list').length) {
            $('#suggestions-list').empty();
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
    $('#search-movie').on('keypress', function (e) {
        if (e.which === 13) {
            e.preventDefault();
            var searchQuery = $(this).val();
            if (searchQuery.trim().length > 0) {
                $('.card-movie-search').css('width', '100vw');
                $('.card-movie-search').css('height', '100vh');
                $('.card-search').css('width', '260px');
                $('.card-search').css('height', '400px');
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
        $('#movie-released-date-search').text(movie.releasedDate);
        $('#movie-duration-search').text(movie.duration + " phút");
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
