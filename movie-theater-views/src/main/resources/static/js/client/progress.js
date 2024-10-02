// script.js
// $(document).ready(function () {
//     var checkPointText = $('#check-point').text().trim();
//     var checkPoint = parseInt(checkPointText, 10);

//     if (isNaN(checkPoint)) {
//         checkPoint = 0;
//     }

//     console.log(checkPoint);
function updateProgress(currentStepIndex) {
    $('.progress-step').each(function (index) {
        if (index < currentStepIndex) {
            $(this).addClass('completed').removeClass('active');
        } else if (index === currentStepIndex) {
            $(this).addClass('active').removeClass('completed');
        } else {
            $(this).removeClass('active completed');
        }
    });
}

//     updateProgress(checkPoint);


// });
