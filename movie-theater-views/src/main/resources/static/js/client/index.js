
$(document).ready(function () {
    $('#header').load("/header", function () {
        checkAccessToken();
        $.getScript("js/client/header.js");
    });
    $('#footer').load("/footer", function () {
        $.getScript("js/client/footer.js");
    });

    $('.title-card').hover(function () {
        $(this).find('.tooltip').css('display', 'block');
    }, function () {
        $(this).find('.tooltip').css('display', 'none');
    });

    localStorage.setItem('redirectUrl', "/home");

});


function checkAccessToken() {
    var accessTokenLocal = localStorage.getItem("accessToken");
    var accessTokenGoogle = localStorage.getItem("access_token");

    if (accessTokenGoogle == null) {
        handleCallback(function () {
            accessTokenGoogle = localStorage.getItem("access_token");
            executeAfterTokenReceived(accessTokenGoogle, accessTokenLocal);
        });
    } else {
        executeAfterTokenReceived(accessTokenGoogle, accessTokenLocal);
    }
}

function getInfo(accessToken, url) {
    $.ajax({
        url: url,
        method: "GET",
        headers: {
            'Authorization': `Bearer ${accessToken}` // Thêm 'Bearer ' nếu cần
        },
        success: function (response) {
            localStorage.setItem("userId", response.id);
            renderInfoUser(response);
        },
        error: function (xhr) {
            if (xhr.status === 401) {
                handleTokenExpired();
            } else {
                console.log("Có lỗi khi tải dữ liệu!");
            }
        }
    });
}

function handleCallback(callback) {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');

    if (code != null) {
        $.ajax({
            url: `http://localhost:8080/oauth2/access?code=${code}`,
            method: 'GET',
            success: function (data) {
                localStorage.setItem('access_token', data.access_token);
                if (callback) callback(); // Gọi callback sau khi token được lưu
            },
            error: function (xhr, status, error) {
                console.log(error);
            }
        });
    } else {
        if (callback) callback(); // Gọi callback nếu không có code
    }
}

function executeAfterTokenReceived(accessTokenGoogle, accessTokenLocal) {
    if (accessTokenLocal != null) {
        $(".no-login").css("display", "none");
        getInfo(accessTokenLocal, "http://localhost:8080/user/info");
    }
    if (accessTokenGoogle != null) {
        $(".no-login").css("display", "none");
        getInfo(accessTokenGoogle, "http://localhost:8080/oauth2/getUser");
    }
    if (accessTokenGoogle == null && accessTokenLocal == null) {
        $(".yes-login").css("display", "none");
        console.log("Bạn chưa đăng nhập!");
    }
}

function renderInfoUser(user) {
    const info = `<img class="user-avatar" src="${user.avatarUrl}" alt=""
                        width="100%" height="100%" style="object-fit: cover; object-position: center;">`
    $('.avatar-placeholder').append(info);
}

function handleTokenExpired() {
    // Xóa token trong localStorage
    localStorage.removeItem("accessToken");
    localStorage.removeItem("access_token");

    // Chuyển về trạng thái chưa đăng nhập
    $(".yes-login").css("display", "none");
    $(".no-login").css("display", "flex");
    console.log("Token đã hết hạn, bạn cần đăng nhập lại!");
}

$(document).on('click', '.btn-moving-now, .btn-detail-movie, .hero-btn-movie-detail', function (e) {
    e.preventDefault();
    let id = $(this).data("id");
    let url = "/movie?id=" + id;
    $("body").load(url, function () {
        window.location.href = url;
    });
});

$(document).on('click', '.btn-buy-ticket, .hero-btn-buy-ticket', function (e) {
    e.preventDefault();
    let id = $(this).data("id");
    let url = "/movie/buy-ticket?id=" + id;
    localStorage.setItem('movie', id);
    $("body").load(url, function () {
        window.location.href = url;
    });
});
