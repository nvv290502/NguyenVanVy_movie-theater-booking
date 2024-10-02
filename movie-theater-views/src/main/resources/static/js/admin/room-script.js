$(document).ready(function () {
  $(".menu-admin").load("/menu", function () {
    $.getScript("/js/admin/menu.js");
  });
  const cinemaId = getQueryParam("cinema-id");
  $(".background").show();
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId,
    type: "GET",
    dataType: "json",
    success: function (data) {
      let html = ` > ${data.name}`;
      $("#title").append(html);
    },
    error: function () {},
  });
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/rooms",
    type: "GET",
    dataType: "json",
    success: function (data) {
      $("#table").bootstrapTable("load", data);
      $("#customSearch").on("keyup", function (event) {
        event.preventDefault();
        var searchText = $(this).val().toLowerCase();
        var filteredData = data.filter(function (item) {
          return item.name.toLowerCase().includes(searchText);
        });

        $("#table").bootstrapTable("load", filteredData);
      });
      $(".background").hide();
    },
    error: function (xhr) {
      $(".background").hide();
      var response = JSON.parse(xhr.responseText);
      toastr.error(response.message);
    },
  });
  $(document).on("click", "#create-room-btn", function () {
    addItem(cinemaId);
  });
  $(document).on("click", ".btn-view-room-details", function (event) {
    const roomId = $(this).data("room-id");
    window.location.href = `/room-details?cinema-id=${encodeURIComponent(
      cinemaId
    )}&room-id=${encodeURIComponent(roomId)}`;
  });
  $("#editModal").on("show.bs.modal", function (event) {
    var button = $(event.relatedTarget);
    var recipient = button.data("whatever");
    var modal = $(this);
    $(".background").show();
    $.ajax({
      url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/" + recipient,
      type: "GET",
      dataType: "json",
      success: function (data) {
        modal.find("#room-id").val(data.id);
        modal.find("#edit-room-name").val(data.name);
        modal.find("#edit-location").val(data.location);
        var options = $("#edit-screen-size option");
        var optionExists =
          options.filter(function () {
            return $(this).val() === data.screenSize;
          }).length > 0;
        if (optionExists) {
          modal.find("#edit-screen-size").val(data.screenSize);
        }
        var option2s = $("#edit-room-type option");
        var optionExist2s =
          option2s.filter(function () {
            return $(this).val() === data.roomType;
          }).length > 0;
        if (optionExist2s) {
          modal.find("#edit-room-type").val(data.roomType);
        }
        var option3s = $("#edit-room-status option");
        var optionExist3s =
          option3s.filter(function () {
            return $(this).val() === data.roomStatus;
          }).length > 0;
        if (optionExist3s) {
          modal.find("#edit-room-status").val(data.roomStatus);
          modal.find("#edit-room-status").prop("disabled", true);
        }
        $(".background").hide();
      },
      error: function (xhr) {
        $(".background").hide();
        var response = JSON.parse(xhr.responseText);
        toastr.error(response.message);
      },
    });
  });
  $(document).on("click", "#edit-room-btn", function (event) {
    let roomId = $("#room-id").val();
    editItem(cinemaId, roomId);
  });
});
function editItem(cinemaId, roomId) {
  let name = $("#edit-room-name").val();
  let location = $("#edit-location").val();
  let screenSize = $("#edit-screen-size").val();
  let roomType = $("#edit-room-type").val();
  let roomStatus = $("#edit-room-status").val();
  let jsonData = {
    name: name,
    location: location,
    screenSize: screenSize,
    roomType: roomType,
    roomStatus: roomStatus,
  };
  $(".background").show();
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/" + roomId,
    type: "PUT",
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    dataType: "json",
    success: function () {
      loadTable(cinemaId);
      $("#editModal").modal("hide");
      $(".background").hide();
      toastr.success("Sửa phòng chiếu phim thành công");
    },
    error: function (xhr) {
      $(".background").hide();
      if (xhr.status === 400) {
        var errorResponse = xhr.responseJSON;
        var errorCode = errorResponse.errorCode;

        if (errorCode === "INVALID_INPUT") {
          $.each(errorResponse.errors, function (fieldName, errorMessage) {
            $("#edit-room-" + fieldName + "-validation").text(errorMessage);
          });
        }
      }
    },
  });
}
function addItem(cinemaId) {
  let name = $("#room-name").val();
  let location = $("#location").val();
  let screenSize = $("#screen-size").val();
  let roomType = $("#room-type").val();
  let roomStatus = "UNASSIGNED";
  let jsonData = {
    name: name,
    location: location,
    screenSize: screenSize,
    roomType: roomType,
    roomStatus: roomStatus,
  };
  $(".background").show();
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/room",
    type: "POST",
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    dataType: "json",
    success: function () {
      loadTable(cinemaId);
      $("#exampleModal").modal("hide");
      $(".background").hide();
      toastr.success("Tạo phòng chiếu phim thành công");
    },
    error: function (xhr) {
      $(".background").hide();
      if (xhr.status === 400) {
        var errorResponse = xhr.responseJSON;
        var errorCode = errorResponse.errorCode;

        if (errorCode === "INVALID_INPUT") {
          $.each(errorResponse.errors, function (fieldName, errorMessage) {
            $("#room-" + fieldName + "-validation").text(errorMessage);
          });
        }
      }
    },
  });
}
function getQueryParam(param) {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(param);
}
function operateFormatter(value, row, index) {
  const cinemaId = getQueryParam("cinema-id");
  let html = [];
  if (row.roomStatus === "AVAILABLE") {
    html.push(
      '<button class="btn btn-info item-operate-btn mx-1 text-white btn-view-room-details" data-room-id="' +
        row.id +
        '"><i class="bi bi-info-circle"></i><span class="ms-3">Chi tiết phòng</span></button>',
      '<button class="btn btn-primary item-operate-btn mx-1" data-bs-toggle="modal" data-bs-target="#editModal" data-whatever="' +
        row.id +
        '"><i class="bi bi-pencil-square"></i></button>',
      '<button id="lock-btn-' +
        row.id +
        '"class="btn btn-danger item-operate-btn mx-1" onclick="deavtivateItem(' +
        cinemaId +
        ", " +
        row.id +
        ',event)"><i class="bi bi-lock"></i></button>'
    );
  } else if (row.roomStatus === "MAINTENANCE") {
    html.push(
      '<button disabled class="btn btn-outline-light item-operate-btn mx-1 btn-view-room-details" data-room-id="' +
        row.id +
        '"><i class="bi bi-info-circle"></i><span class="ms-3">Chi tiết phòng</span></button>',
      '<button disabled class="btn btn-outline-light item-operate-btn mx-1" data-bs-toggle="modal" data-bs-target="#editModal" data-whatever="' +
        row.id +
        '"><i class="bi bi-pencil-square"></i></button>',
      '<button id="lock-btn-' +
        row.id +
        '"class="btn btn-success item-operate-btn mx-1" onclick="activeItem(' +
        cinemaId +
        ", " +
        row.id +
        ',event)"><i class="bi bi-unlock"></i></button>'
    );
  } else {
    html.push(
      '<button id="assign-btn' +
        row.id +
        '"class="btn btn-warning item-operate-btn mx-1 text-white" onclick="assignItem( ' +
        cinemaId +
        "," +
        row.id +
        ', event)" data-room-id="' +
        row.id +
        '"><i class="bi bi-grid-3x3-gap-fill"></i><span class="ms-3">Khởi tạo phòng chiếu phim</span></button>'
    );
  }
  return html.join("");
}
function deavtivateItem(cinemaId, id, event) {
  event.preventDefault();
  let url =
    "http://localhost:8080/api/pub/cinema/" +
    cinemaId +
    "/" +
    id +
    "/deactivate";
  $(".background").show();
  $.ajax({
    url: url,
    type: "PUT",
    dataType: "json",
    success: function (data) {
      let selector = "#lock-btn-" + data;
      $(selector).removeClass("btn-danger").addClass("btn-success");
      $(selector)
        .off("click")
        .on("click", function (event) {
          activeItem(data, event);
        });
      let icon = $(selector).find(".bi");
      $(icon).removeClass("bi-lock").addClass("bi-unlock");
      loadTable(cinemaId);
      $(".background").hide();
    },
    error: function (xhr) {
      $(".background").hide();
      var response = JSON.parse(xhr.responseText);
      toastr.error(response.message);
    },
  });
}
function activeItem(cinemaId, id, event) {
  event.preventDefault();
  let url =
    "http://localhost:8080/api/pub/cinema/" + cinemaId + "/" + id + "/active";
  $(".background").show();
  $.ajax({
    url: url,
    type: "PUT",
    dataType: "json",
    success: function (data) {
      let selector = "#lock-btn-" + data;
      $(selector).removeClass("btn-success").addClass("btn-danger");
      $(selector)
        .off("click")
        .on("click", function (event) {
          deavtivateItem(data, event);
        });
      let icon = $(selector).find(".bi");
      $(icon).removeClass("bi-unlock").addClass("bi-lock");
      loadTable(cinemaId);
      $(".background").hide();
    },
    error: function (xhr) {
      $(".background").hide();
      var response = JSON.parse(xhr.responseText);
      toastr.error(response.message);
    },
  });
}
function assignItem(cinemaId, roomId, event) {
  event.preventDefault();
  window.location.href = `/room-details?cinema-id=${encodeURIComponent(
    cinemaId
  )}&room-id=${encodeURIComponent(roomId)}`;
}
function loadTable(cinemaId) {
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema/" + cinemaId + "/rooms",
    type: "GET",
    dataType: "json",
    success: function (data) {
      $("#table").bootstrapTable("load", data);
    },
    error: function (xhr) {
      var response = JSON.parse(xhr.responseText);
      toastr.error(response.message);
    },
  });
}
function customSSFormatter(value, row, index) {
  if (value === "SMALL") {
    return '<div class="container"><span class="badge text-light" style="background-color:#ec7063">Small</span></div>';
  } else if (value === "MEDIUM") {
    return '<div class="container"><span class="badge text-light" style="background-color:#e74c3c">Medium</span></div>';
  } else {
    return '<div class="container"><span class="badge text-light" style="background-color:#78281f">Large</span></div>';
  }
}
function customRTFormatter(value, row, index) {
  if (value === "STANDARD") {
    return '<div class="container"><span class="badge text-light" style="background-color:#76d7c4">2D</span></div>';
  } else if (value === "THREE_D") {
    return '<div class="container"><span class="badge text-light" style="background-color:#1abc9c">3D</span></div>';
  } else {
    return '<div class="container"><span class="badge text-light" style="background-color:#0e6251">IMAX</span></div>';
  }
}
function customRSFormatter(value, row, index) {
  if (value === "UNASSIGNED") {
    return '<div class="container"><span class="badge bg-danger">Unassigned</span></div>';
  } else if (value === "AVAILABLE") {
    return '<div class="container"><span class="badge bg-success">Available</span></div>';
  } else {
    return '<div class="container"><span class="badge bg-secondary">Maintenance</span></div>';
  }
}
