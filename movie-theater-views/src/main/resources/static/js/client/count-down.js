$(document).ready(function () {
    let endTime = parseInt(localStorage.getItem('endTime'), 10); // Đảm bảo là số
    if (!endTime) {
        $('.count-down-info').hide();
        return;
    }
    let now = new Date().getTime();
    let countDownTime = endTime - now;

    if (countDownTime < 0) {
        countDownTime = 0;
    }

    function updateCountdown() {
        let now = new Date().getTime();
        let countDownTime = endTime - now;

        if (countDownTime < 0) {
            countDownTime = 0;
            clearInterval(countdownInterval);
            $('.count-down-info').hide();
            localStorage.removeItem('movie');
            localStorage.removeItem('room');
            localStorage.removeItem('showDate');
            localStorage.removeItem('showTime');
            localStorage.removeItem('cinema');
            localStorage.removeItem('selectedSeats');
            localStorage.removeItem('totalMoney');
            localStorage.removeItem('endTime');
            localStorage.removeItem('city');
            localStorage.removeItem('billCode');
            localStorage.removeItem('order');
            return;
        }

        const minutes = Math.floor((countDownTime % (1000 * 3600)) / (1000 * 60));
        const seconds = Math.floor((countDownTime % (1000 * 60)) / 1000);

        $('#count-down-time').text(`${pad(minutes)}:${pad(seconds)}`);
    }

    function pad(number) {
        return number < 10 ? '0' + number : number;
    }

    const countdownInterval = setInterval(updateCountdown, 1000);
    updateCountdown();

    $(document).on('click', '#continue-payment', function (e) {
        e.preventDefault();
        window.location.href = "/booking/food";
    })
});
