$(document).ready(function () {
  $(".menu-admin").load("/menu", function () {
    $.getScript("/js/admin/menu.js");
  });
  const cinemaId = getQueryParam("cinema-id");
  const roomId = getQueryParam("room-id");
  let cinema;
  let room;
  $(".background").show();
  fetchCinema(cinemaId)
    .then(function (result) {
      cinema = result;
      return fetchRoom(cinemaId, roomId);
    })
    .then(function (result) {
      room = result;
      console.log(result);
      $("#room-name").text(room.name || "N/A");
      $("#room-location").text(room.location || "N/A");
      $("#room-screen-size").text(
        room.screenSize === "SMALL"
          ? "Nhỏ"
          : (room.screenSize === "MEDIUM" ? "Trung Bình" : "Lớn") || "N/A"
      );
      $("#room-type").text(
        room.roomType === "THREE-D"
          ? "3D"
          : (room.roomType === "STANDARD" ? "2D" : "IMAX") || "N/A"
      );
      $("#room-status").text(
        room.roomStatus === "UNASSIGNED"
          ? "Chưa Đăng Ký"
          : (room.roomStatus === "AVAILABLE" ? "Có Sẵn" : "Đang Bảo Trì") ||
              "N/A"
      );
      if (room.roomStatus === "UNASSIGNED") {
        $("#type-seat")
          .append(`<div class="d-flex justify-content-center pt-3 pb-4">
                  <img style="height: 30px" src="/img/ghe-vip.png" alt="" />
                  <p class="text-white mb-0 ms-2 me-5 pt-1">Ghế VIP</p>
                  <img style="height: 30px" src="/img/ghe-thuong.png" alt="" />
                  <p class="text-white mb-0 ms-2 me-5 pt-1">Ghế Thường</p>
                  <img style="height: 30px" src="/img/ghe-doi.png" alt="" />
                  <p class="text-white mb-0 ms-2 pt-1">Ghế Đôi</p>
                </div>`);
        $("#room-display").closest(".row").css({ height: "900px" });
        var html = `<p class="text-light">Chưa có bố cục phòng chiếu phim</p>
                  <button
                    class="btn btn-primary"
                    data-bs-toggle="modal"
                    data-bs-target="#exampleModal"
                  >
                    Tạo Bố Cục Phòng Chiếu Phim
                  </button>`;
        $("#room-display").addClass(
          "d-flex flex-column justify-content-center align-items-center flex-grow-1"
        );
        $("#room-display").append(html);
        $(".background").hide();
        $("#create-room-btn").on("click", function (event) {
          $(".background").show();
          event.preventDefault();
          $("#room-display").closest(".row").css("height", "");
          drawScreen(room.screenSize.charAt(0).toUpperCase());
          let cols = parseInt($("#seat-column-numbers").val(), 10);
          let rows = parseInt($("#seat-row-numbers").val(), 10);
          let aislePosition = parseInt($("#aisle-position").val(), 10);
          let aisleWidth = parseInt($("#aisle-width").val(), 10);
          let aisleHeight = parseInt($("#aisle-height").val(), 10);
          let doubleSeatRows = parseInt(
            $("#double-seat-row-numbers").val(),
            10
          );
          drawSeat(
            rows,
            cols,
            aislePosition,
            aisleWidth,
            aisleHeight,
            doubleSeatRows
          );
          $("#exampleModal").modal("hide");
          $(
            "#room-display"
          ).append(`<div class="row d-flex justify-content-end align-items-center gap-2 flex-grow-2 my-2"><button
                    hidden
                    class="customize-btn btn-primary rounded-circle"
                    data-bs-toggle="modal"
                    data-bs-target="#exampleModal"
                    id="generate-btn"
                    title="Tạo lại"
                  >
                    <i class="bi bi-arrow-repeat"></i>
                  </button>

                  <button
                    hidden
                    class="customize-btn btn-success rounded-circle"
                    id="save-room-btn"
                    title="Lưu"
                  >
                    <i class="bi bi-plus-circle"></i>
                  </button>
                  
                  </div>`);
          $("#room-display")
            .closest(".row")
            .find("button")
            .removeAttr("hidden");
          displaySeat();
          $(".background").hide();
        });
        $(document).on("click", "#save-room-btn", function (event) {
          $(".background").show();
          let cols = parseInt($("#seat-column-numbers").val(), 10);
          let rows = parseInt($("#seat-row-numbers").val(), 10);
          let aislePosition = parseInt($("#aisle-position").val(), 10);
          let aisleWidth = parseInt($("#aisle-width").val(), 10);
          let aisleHeight = parseInt($("#aisle-height").val(), 10);
          let doubleSeatRows = parseInt(
            $("#double-seat-row-numbers").val(),
            10
          );
          saveLayout(
            cinema.id,
            room.id,
            cols,
            rows,
            aislePosition,
            aisleWidth,
            aisleHeight,
            doubleSeatRows,
            30,
            event
          );
          event.preventDefault();
          var requests = [];
          $("td").each(function () {
            var status = $(this).attr("data-status");
            var cellId = $(this).attr("id");
            if (status !== "aisle" && cellId !== undefined) {
              if (status === "normal") {
                status = "NORMAL";
              }
              if (status === "double") {
                status = "DOUBLE";
              }
              if (status === "VIP") {
                status = "VIP";
              }
              var cellIdArr = cellId.split(",");
              var request = $.ajax({
                url: "http://localhost:8080/api/pub/seat",
                type: "POST",
                data: JSON.stringify({
                  rowName: cellIdArr[0],
                  columnName: cellIdArr[1],
                }),
                contentType: "application/json",
                dataType: "json",
              })
                .done(function (data) {
                  room.roomSeats.push({
                    id: data.id,
                    typeSeat: status,
                  });
                })
                .fail(function (xhr, status, error) {});
              requests.push(request);
            }
          });
          $.when.apply($, requests).then(function () {
            $.ajax({
              url: "http://localhost:8080/api/pub/room/" + room.id,
              type: "POST",
              data: JSON.stringify({
                id: room.id,
                roomSeats: room.roomSeats,
              }),
              contentType: "application/json",
              dataType: "json",
              success: function () {
                activeItem(cinema.id, room.id, event);
                window.location.href = `/room-manager?cinema-id=${encodeURIComponent(
                  cinemaId
                )}`;
              },
              error: function (xhr, status, error) {},
            });
          });
        });
      } else if (room.roomStatus === "AVAILABLE") {
        $("#type-seat")
          .append(`<div class="d-flex justify-content-center pt-3 pb-4">
                  <img style="height: 30px" src="/img/ghe-vip.png" alt="" />
                  <p class="text-white mb-0 ms-2 me-5 pt-1">Ghế VIP</p>
                  <img style="height: 30px" src="/img/ghe-thuong.png" alt="" />
                  <p class="text-white mb-0 ms-2 me-5 pt-1">Ghế Thường</p>
                  <img style="height: 30px" src="/img/ghe-da-dat.png" alt="" />
                  <p class="text-white mb-0 ms-2 me-5 pt-1">Ghế Không Sử Dụng Được</p>
                  <img style="height: 30px" src="/img/ghe-doi.png" alt="" />
                  <p class="text-white mb-0 ms-2 pt-1">Ghế Đôi</p>
                </div>`);
        $("#room-display").closest(".row").css({ height: "" });
        $("#room-display").addClass(
          "d-flex flex-column justify-content-center align-items-center flex-grow-1"
        );
        drawScreen(room.screenSize.charAt(0).toUpperCase());
        fetchRoomLayout(room.id)
          .then(function () {
            displaySeat();
            $(
              "#room-display"
            ).append(`<div class="row d-flex justify-content-end align-items-center gap-2 flex-grow-2 my-2"><button
              hidden
              class="btn btn-success text-white"
              id="update-btn"
              title="Update"
            >
              Lưu
            </button>   
            <button
            hidden
              class="btn btn-primary text-white"
              data-bs-toggle="modal"
              data-bs-target="#fixModal"
              id="fix-btn"
              title="Update">
              Sửa
            </button>            
            </div>`);
            $("#room-display")
              .closest(".row")
              .find("button")
              .removeAttr("hidden");
            $("td").each(function () {
              var cellId = $(this).attr("id");
              var cell = $(this);
              if (cellId !== undefined) {
                var cellIdArr = cellId.split(",");
                var rowName = String(cellIdArr[0]);
                var colName = String(cellIdArr[1]);
                $.ajax({
                  url: `http://localhost:8080/api/pub/room/${roomId}/seatStatus?rowName=${encodeURIComponent(
                    rowName
                  )}&colName=${encodeURIComponent(colName)}`,
                  type: "GET",
                  dataType: "json",
                  success: function (data) {
                    var $img = cell.find("img");
                    var newStatus = data.typeSeat;
                    if (newStatus === "VIP") {
                      cell.attr("data-status", "VIP");
                      cell.attr("data-room-id", data.srId.roomId);
                      cell.attr("data-seat-id", data.srId.seatId);
                      $img
                        .attr("src", "/img/ghe-vip.png")
                        .attr("alt", "VIP Seat");
                    } else if (newStatus === "BROKEN") {
                      cell.off();
                      cell.attr("data-status", "disabled");
                      cell.attr("data-room-id", data.srId.roomId);
                      cell.attr("data-seat-id", data.srId.seatId);
                      $img
                        .attr("src", "/img/ghe-da-dat.png")
                        .attr("alt", "Dis Seat");
                    } else if (newStatus === "NORMAL") {
                      cell.attr("data-status", "normal");
                      cell.attr("data-room-id", data.srId.roomId);
                      cell.attr("data-seat-id", data.srId.seatId);
                    } else {
                      cell.attr("data-status", "double");
                      cell.attr("data-room-id", data.srId.roomId);
                      cell.attr("data-seat-id", data.srId.seatId);
                    }
                  },
                  error: function (xhr) {},
                });
              }
            });
            $(".background").hide();
          })
          .then(function () {
            $("#update-btn").on("click", function () {
              $(".background").show();
              var requests = [];
              $("td").each(function () {
                var cell = $(this);
                var cellId = cell.attr("id");
                if (
                  cell.attr("data-status") === "disabled" &&
                  cellId !== undefined
                ) {
                  var cellRoomID = cell.attr("data-room-id");
                  var cellSeatID = cell.attr("data-seat-id");
                  var request = $.ajax({
                    url: `http://localhost:8080/api/pub/room/${roomId}/updateSeatStatus`,
                    type: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify({
                      id: cellSeatID,
                      typeSeat: "BROKEN",
                    }),
                  });
                  requests.push(request);
                  request.done(function (response) {
                    cell.off();
                    cell.attr("data-status", "disabled");
                    cell.attr("data-room-id", response.srId.roomId);
                    cell.attr("data-seat-id", response.srId.seatId);
                  });

                  request.fail(function (xhr) {});
                }
              });
              $.when.apply($, requests).done(function () {
                $(".background").hide();
                toastr.success("Cập nhật tất cả các ghế thành công");
              });
            });
            $("#fix-btn").on("click", function () {
              $("#seat-status option[value='VIP']").prop("disabled", false);
              $("#seat-status option[value='NORMAL']").prop("disabled", false);
              $("#seat-status option[value='DOUBLE']").prop("disabled", false);
              var disabledSeat = [];
              $("td").each(function () {
                var cell = $(this);
                var cellId = cell.attr("id");
                if (
                  cell.attr("data-status") === "disabled" &&
                  cellId !== undefined
                ) {
                  disabledSeat.push(cellId);
                }
              });
              var $select = $("#disabled-seats");
              $select.empty();
              $select.append("<option selected>Chọn ghế</option>");
              disabledSeat.forEach(function (seatId) {
                var [rowName, colName] = seatId.split(",");
                $select.append(
                  `<option value="${seatId}">${rowName}, ${colName}</option>`
                );
              });
              $("#disabled-seats").on("change", function () {
                var selectedSeatId = $(this).val();
                if (selectedSeatId && selectedSeatId !== "Chọn ghế") {
                  var cell = $(`td[id='${selectedSeatId}']`);
                  var preStatus = cell.attr("data-status-pre");
                  if (preStatus === "double") {
                    $("#seat-status option[value='VIP']").prop(
                      "disabled",
                      true
                    );
                    $("#seat-status option[value='NORMAL']").prop(
                      "disabled",
                      true
                    );
                  } else {
                    $("#seat-status option[value='DOUBLE']").prop(
                      "disabled",
                      true
                    );
                  }
                }
              });
            });
            $(document).on("click", "#edit-seat-btn", function () {
              $(".background").show();
              var selectedSeatId = $("#disabled-seats").val();
              var selectedTypeSeat = $("#seat-status").val();
              if (selectedSeatId && selectedSeatId !== "Chọn ghế") {
              }
              var cell = $(`td[id='${selectedSeatId}']`);
              var cellRoomID = cell.attr("data-room-id");
              var cellSeatID = cell.attr("data-seat-id");
              console.log(cell);
              $.ajax({
                url: `http://localhost:8080/api/pub/room/${roomId}/updateSeatStatus`,
                type: "PUT",
                contentType: "application/json",
                data: JSON.stringify({
                  id: cellSeatID,
                  typeSeat: selectedTypeSeat,
                }),
                success: function (response) {
                  var $img = cell.find("img");
                  var newStatus = response.typeSeat;
                  if (newStatus === "VIP") {
                    cell.attr("data-status", "VIP");
                    cell.attr("data-room-id", response.srId.roomId);
                    cell.attr("data-seat-id", response.srId.seatId);
                    $img
                      .attr("src", "/img/ghe-vip.png")
                      .attr("alt", "VIP Seat");
                  } else if (newStatus === "NORMAL") {
                    cell.attr("data-status", "normal");
                    cell.attr("data-room-id", response.srId.roomId);
                    cell.attr("data-seat-id", response.srId.seatId);
                    $img
                      .attr("src", "/img/ghe-thuong.png")
                      .attr("alt", "Normal Seat");
                  } else if (newStatus === "DOUBLE") {
                    cell.attr("data-status", "double");
                    cell.attr("data-room-id", response.srId.roomId);
                    cell.attr("data-seat-id", response.srId.seatId);
                    $img
                      .attr("src", "/img/ghe-doi.png")
                      .attr("alt", "Double Seat");
                  }
                  cell.on("click", function (event) {
                    event.preventDefault();
                    var $this = $(this);
                    var $img = $this.find("img");
                    var currentStatus = $this.attr("data-status");
                    var preStatus = $this.attr("data-status-pre");
                    if (currentStatus === "normal") {
                      $this.attr("data-status", "disabled");
                      $this.attr("data-status-pre", "normal");
                      $img
                        .attr("src", "/img/ghe-da-dat.png")
                        .attr("alt", "Dis Seat");
                    } else if (currentStatus === "VIP") {
                      $this.attr("data-status", "disabled");
                      $this.attr("data-status-pre", "VIP");
                      $img
                        .attr("src", "/img/ghe-da-dat.png")
                        .attr("alt", "Dis");
                    } else if (currentStatus === "disabled") {
                      $this.attr("data-status", preStatus);
                      if (preStatus === "normal") {
                        $img
                          .attr("src", "/img/ghe-thuong.png")
                          .attr("alt", "Dis");
                      } else if (preStatus === "VIP") {
                        $img.attr("src", "/img/ghe-vip.png").attr("alt", "Dis");
                      } else {
                        $this.attr("data-status-pre", "normal");
                        $img
                          .attr("src", "/img/ghe-da-dat.png")
                          .attr("alt", "Dis Seat");
                      }
                    }
                  });
                  $("#fixModal").modal("hide");
                  $(".background").hide();
                  toastr.success("Sửa ghế thành công");
                },
                error: function (xhr) {},
              });
            });
          });
      }
    });
});
function fetchRoomLayout(roomId) {
  let cols;
  let rows;
  let aislePosition;
  let aisleWidth;
  let aisleHeight;
  let doubleSeatRows;
  let url = "http://localhost:8080/api/pub/room/" + roomId + "/layout";
  return $.ajax({
    url: url,
    type: "GET",
    contentType: "application/json",
    dataType: "json",
    success: function (data) {
      cols = data.seatColumnNumbers;
      rows = data.seatRowNumbers;
      aislePosition = data.aislePosition;
      aisleWidth = data.aisleWidth;
      aisleHeight = data.aisleHeight;
      doubleSeatRows = data.doubleSeatRowNumbers;

      loadSeat(
        rows,
        cols,
        aislePosition,
        aisleWidth,
        aisleHeight,
        doubleSeatRows
      );
    },
    error: function (xhr) {},
  });
}
function saveLayout(
  cinemaId,
  roomId,
  cols,
  rows,
  aislePosition,
  aisleWidth,
  aisleHeight,
  doubleSeatRows,
  numberOfSeats,
  event
) {
  console.log(
    rows,
    cols,
    aislePosition,
    aisleWidth,
    aisleHeight,
    doubleSeatRows
  );
  event.preventDefault();
  let url =
    "http://localhost:8080/api/pub/cinema/" +
    cinemaId +
    "/" +
    roomId +
    "/layout";
  $.ajax({
    url: url,
    type: "PUT",
    data: JSON.stringify({
      seatNumbers: numberOfSeats,
      seatRowNumbers: rows,
      seatColumnNumbers: cols,
      aislePosition: aislePosition,
      aisleWidth: aisleWidth,
      aisleHeight: aisleHeight,
      doubleSeatRowNumbers: doubleSeatRows,
    }),
    contentType: "application/json",
    dataType: "json",
    success: function (data) {
      room = data;
    },
    error: function (xhr) {},
  });
}
function validateForm() {
  let isValid = true;

  // Lấy tất cả các trường input có thuộc tính aria-describedby
  $("input[aria-describedby]").each(function () {
    let input = $(this);
    let validationMessage = $("#" + input.attr("aria-describedby"));

    if (input.val().trim() === "") {
      validationMessage.text(
        "Vui lòng nhập " + input.attr("placeholder").toLowerCase()
      );
      isValid = false;
    } else {
      validationMessage.text(""); // Xóa thông báo lỗi nếu trường được điền
    }
  });
  return isValid;
}
function displaySeat() {
  var cols = $("tr").length - 1;
  $("tr").each(function (rowIndex) {
    if (rowIndex !== cols) {
      var rowLabel = String.fromCharCode(65 + rowIndex);
      var i = 0;
      $(this)
        .find("td")
        .slice(1)
        .each(function (colIndex) {
          var status = $(this).attr("data-status");
          if (status !== "aisle") {
            var colLabel = ++i;
            var cellId = rowLabel + "," + colLabel;
            $(this).attr("id", cellId);
          }
        });
    }
  });
}
function activeItem(cinemaId, id, event) {
  event.preventDefault();
  let url =
    "http://localhost:8080/api/pub/cinema/" + cinemaId + "/" + id + "/active";
  $.ajax({
    url: url,
    type: "PUT",
    dataType: "json",
    success: function () {
      $(".background").hide();
      toast.success("Tạo phòng chiếu phim thành công");
    },
    error: function (xhr, status, error) {
      console.error("Error fetching cinemas:", error);
    },
  });
}
function drawSeat(
  rows,
  cols,
  aislePosition,
  aisleWidth,
  aisleHeight,
  doubleSeatRows
) {
  console.log(
    rows,
    cols,
    aislePosition,
    aisleWidth,
    aisleHeight,
    doubleSeatRows
  );
  const $container = $("#room-display");
  var table = $("<table>").addClass("table mt-5 pt-5");
  for (var i = 0; i < rows + doubleSeatRows; i++) {
    var row = $("<tr>");
    var labelCell = $("<td>").text(String.fromCharCode(65 + i));
    labelCell.addClass("text-white");
    labelCell.css({
      width: "70px",
      height: "70px",
      textAlign: "center",
      verticalAlign: "middle",
    });
    row.append(labelCell);
    if (i < rows) {
      for (var j = 0; j < cols; j++) {
        var cell = $("<td>")
          .attr("data-status", "normal")
          .css({
            width: "60px",
            height: "60px",
            textAlign: "center",
            verticalAlign: "middle",
            padding: "5px",
          })
          .on("click", function (event) {
            event.preventDefault();
            var $this = $(this);
            var $img = $this.find("img");
            var currentStatus = $this.attr("data-status");
            if (currentStatus === "normal") {
              $this.attr("data-status", "VIP");
              $img.attr("src", "/img/ghe-vip.png").attr("alt", "VIP Seat");
            } else if (currentStatus === "VIP") {
              $this.attr("data-status", "aisle");
              $img.attr("src", "/img/loi-di.png").attr("alt", "Aisle");
            } else if (currentStatus === "aisle") {
              $this.attr("data-status", "normal");
              $img
                .attr("src", "/img/ghe-thuong.png")
                .attr("alt", "Normal Seat");
            }
          });
        if (
          j >= aislePosition &&
          j < aislePosition + aisleWidth &&
          i + 1 <= aisleHeight
        ) {
          var img = $("<img>")
            .attr("src", "/img/loi-di.png")
            .attr("alt", "Image")
            .css({
              width: "100%", // Hình ảnh sẽ chiếm 100% chiều rộng của ô
              height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
              objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
              padding: "0",
            });
          cell.attr("data-status", "aisle");
          cell.append(img);
          row.append(cell);
        } else {
          var img = $("<img>")
            .attr("src", "/img/ghe-thuong.png")
            .attr("alt", "Image")
            .css({
              width: "100%", // Hình ảnh sẽ chiếm 100% chiều rộng của ô
              height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
              objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
              padding: "0",
            });
          cell.append(img);
          row.append(cell);
        }
      }
    } else {
      for (var j = 0; j < cols; j += 2) {
        var cell = $("<td>")
          .attr("data-status", "double")
          .attr("colspan", 2)
          .css({
            width: "40px",
            height: "40px",
            textAlign: "center",
            verticalAlign: "middle",
            padding: "5px",
          })
          .on("click", function (event) {
            event.preventDefault();
            var $this = $(this);
            var $img = $this.find("img");
            var currentStatus = $this.attr("data-status");
            if (currentStatus === "double") {
              $this.attr("data-status", "aisle");
              $img.attr("src", "/img/loi-di.png").attr("alt", "Aisle");
            } else if (currentStatus === "aisle") {
              $this.attr("data-status", "double");
              $img.attr("src", "/img/ghe-doi.png").attr("alt", "Double Seat");
            }
          });
        if (j + 1 >= cols) {
          break; // Nếu không đủ chỗ, ngừng lặp
        }
        var isAisle = false;
        for (var k = 0; k < 2; k++) {
          if (
            j + k >= aislePosition &&
            j + k < aislePosition + aisleWidth &&
            i + 1 <= aisleHeight
          ) {
            isAisle = true;
            break;
          }
        }
        if (isAisle) {
          var img = $("<img>")
            .attr("src", "/img/loi-di.png")
            .attr("alt", "Image")
            .css({
              width: "100%",
              height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
              objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
              padding: "0",
            });
          cell.attr("data-status", "aisle");
          cell.append(img);
          row.append(cell);
          continue;
        }
        var img = $("<img>")
          .attr("src", "/img/ghe-doi.png")
          .attr("alt", "Double Seat")
          .css({
            width: "100%", // Hình ảnh sẽ chiếm 100% chiều rộng của ô
            height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
            objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
            padding: "0",
          });
        cell.attr("data-status", "double");
        cell.append(img);
        row.append(cell);
      }
    }
    table.append(row);
  }

  var footerRow = $("<tr>");
  footerRow.append($("<td>"));
  var l = 0;
  for (var j = 0; j < cols; j++) {
    if (
      !(
        j >= aislePosition &&
        j < aislePosition + aisleWidth &&
        aisleHeight == rows + doubleSeatRows
      )
    ) {
      l++;
      var numberCell = $("<td>").text(l);
      numberCell.addClass("text-white");
      footerRow.append(numberCell);
    } else {
      footerRow.append($("<td>"));
    }
  }
  table.append(footerRow);
  $container.append(table);
}
function loadSeat(
  rows,
  cols,
  aislePosition,
  aisleWidth,
  aisleHeight,
  doubleSeatRows
) {
  console.log(
    rows,
    cols,
    aislePosition,
    aisleWidth,
    aisleHeight,
    doubleSeatRows
  );
  const $container = $("#room-display");
  var table = $("<table>").addClass("table mt-5 pt-5");
  for (var i = 0; i < rows + doubleSeatRows; i++) {
    var row = $("<tr>");
    var labelCell = $("<td>").text(String.fromCharCode(65 + i));
    labelCell.addClass("text-white");
    labelCell.css({
      width: "70px",
      height: "70px",
      textAlign: "center",
      verticalAlign: "middle",
    });
    row.append(labelCell);
    if (i < rows) {
      for (var j = 0; j < cols; j++) {
        var cell = $("<td>")
          .attr("data-status", "normal")
          .attr("data-status-pre", "")
          .css({
            width: "40px",
            height: "40px",
            textAlign: "center",
            verticalAlign: "middle",
            padding: "5px",
          })
          .on("click", function (event) {
            event.preventDefault();
            var $this = $(this);
            var $img = $this.find("img");
            var currentStatus = $this.attr("data-status");
            var preStatus = $this.attr("data-status-pre");
            if (currentStatus === "normal") {
              $this.attr("data-status", "disabled");
              $this.attr("data-status-pre", "normal");
              $img.attr("src", "/img/ghe-da-dat.png").attr("alt", "Dis Seat");
            } else if (currentStatus === "VIP") {
              $this.attr("data-status", "disabled");
              $this.attr("data-status-pre", "VIP");
              $img.attr("src", "/img/ghe-da-dat.png").attr("alt", "Dis");
            } else if (currentStatus === "disabled") {
              $this.attr("data-status", preStatus);
              if (preStatus === "normal") {
                $img.attr("src", "/img/ghe-thuong.png").attr("alt", "Dis");
              } else if (preStatus === "VIP") {
                $img.attr("src", "/img/ghe-vip.png").attr("alt", "Dis");
              } else {
                $this.attr("data-status-pre", "normal");
                $img.attr("src", "/img/ghe-da-dat.png").attr("alt", "Dis Seat");
              }
            }
          });
        if (
          j >= aislePosition &&
          j < aislePosition + aisleWidth &&
          i + 1 <= aisleHeight
        ) {
          var img = $("<img>")
            .attr("src", "/img/loi-di.png")
            .attr("alt", "Image")
            .css({
              width: "100%", // Hình ảnh sẽ chiếm 100% chiều rộng của ô
              height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
              objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
              padding: "0",
            });
          cell.attr("data-status", "aisle");
          cell.append(img);
          row.append(cell);
        } else {
          var img = $("<img>")
            .attr("src", "/img/ghe-thuong.png")
            .attr("alt", "Image")
            .css({
              width: "100%", // Hình ảnh sẽ chiếm 100% chiều rộng của ô
              height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
              objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
              padding: "0",
            });
          cell.append(img);
          row.append(cell);
        }
      }
    } else {
      for (var j = 0; j < cols; j += 2) {
        var cell = $("<td>")
          .attr("data-status", "double")
          .attr("data-status-pre", "double")
          .attr("colspan", 2)
          .css({
            width: "60px",
            height: "60px",
            textAlign: "center",
            verticalAlign: "middle",
            padding: "5px",
          })
          .on("click", function (event) {
            event.preventDefault();
            var $this = $(this);
            var $img = $this.find("img");
            var currentStatus = $this.attr("data-status");
            if (currentStatus === "double") {
              $this.attr("data-status", "disabled");
              $img.attr("src", "/img/ghe-da-dat.png").attr("alt", "VIP Seat");
            } else if (currentStatus === "disabled") {
              $this.attr("data-status", "double");
              $img.attr("src", "/img/ghe-doi.png").attr("alt", "Disabled Seat");
            }
          });
        if (j + 1 >= cols) {
          break; // Nếu không đủ chỗ, ngừng lặp
        }
        var isAisle = false;
        for (var k = 0; k < 2; k++) {
          if (
            j + k >= aislePosition &&
            j + k < aislePosition + aisleWidth &&
            i + 1 <= aisleHeight
          ) {
            isAisle = true;
            break;
          }
        }
        if (isAisle) {
          var img = $("<img>")
            .attr("src", "/img/loi-di.png")
            .attr("alt", "Image")
            .css({
              width: "100%",
              height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
              objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
              padding: "0",
            });
          cell.attr("data-status", "aisle");
          cell.append(img);
          row.append(cell);
          continue;
        }
        var img = $("<img>")
          .attr("src", "/img/ghe-doi.png")
          .attr("alt", "Double Seat")
          .css({
            width: "100%", // Hình ảnh sẽ chiếm 100% chiều rộng của ô
            height: "100%", // Hình ảnh sẽ chiếm 100% chiều cao của ô
            objectFit: "contain", // Giữ tỷ lệ của hình ảnh bên trong ô
            padding: "0",
          });
        cell.attr("data-status", "double");
        cell.append(img);
        row.append(cell);
      }
    }
    table.append(row);
  }

  var footerRow = $("<tr>");
  footerRow.append($("<td>"));
  var l = 0;
  for (var j = 0; j < cols; j++) {
    if (
      !(
        j >= aislePosition &&
        j < aislePosition + aisleWidth &&
        aisleHeight == rows + doubleSeatRows
      )
    ) {
      l++;
      var numberCell = $("<td>").text(l);
      numberCell.addClass("text-white");
      footerRow.append(numberCell);
    } else {
      footerRow.append($("<td>"));
    }
  }
  table.append(footerRow);
  $container.append(table);
}

function drawScreen(size) {
  const $container = $("#room-display");
  $container.empty().append(`<h5 class="text-white mt-5">SCREEN</h5>`);
  let screenClass = "";
  switch (size) {
    case "S":
      screenClass = "small";
      break;
    case "M":
      screenClass = "medium";
      break;
    case "L":
      screenClass = "large";
      break;
    default:
      console.error("Invalid size");
      return;
  }
  const $screenElement = $("<div></div>", {
    class: `screen ${screenClass} mb-5`,
  });
  $container.append($screenElement);
}

function fetchCinema(cinemaId) {
  return $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId,
    type: "GET",
    dataType: "json",
    success: function (data) {
      let html = ` > ${data.name}`;
      $("#title").append(html);
    },
    error: function (xhr, status, error) {
      console.error("Error fetching cinema:", error);
    },
  }).then(function (data) {
    return data;
  });
}

// Hàm thứ hai lấy thông tin phòng
function fetchRoom(cinemaId, roomId) {
  return $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/" + roomId,
    type: "GET",
    dataType: "json",
    success: function (data) {
      let html = ` > ${data.name}`;
      $("#title").append(html);
    },
    error: function (xhr, status, error) {
      console.error("Error fetching room:", error);
    },
  }).then(function (data) {
    return data;
  });
}

function getQueryParam(param) {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(param);
}
