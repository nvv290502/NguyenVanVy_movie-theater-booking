
$(document).ready(function () {
    const daysOfWeek = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    let currentStartDate = getStartOfWeek(new Date());
    let selectedCells = []; // Mảng lưu trữ các ô đã chọn

    // Tính toán ngày bắt đầu của tuần (thứ hai)
    function getStartOfWeek(date) {
        const day = date.getDay() || 7;
        if (day !== 1) {
            date.setDate(date.getDate() - day + 1);
        }
        return new Date(date.setHours(0, 0, 0, 0));
    }

    // Chèn tiêu đề cột ngày động vào lịch
    function insertDynamicDays() {
        const theadRow = $('thead tr');
        theadRow.find('th:gt(0)').remove(); // Xóa các tiêu đề cột cũ

        daysOfWeek.forEach(function (day, index) {
            const currentDay = new Date(currentStartDate);
            currentDay.setDate(currentStartDate.getDate() + index);
            const dayHeader = `${day} (${currentDay.getDate()}/${currentDay.getMonth() + 1})`;
            theadRow.append(`<th class="calender-date" data-date="${currentDay.toDateString()}">${dayHeader}</th>`);
        });


        // Xóa nội dung cũ trước khi chèn mới
        $('tbody').empty();

        // Tạo hàng giờ và ô trống
        for (let i = 0; i < 24; i++) {
            const timeRow = $('<tr></tr>').append(`<th class="calender-time">${i}:00</th>`);
            for (let j = 0; j < 7; j++) {
                const dateStr = new Date(currentStartDate);
                dateStr.setDate(currentStartDate.getDate() + j);
                const timeStr = `${i}:00`;
                timeRow.append(`<td class="calender-movie" data-date="${dateStr.toDateString()}" data-time="${timeStr}"></td>`);
            }
            $('tbody').append(timeRow);
        }

        // Chèn lại phim vào lịch
        movieData.forEach(function (movie) {
            insertMovieIntoCalendar(movie);
        });
    }

    // Giả lập dữ liệu phim
    const movieData = [
        { title: 'Movie 1', date: new Date(2024, 7, 12, 8, 0) },
        { title: 'Movie 2', date: new Date(2024, 7, 14, 14, 0) },
        { title: 'Movie 3', date: new Date(2024, 7, 16, 20, 0) },
        { title: 'Movie 4', date: new Date(2024, 7, 26, 1, 0) }
    ];

    // Hàm để chèn tên phim vào đúng ô trong lịch
    function insertMovieIntoCalendar(movie) {
        const movieDate = new Date(movie.date);
        const startOfWeek = new Date(currentStartDate);
        const endOfWeek = new Date(currentStartDate);
        endOfWeek.setDate(startOfWeek.getDate() + 6);

        // Kiểm tra xem ngày của bộ phim có nằm trong tuần hiện tại không
        if (movieDate >= startOfWeek && movieDate <= endOfWeek) {
            const dayIndex = $('thead th').filter(function () {
                return new Date($(this).data('date')).toDateString() === movieDate.toDateString();
            }).index() - 1; // -1 để điều chỉnh chỉ số cột (ngày) vì cột đầu tiên là trống

            const timeIndex = $('tbody th').filter(function () {
                return $(this).text() === `${movieDate.getHours()}:00`;
            }).closest('tr').index();

            $('tbody tr').eq(timeIndex).find('td').eq(dayIndex).addClass('movie-cell').text(movie.title);
        }
    }

    // Sự kiện chuyển sang tuần trước
    $('#prev-week').click(function () {
        currentStartDate.setDate(currentStartDate.getDate() - 7);
        insertDynamicDays();
    });

    // Sự kiện chuyển sang tuần tiếp theo
    $('#next-week').click(function () {
        currentStartDate.setDate(currentStartDate.getDate() + 7);
        insertDynamicDays();
    });

    // Khởi tạo lịch và chèn phim
    insertDynamicDays();

    // Hàm định dạng ngày theo kiểu YYYY/MM/DD
    function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}/${month}/${day}`;
    }

    // Hàm định dạng giờ theo kiểu HH:mm
    function formatTime(date) {
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${hours}:${minutes}`;
    }


    // Sự kiện click vào ô
    $('table').on('click', 'td', function () {
        // Thêm hoặc gỡ lớp chọn cho ô hiện tại
        $(this).toggleClass('selected');

        // Lưu hoặc xóa ô từ mảng selectedCells
        const cellIndex = selectedCells.indexOf(this);
        if (cellIndex === -1) {
            selectedCells.push(this);
        } else {
            selectedCells.splice(cellIndex, 1);
        }
    });

    // Sự kiện click vào nút xác nhận
    $('#confirm-selection').click(function () {

        // Sắp xếp mảng các ô đã chọn theo thứ tự thời gian
        selectedCells.sort((a, b) => {
            const timeA = new Date($(a).data('date') + ' ' + $(a).data('time'));
            const timeB = new Date($(b).data('date') + ' ' + $(b).data('time'));
            return timeA - timeB;
        });

        // Lấy thời gian bắt đầu và kết thúc
        const startCell = selectedCells[0];
        const endCell = selectedCells[selectedCells.length - 1];

        const startDate = new Date($(startCell).data('date') + ' ' + $(startCell).data('time'));
        const endDate = new Date($(endCell).data('date') + ' ' + $(endCell).data('time'));

        // Điều chỉnh endDate để bao gồm toàn bộ giờ kết thúc
        endDate.setHours(endDate.getHours() + 1);

        // Định dạng ngày
        const formattedDate = formatDate(startDate);

        // Định dạng khoảng thời gian
        const formattedStartTime = formatTime(startDate);
        const formattedEndTime = formatTime(endDate);
        const timeRange = `${formattedStartTime} - ${formattedEndTime}`;

        // Hiển thị kết quả
        console.log(`Ngày: ${formattedDate}`);
        console.log(`Khoảng thời gian: ${timeRange}`);
    });
});
