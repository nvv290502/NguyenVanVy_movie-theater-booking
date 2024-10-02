$(document).ready(function () {

    $(document).on('click', '.add-movie', function (e) {
        e.preventDefault();
        $('.form-movie-add').css('opacity', '1').css('top', '50%');
        $('.manager-container').css('opacity', '1').css('z-index','1');
    });

    $(document).on('click', '.btn-detail-movie-manager', function (e) {
        e.preventDefault();
        $('.form-movie-detail').css('opacity', '1').css('top', '50%');
        $('.manager-container').css('opacity', '1').css('z-index','1');
    });

    $(document).on('click', '.close-icon', function (e) {
        e.preventDefault();
        $('.form-movie-manager').css('opacity', '0').css('top', '150%');
        $('.manager-container').css('opacity', '0').css('z-index','-1');
    });
});