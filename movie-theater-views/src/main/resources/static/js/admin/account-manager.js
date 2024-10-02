$(document).ready(function () {

    $(document).on('click', '.add-account', function (e) {
        e.preventDefault();
        $('.form-account-add').css('opacity', '1').css('top', '50%');
        $('.manager-container').css('opacity', '1').css('z-index','1');
    });

    $(document).on('click', '.btn-detail-account-manager', function (e) {
        e.preventDefault();
        $('.form-account-detail').css('opacity', '1').css('top', '50%');
        $('.manager-container').css('opacity', '1').css('z-index','1');
    });

    $(document).on('click', '.close-icon', function (e) {
        e.preventDefault();
        $('.form-account-manager').css('opacity', '0').css('top', '150%');
        $('.manager-container').css('opacity', '0').css('z-index','-1');
    });
});