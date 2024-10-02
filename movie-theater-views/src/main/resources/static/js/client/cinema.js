
$(document).ready(function () {
    //  <!-- Tải header -->
    $('#header').load("/header", function () {
        checkAccessToken();
        $.getScript("/js/client/header.js");
    });

    $(document).on('click', '.cinema-detail', function (e) {
        e.preventDefault();
        window.location.href = "/cinema/detail?cinema="+localStorage.getItem('cinema');
    });

});

// <!-- xử lí vị trí map
$(document).ready(function () {
    var latitude = 10.8231;  // Tọa độ ví dụ
    var longitude = 106.6297; // Tọa độ ví dụ

    $('.location-icon').on('click', function () {
        $('#map-container').show();

        var map = L.map('map').setView([latitude, longitude], 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '© OpenStreetMap'
        }).addTo(map);

        L.marker([latitude, longitude]).addTo(map)
            .bindPopup('Vị trí rạp!')
            .openPopup();
    });

    $('#close-map').on('click', function () {
        $('#map-container').hide();
    });
});