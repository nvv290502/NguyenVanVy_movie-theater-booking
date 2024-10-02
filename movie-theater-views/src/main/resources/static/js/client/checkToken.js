// Danh sách URL cần chặn
const restrictedUrls = [
    "/movie/buy-ticket",
    "/user-info"
];

function checkToken() {
    var accessTokenLocal = localStorage.getItem('accessToken');
    var accessTokenGoogle = localStorage.getItem('access_token');
    var currentPath = window.location.pathname;

    // Kiểm tra xem URL hiện tại có nằm trong danh sách URL cần chặn không
    if (restrictedUrls.includes(currentPath)) {
        if (accessTokenGoogle == null && accessTokenLocal == null) {
            localStorage.setItem('redirectUrl', currentPath);
            window.location.href = "/login";
            return; // Dừng thực thi nếu không có token
        }

        // Kiểm tra accessTokenLocal nếu có
        if (accessTokenLocal != null) {
            callApiIsValidAccessTokenLocal(accessTokenLocal);
        }

        // Kiểm tra accessTokenGoogle nếu có
        if (accessTokenGoogle != null) {
            callApiIsValidAccessTokenGoogle(accessTokenGoogle);
        }
    }
}

function callApiIsValidAccessTokenLocal(accessToken) {
    $.ajax({
        url: "http://localhost:8080/auth/validate-token?token=" + accessToken,
        method: "GET",
        success: function (response) {
            console.log("Local token valid:", response);
        },
        error: function () {
            window.location.href = "/login";
        }
    });
}

function callApiIsValidAccessTokenGoogle(accessToken) {
    $.ajax({
        url: "http://localhost:8080/oauth2/validate-token?accessToken=" + accessToken,
        method: "GET",
        success: function (response) {
            console.log("Google token valid:", response);
        },
        error: function () {
            window.location.href = "/login";
        }
    });
}

// Gọi hàm checkToken khi tài liệu đã sẵn sàng
$(document).ready(function () {
    checkToken();
});
