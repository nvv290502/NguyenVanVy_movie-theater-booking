
var calendars = [];
var schedules = [];
var draggable;
$(document).ready(function () {

  initializeDraggable();

  let isDragging = false;
  let offset = { x: 0 };
  const $externalEvents = $('#external-events');

  $('#move-events').on('mousedown', function (e) {
    isDragging = true;
    offset.x = e.clientX - $externalEvents[0].getBoundingClientRect().left;
    e.preventDefault();
  });

  $(document).on('mousemove', function (e) {
    if (isDragging) {
      $externalEvents.css({
        'position': 'fixed',
        'left': `${e.clientX - offset.x}px`
      });
    }
  });

  $(document).on('mouseup', function () {
    isDragging = false;
  });

  $('#toggle-btn').on('click', function () {
    const $movieBlock = $('#movie-block');
    const $movieSearch = $('#movie-search');
    const $icon = $('#toggle-icon');
    const isVisible = $movieBlock.is(':visible');

    $movieBlock.toggle(!isVisible);
    $movieSearch.toggle(!isVisible);
    $icon.toggleClass('fa-chevron-down', !isVisible);
    $icon.toggleClass('fa-chevron-up', isVisible);
  });

  fetchCinema().then(function (result) {
    cinema = result;
    var select = $("#cinema");
    select.empty();
    select.append("<option selected value='0'>Chọn Rạp Chiếu Phim</option>");
    result.forEach(function (cimema) {
      select.append(
        $("<option>", {
          value: cimema.id,
          text: cimema.name,
        })
      );
    });
    $("#cinema").on("change", function () {
      var selectedCinemaId = $(this).val();

      if (selectedCinemaId !== "0") {
        calendars = removeCalendar(calendars); // Xóa các lịch hiện có
        schedules = [];
        fetchRoom(selectedCinemaId).then(function (result) {
          var select = $("#room");
          select.empty();
          select.append("<option selected value='0'>Chọn Phòng Chiếu Phim</option>");

          result.forEach(function (room) {
            select.append(
              $("<option>", {
                value: room.id,
                text: room.name,
              })
            );
          });

          // Gán sự kiện change cho phòng chiếu phim
          $("#room").off("change").on("change", function () {
            var selectedRoomId = $(this).val();
            if (selectedRoomId !== "0") {
              calendars = removeCalendar(calendars); // Xóa các lịch hiện có
              schedules = [];
              renderCalendar(calendars); // Tạo mới lịch
            } else {
              calendars = removeCalendar(calendars); // Xóa lịch nếu không chọn phòng
              schedules = [];
            }
          });
        });
      } else {
        var selectElement = document.getElementById("room");
        selectElement.innerHTML = "<option selected value='0'>Chọn Phòng Chiếu Phim</option>";
        calendars = removeCalendar(calendars); // Xóa lịch nếu không chọn rạp
        schedules = [];
      }
    });

  });
  getMovie(); // Lấy danh sách phim
  $("#movie-search").on("input", function () {
    const query = $(this).val().toLowerCase();
    filterMovies(query);
  });
});

function initializeDraggable() {
  var Calendar = FullCalendar.Calendar;
  var Draggable = FullCalendar.Draggable;
  var containerEl = document.getElementById("external-events");

  // Kiểm tra nếu Draggable đã được khởi tạo
  if (draggable) {
    draggable.destroy();
  }

  draggable = new Draggable(containerEl, {
    itemSelector: ".fc-event",
    eventData: function (eventEl) {
      return {
        title: eventEl.innerText,
        movieId: eventEl.getAttribute("id"),
        backgroundImage: eventEl.getAttribute("data-background-image"),
        itemId: 'item' + parseInt(eventEl.getAttribute("id").replace(/\D/g, ""), 10),
        author: eventEl.getAttribute("data-author"),
        extendedProps: {
          duration: eventEl.getAttribute("data-duration"),
          movieId: eventEl.getAttribute("id")
        },
      };
    },
  });
}

function filterMovies(query) {
  $("#movie-block div").each(function () {
    const movieName = $(this).text().toLowerCase();
    $(this).toggle(movieName.includes(query));
  });
}

function removeCalendar(calendars) {
  calendars.forEach(function (cal) {
    if (cal && typeof cal.destroy === "function") {
      cal.destroy();
    }
  });
  document.getElementById("calendar").innerHTML = "";
  calendars.length = 0;
  return calendars;
}

function toUTC(dateStr) {
  return new Date(dateStr).toISOString();
}

function renderCalendar(calendars) {
  var Calendar = FullCalendar.Calendar;
  var isHourView = false;
  // var count = 0;
  // var Draggable = FullCalendar.Draggable;
  // var containerEl = document.getElementById("external-events");
  // new Draggable(containerEl, {
  //   itemSelector: ".fc-event",
  //   eventData: function (eventEl) {
  //     return {
  //       title: eventEl.innerText,
  //       movieId: eventEl.getAttribute("id"),
  //       backgroundImage: eventEl.getAttribute("data-background-image"),
  //       itemId: count++,
  //       author: eventEl.getAttribute("data-author"),
  //       extendedProps: {
  //         duration: eventEl.getAttribute("data-duration"),
  //         movieId: eventEl.getAttribute("id")
  //       },
  //     };
  //   },
  // });
  var calendarEl = $("#calendar")[0];
  var calendar = new Calendar(calendarEl, {
    timeZone: "local",
    themeSystem: "bootstrap5",
    initialView: "timeGridWeek",
    nowIndicator: true,
    headerToolbar: {
      start: "title",
      end: "toggleSlotButton myCustomButton today timeGridWeek,timeGridDay prev,next",
    },
    titleFormat: {
      year: "numeric",
      month: "long",
      day: "numeric",
    },
    buttonText: {
      today: "Lịch chiếu ngày hôm nay",
      timeGridWeek: "Tuần",
      timeGridDay: "Ngày",
    },
    customButtons: {
      myCustomButton: {
        text: "Lưu lịch chiếu mới sắp xếp",
        click: function () {
          saveSchedule(schedules);
        },
      },
      toggleSlotButton: {
        text: "View",
        click: function () {
          isHourView = !isHourView;
          var newSlotDuration = isHourView ? "01:00:00" : "00:15:00";
          var newSlotLabelInterval = isHourView ? "01:00:00" : "00:15:00";
          var newContentHeight = isHourView ? 540 : 2000;
          calendar.setOption('slotDuration', newSlotDuration);
          calendar.setOption('slotLabelInterval', newSlotLabelInterval);
          calendar.setOption('contentHeight', newContentHeight);

          if (isHourView) {
            calendarEl.classList.add('hour-view');
          } else {
            calendarEl.classList.remove('hour-view');
          }
          calendar.render();
        }
      },
    },
    contentHeight: 2000,
    slotDuration: "00:15:00",
    slotLabelInterval: "00:15:00",
    slotLabelFormat: {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    },
    slotMinTime: "00:00:00",
    slotMaxTime: "24:00:00",
    allDaySlot: false,
    editable: true,
    eventStartEditable: true,
    eventDurationEditable: true,
    eventOverlap: false,
    longPressDelay: 800,
    droppable: true,
    eventReceive: function (info) {
      const eventDateStr = info.event.startStr.split("T")[0];
      const eventDateTimeStr = new Date(info.event.startStr).toISOString();
      const $dayEl = $(`[data-date="${eventDateStr}"]`);

      const todayDate = getTodayDateTime();
      if (eventDateTimeStr < todayDate) {
        info.revert();
        toastr.warning("Không thể đặt lịch trong quá khứ!");
        return;
      }
      if ($dayEl.hasClass("no-interaction")) {
        info.revert();
      } else {
        const movieId = info.event.extendedProps.movieId;
        const movieName = info.event.title;
        const startTime = info.event.startStr;
        const duration = parseInt(info.event.extendedProps.duration, 10); // Thời gian phim
        const endTime = new Date(new Date(startTime).getTime() + duration * 60000).toISOString(); // Tính thời gian kết thúc
        const roomId = $("#room").val(); // Lấy roomId hiện tại
        const cinemaId = $("#cinema").val(); // Lấy roomId hiện tại

        // Kiểm tra sự kiện có tồn tại trong schedules chưa theo roomId
        const exists = schedules.some(schedule =>
          schedule.movieId === movieId &&
          schedule.start === startTime &&
          schedule.roomId === roomId &&
          schedule.cinemaId === cinemaId
        );

        if (!exists) {
          schedules.push({
            itemId: info.event.extendedProps.itemId,
            movieId: movieId,
            movieName: movieName,
            start: startTime,
            end: endTime,
            roomId: roomId, // Lưu roomId vào sự kiện
            cinemaId: cinemaId, // Lưu roomId vào sự kiện
            backgroundImage: info.event.extendedProps.backgroundImage,
          });
          // Cập nhật thời gian và chiều rộng cho sự kiện
          info.event.setEnd(endTime); // Cập nhật thời gian kết thúc cho sự kiện
        } else {
          info.revert(); // Nếu đã tồn tại thì hoàn tác hành động
        }
        console.log(schedules);
      }
    },
    eventDidMount: function (info) {
      const todayDateTime = getTodayDateTime();
      const eventStartDateTime = new Date(info.event.start).toISOString();

      // Kiểm tra nếu sự kiện đã qua
      if (eventStartDateTime < todayDateTime) {
        $(info.el).css("background-color", "red");
        $(info.el).addClass('no-interaction');
      }
      const old = info.event.extendedProps.old;
      if (old) {
        var backgroundImage = info.event.extendedProps.movieSmallImageUrl;
        if (backgroundImage) {
          let imgElement = $("<img>", {
            src: info.event.extendedProps.movieSmallImageUrl,
            alt: "Movie Image",
            css: {
              width: '100%',
              height: 'auto',
              maxHeight: '100%',
              objectFit: 'cover',
              display: "block",
              position: "absolute",
              zIndex: 10,
              top: "0",
              left: "0",
            }
          });
          $(info.el).append(imgElement);
          // Thêm tên phim và thời gian
          const title = info.event.title;
          const startTime = info.event.start.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
          const endTime = info.event.end.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
          // const textDiv = $("<div>", {
          //   class: "event-text",
          //   html: `<div class="event-title">${title}</div><div class="event-time">${startTime} - ${endTime}</div>`,
          //   css: {
          //     position: "absolute",
          //     top: "10px",
          //     left: "10px",
          //     color: "white",
          //     backgroundColor: "rgba(0, 0, 0, 0.7)",
          //     padding: "5px",
          //     borderRadius: "5px",
          //     zIndex: 20
          //   }
          // });

          // $(info.el).append(textDiv);
        }
      } else {
        // Lấy URL ảnh nền từ extendedProps
        var backgroundImage = info.event.extendedProps.backgroundImage;
        var itemId = info.event.extendedProps.itemId;
        var duration = info.event.extendedProps.duration;
        var formattedDuration;
        if (duration) {
          var hours = Math.floor(duration / 60); // Lấy phần giờ
          var minutes = duration % 60; // Lấy phần phút còn lại
          formattedDuration =
            hours > 0 ? `${hours}h ${minutes} phút` : `${minutes} phút`;
        }
        var author = info.event.extendedProps.author;

        // Tạo một phần tử chứa ảnh nền cho văn bản
        let backgroundDiv = $(
          `<span id='remove-${itemId}' class="remove-event"> Hủy</span>`
        );

        // $(info.el).find(".fc-event-title")
        //   .after(< div class= "text-white" > Thời lượng: ${ duration }' ~ ${formattedDuration}</div>
        //           <div class="text-white">Tác giả: ${author}</div>);
        // Chèn phần tử nền vào trong phần tử sự kiện
        $(info.el).prepend(backgroundDiv);
        $(document).on("click", `#remove-${itemId}`, function (event) {
          info.event.remove();
          schedules = schedules.filter(function (schedule) {
            return schedule.itemId !== itemId;
          });
        });
      }
    },
    eventDrop: function (info) {
      const isOldEvent = info.event.extendedProps.old;

      if (isOldEvent) {
        Swal.fire({
          title: 'Phim đã lên suất chiếu. Bạn có chắc muốn thay đổi không?',
          text: "Thay đổi này sẽ ảnh hưởng đến lịch chiếu!",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Tiếp tục',
          cancelButtonText: 'Hủy'
        }).then((result) => {
          if (result.isConfirmed) {
            // Nếu xác nhận, cập nhật lịch chiếu
            const itemId = info.event.extendedProps.itemId;
            const updatedStart = info.event.startStr;
            const updatedEnd = info.event.endStr;
            const eventDateTimeStr = new Date(info.event.startStr).toISOString();
            const todayDate = getTodayDateTime();

            if (eventDateTimeStr < todayDate) {
              info.revert();
              toastr.warning("Không thể đặt lịch trong quá khứ!");
              return;
            }

            schedules = schedules.map(schedule => {
              if (schedule.itemId === itemId) {
                return {
                  ...schedule,
                  start: updatedStart,
                  end: updatedEnd,
                };
              }
              return schedule;
            });
          } else {
            // Nếu không xác nhận, hoàn tác thay đổi
            info.revert();
          }
        });
      } else {
        // Nếu không phải sự kiện đã lưu, cho phép di chuyển
        const itemId = info.event.extendedProps.itemId;
        const updatedStart = info.event.startStr;
        const updatedEnd = info.event.endStr;
        const eventDateTimeStr = new Date(info.event.startStr).toISOString();
        const todayDate = getTodayDateTime();

        if (eventDateTimeStr < todayDate) {
          info.revert();
          toastr.warning("Không thể đặt lịch trong quá khứ!");
          return;
        }

        schedules = schedules.map(schedule => {
          if (schedule.itemId === itemId) {
            return {
              ...schedule,
              start: updatedStart,
              end: updatedEnd,
            };
          }
          return schedule;
        });
      }
    },
    eventChange: function (info) {
      var itemId = info.event.extendedProps.itemId;
      var updatedStart = info.event.startStr;
      var updatedEnd = info.event.endStr;

      // Cập nhật thông tin sự kiện trong mảng schedules
      schedules = schedules.map(function (schedule) {
        if (schedule.itemId === itemId) {
          return {
            ...schedule,
            start: updatedStart,
            end: updatedEnd,
          };
        }
        return schedule;
      });
    },
    datesSet: function (info) {
      const isTimeGridWeek = info.view.type === 'timeGridWeek';
      $(".fc-event").each(function () {
        const imgElement = $(this).find("img");
        if (imgElement.length) {
          imgElement.css({
            width: isTimeGridWeek ? '100%' : 'auto',
            height: isTimeGridWeek ? 'auto' : '100%',
            maxHeight: isTimeGridWeek ? '200px' : 'none',
            objectFit: 'cover',
          });
        }
      });
      // Khi các ngày được thiết lập, kiểm tra và cập nhật CSS
      const viewStart = info.view.activeStart;
      const viewEnd = info.view.activeEnd;
      // Xử lý các ngày trong view
      let currentDate = new Date(viewStart);
      while (currentDate <= viewEnd) {
        const currentDateStr = currentDate.toISOString().split("T")[0];
        currentDate.setDate(currentDate.getDate() + 1);
      }
    },
    nextDayThreshold: "00:00:00",
    forceEventDuration: true,
    eventTimeFormat: {
      // Định dạng thời gian của sự kiện
      hour: "2-digit",
      minute: "2-digit",
      hour12: true,
    },
  });

  var roomId = $("#room").val();
  fetchOldSchedules(roomId)
    .done(function (schedulesData) {
      const events = schedulesData.map((schedule) => {
        const startDateTime = `${schedule.date}T${schedule.time}+07:00`;
        const endDateTime = `${schedule.date}T${schedule.timeEnd}+07:00`;
        const todayDate = getTodayDate();
        // Thêm vào mảng schedules
        schedules.push({
          itemId: schedule.scheduleId,
          movieId: 'movie' + schedule.movieId,
          movieName: schedule.movieName,
          start: startDateTime,
          end: endDateTime,
          roomId: roomId,
          cinemaId: $("#cinema").val(),
          backgroundImage: schedule.movieSmallImageUrl,
        });

        console.log(schedules);

        return {
          title: schedule.movieName,
          start: startDateTime,
          end: endDateTime,
          extendedProps: {
            movieSmallImageUrl: schedule.movieSmallImageUrl,
            old: true,
            itemId: schedule.scheduleId,
          },
        };
      });
      calendar.addEventSource(events);
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
      console.error("Error fetching schedules:", textStatus, errorThrown);
    });

  calendar.render();
  calendars.push(calendar);
}

function fetchOldSchedules(roomId) {
  return $.ajax({
    url: "http://localhost:8080/api/pub/schedule/" + roomId,
    method: "GET",
    dataType: "json",
    contentType: "application/json",
  });
}
function saveSchedule(schedules) {
  console.log("Starting saveSchedule with:", schedules);
  var cinemaId = $("#cinema").val();
  var roomId = $("#room").val();

  // Kiểm tra đầu vào
  if (cinemaId === "0" || cinemaId === undefined) {
    toastr.error("Chọn rạp chiếu phim");
    return;
  }
  if (roomId === "0" || roomId === undefined) {
    toastr.error("Chọn phòng chiếu phim");
    return;
  }
  if (schedules.length === 0) {
    toastr.error("Lịch chiếu phim trống. Vui lòng thêm ít nhất một lịch chiếu.");
    return;
  }

  // Kiểm tra thời gian kết thúc
  var hasEmptyEndTime = schedules.some(schedule => !schedule.end || schedule.end === "");
  if (hasEmptyEndTime) {
    toastr.error("Có lịch chiếu chưa thiết lập thời gian kết thúc.");
    return;
  }

  var requests = [];
  var newSchedules = [];
  var hasError = false; // Flag để theo dõi lỗi

  schedules.forEach(function (schedule) {
    const startIsoString = schedule.start;
    const startDatePart = startIsoString.split("T")[0];
    const startTimePart = startIsoString.split("T")[1].slice(0, 8);
    const endIsoString = schedule.end;
    const endTimePart = endIsoString.split("T")[1].slice(0, 8);

    var scheduleData = {
      scheduleId: schedule.itemId,
      roomId: parseInt(roomId, 10),
      movieId: parseInt(schedule.movieId.replace(/\D/g, ""), 10),
      date: startDatePart,
      time: startTimePart,
      timeEnd: endTimePart,
    };

    var request = $.ajax({
      url: "http://localhost:8080/api/pub/schedule",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(scheduleData),
    });

    requests.push(request);

    request.done(function (response) {
      if (!newSchedules.includes(response)) {
        newSchedules.push(response);
      }
    });

    request.fail(function (xhr) {
      hasError = true; // Đánh dấu có lỗi
      const response = JSON.parse(xhr.responseText);
      toastr.error(response.message);

      var calendar = calendars[0];
      calendar.removeAllEvents();

      schedules.forEach(function (schedule) {
        calendar.addEvent({
          title: schedule.movieName,
          start: schedule.start,
          end: schedule.end,
          extendedProps: {
            movieSmallImageUrl: schedule.backgroundImage,
            old: true,
            itemId: schedule.itemId,
          }
        });
      });
      calendar.render();

    });
  });
  var requests2 = [];
  $.when.apply($, requests).done(function () {
    if (!hasError) { // Chỉ thông báo thành công nếu không có lỗi
      newSchedules.forEach(function (schedule) {
        const scheduleId = schedule;
        const roomIdINT = parseInt(roomId, 10);

        var request = $.ajax({
          url: "http://localhost:8080/api/pub/schedule/showtime",
          method: "POST",
          contentType: "application/json",
          data: JSON.stringify({
            roomId: roomIdINT,
            scheduleId: scheduleId,
          }),
        });

        requests2.push(request);
        request.done(function (response) { });
        request.fail(function (xhr) { });
      });
    } else {
      toastr.error("Có lỗi trong quá trình cập nhật lịch chiếu phim.");
    }
  });

  $.when.apply($, requests2).done(function () {
    if (!hasError) {
      $(".background").hide();
      toastr.success("Cập nhật lịch chiếu phim thành công");

      var calendar = calendars[0];
      calendar.removeAllEvents();

      schedules.forEach(function (schedule) {
        calendar.addEvent({
          title: schedule.movieName,
          start: schedule.start,
          end: schedule.end,
          extendedProps: {
            movieSmallImageUrl: schedule.backgroundImage,
            old: true,
            itemId: schedule.itemId,
          }
        });
      });

      calendar.render();
    }
  });
}
function getTodayDateTime() {
  return new Date().toISOString();
}

function getTodayDate() {
  const today = new Date();
  return today.toLocaleDateString("en-CA");
}
function getMovie() {
  $.ajax({
    url: "http://localhost:8080/api/pub/movie/isShowing",
    type: "GET",
    dataType: "json",
    success: function (movies) {
      const movieBlock = $("#movie-block");
      movieBlock.empty(); // Xóa nội dung hiện tại
      movies.forEach((movie) => {
        const eventDiv = $("<div>", {
          id: "movie" + movie.id,
          dataId: movie.id,
          class:
            "fc-event fc-h-event fc-daygrid-event fc-daygrid-block-event mb-1",
          "data-background-image": movie.imageSmallUrl,
          "data-duration": movie.duration,
          "data-author": movie.author,
        });

        const eventMainDiv = $("<div>", {
          class: "fc-event-main",
          text: movie.name,
        });

        eventDiv.append(eventMainDiv);
        movieBlock.append(eventDiv);
      });
      initializeDraggable(); // Khởi tạo lại draggable sau khi tải xong danh sách phim
    },
    error: function (xhr, status, error) {
      console.error("Error fetching movies:", error);
    },
  });
}

function fetchCinema() {
  return $.ajax({
    url: "http://localhost:8080/api/pub/cinema/active",
    type: "GET",
    dataType: "json",
    success: function (data) { },
    error: function (xhr, status, error) {
      console.error("Error fetching cinemas:", error);
    },
  }).then(function (data) {
    return data;
  });
}

// Hàm thứ hai lấy thông tin phòng
function fetchRoom(cinemaId) {
  return $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/rooms/active",
    type: "GET",
    dataType: "json",
    success: function (data) { },
    error: function (xhr, status, error) { },
  }).then(function (data) {
    return data;
  });
}

