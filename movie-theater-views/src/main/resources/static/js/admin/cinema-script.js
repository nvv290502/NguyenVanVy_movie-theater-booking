$(document).ready(function () {
  $(".menu-admin").load("/menu", function () {
    $.getScript("/js/admin/menu.js");
  });
  $(".background").show();
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema",
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
  $(document).on("change", "#image", function (event) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        $("#preview-image").attr("src", e.target.result).show();
      };
      reader.readAsDataURL(file);
    }
  });
  $(document).on("click", "#create-cinema-btn", function () {
    addItem();
  });
  $("#exampleModal").on("show.bs.modal", function (event) {
    var modal = $(this);
    modal.find("#cinema-name").val("");
    modal.find("#address").val("");
    modal.find("#description").val("");
    modal.find("#hotline").val("");
    modal.find("#image").val(null);
    modal.find("#preview-image").attr("src", "").hide();
  });
  $("#editModal").on("show.bs.modal", function (event) {
    var button = $(event.relatedTarget);
    var recipient = button.data("whatever");
    var modal = $(this);
    $(".background").show();
    $.ajax({
      url: "http://localhost:8080/api/pub/cinema/" + recipient,
      type: "GET",
      dataType: "json",
      success: function (data) {
        modal.find("#cinema-id").val(data.id);
        modal.find("#update-cinema-name").val(data.name);
        modal.find("#update-address").val(data.address);
        modal.find("#update-description").val(data.description);
        modal.find("#update-hotline").val(data.hotline);
        modal.find("#update-image").val(null);
        if (data.imageUrl) {
          modal.find("#update-preview-image").attr("src", data.imageUrl).show();
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
  $(document).on("change", "#update-image", function (event) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        $("#update-preview-image").attr("src", e.target.result);
      };
      reader.readAsDataURL(file);
    }
  });
  $(document).on("click", "#update-cinema-btn", function () {
    var cinemaId = $("#cinema-id").val();
    editItem(cinemaId);
  });

  $(document).on("click", ".btn-view-list-rooms", function () {
    const cinemaId = $(this).data("cinema-id");
    window.location.href = `/room-manager?cinema-id=${encodeURIComponent(
      cinemaId
    )}`;
  });
});
function operateFormatter(value, row, index) {
  let html = [];
  if (row.isEnabled === true) {
    html.push(
      '<button class="btn btn-info item-operate-btn mx-1 text-white btn-view-list-rooms" data-cinema-id="' +
        row.id +
        '"><i class="bi bi-info-circle"></i><span class="ms-3">Danh sách phòng</span></button>',
      '<button class="btn btn-primary item-operate-btn mx-1" data-bs-toggle="modal" data-bs-target="#editModal" data-whatever="' +
        row.id +
        '"><i class="bi bi-pencil-square"></i></button>',
      '<button id="lock-btn-' +
        row.id +
        '"class="btn btn-danger item-operate-btn mx-1" onclick="deavtivateItem(' +
        row.id +
        ',event)"><i class="bi bi-lock"></i></button>'
    );
  } else {
    html.push(
      '<button class="disabled btn btn-outlint-light item-operate-btn mx-1 text-white btn-view-list-rooms" data-cinema-id="' +
        row.id +
        '"><i class="bi bi-info-circle"></i><span class="ms-3">Danh sách phòng</span></button>',
      '<button class="disabled btn btn-outlint-light item-operate-btn mx-1" data-bs-toggle="modal" data-bs-target="#editModal" data-whatever="' +
        row.id +
        '"><i class="bi bi-pencil-square"></i></button>',
      '<button id="lock-btn-' +
        row.id +
        '"class="btn btn-success item-operate-btn mx-1" onclick="activeItem(' +
        row.id +
        ',event)"><i class="bi bi-unlock"></i></button>'
    );
  }
  return html.join("");
}
function addItem() {
  const fileInput = $("#image")[0];
  const file = fileInput.files[0];
  if (file) {
    const formData = new FormData();
    let name = $("#cinema-name").val();
    let address = $("#address").val();
    let description = $("#description").val();
    let hotline = $("#hotline").val();
    let jsonData = {
      name: name,
      address: address,
      description: description,
      hotline: hotline,
    };
    formData.append("cinema-data", JSON.stringify(jsonData));
    formData.append("cinema-image", file);
    $(".background").show();
    $.ajax({
      url: "http://localhost:8080/api/pub/cinema",
      type: "POST",
      data: formData,
      contentType: false, // Đặt contentType là false
      processData: false, // Đặt processData là false
      success: function (data) {
        loadTable();
        $("#exampleModal").modal("hide");
        $(".background").hide();
        toastr.success("Thêm rạp chiếu phim thành công");
      },
      error: function (xhr) {
        $(".background").hide();
        if (xhr.status === 400) {
          var errorResponse = xhr.responseJSON;
          var errorCode = errorResponse.errorCode;

          if (errorCode === "INVALID_INPUT") {
            $.each(errorResponse.errors, function (fieldName, errorMessage) {
              $("#cinema-" + fieldName + "-validation").text(errorMessage);
            });
          }
        }
      },
    });
  } else {
    $("#image-validation").text("Không tải được file");
  }
}
function editItem(id) {
  const fileInput = $("#update-image")[0];
  const file = fileInput.files[0];
  const formData = new FormData();
  let name = $("#update-cinema-name").val();
  let address = $("#update-address").val();
  let description = $("#update-description").val();
  let hotline = $("#update-hotline").val();
  let jsonData = {
    name: name,
    address: address,
    description: description,
    hotline: hotline,
  };
  formData.append("cinema-data", JSON.stringify(jsonData));
  if (file) {
    formData.append("cinema-image", file);
  } else {
    formData.append("cinema-image", null);
  }
  $(".background").show();
  let url = "http://localhost:8080/api/pub/cinema/" + id;
  $.ajax({
    url: url,
    type: "PUT",
    data: formData,
    contentType: false, // Đặt contentType là false
    processData: false, // Đặt processData là false
    success: function (data) {
      loadTable();
      $("#editModal").modal("hide");
      $(".background").hide();
      toastr.success("Sửa rạp chiếu phim thành công");
    },
    error: function (xhr) {
      $(".background").hide();
      if (xhr.status === 400) {
        var errorResponse = xhr.responseJSON;
        var errorCode = errorResponse.errorCode;

        if (errorCode === "INVALID_INPUT") {
          $.each(errorResponse.errors, function (fieldName, errorMessage) {
            $("#update-cinema-" + fieldName + "-validation").text(errorMessage);
          });
        }
      }
    },
  });
}
function deavtivateItem(id, event) {
  event.preventDefault();
  let url = "http://localhost:8080/api/pub/cinema/" + id + "/deactivate";
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
      loadTable();
      $(".background").hide();
      toastr.success("Khóa rạp chiếu phim thành công");
    },
    error: function (xhr) {
      var response = JSON.parse(xhr.responseText);
      toastr.error(response.message);
    },
  });
}
function activeItem(id, event) {
  event.preventDefault();
  let url = "http://localhost:8080/api/pub/cinema/" + id + "/active";
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
      loadTable();
      $(".background").hide();
      toastr.success("Mở khóa rạp chiếu phim thành công");
    },
    error: function (xhr) {
      var response = JSON.parse(xhr.responseText);
      toastr.error(response.message);
    },
  });
}
function loadTable() {
  $.ajax({
    url: "http://localhost:8080/api/pub/cinema",
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
function customFormatter(value, row, index) {
  if (value === false) {
    return '<div class="container"><span class="badge bg-danger">Inactive</span></div>';
  } else {
    return '<div class="container"><span class="badge bg-success">Active</span></div>';
  }
}
