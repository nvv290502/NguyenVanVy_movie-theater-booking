    $(document).ready(function () {
        const $tableBody = $('#promotion-table-body');
        const $paging = $('.paging');
        const $managerContainer = $('.manager-container');
    
        // Load promotions and handle pagination
        // Load promotions and handle pagination
        function loadPromotions(page = 1) {
            $.ajax({
                url: `http://localhost:8080/api/pub/promotion?page=${page - 1}&size=5`, // Đặt size=5 để hiển thị 5 promotion trên mỗi trang
                method: 'GET',
                success: function (data) {
                    const promotions = data.content;
                    $tableBody.empty();
    
                    if (promotions.length === 0) {
                        $tableBody.append('<tr><td colspan="8">No promotions found</td></tr>');
                    } else {
                        promotions.forEach(function (promotion) {
                            let btnActive = promotion.statusPromotion === 'ACTIVE' ? 'inline' : 'none';
                            let btnInactive = promotion.statusPromotion === 'ACTIVE' ? 'none' : 'inline';
                            $tableBody.append(`
                            <tr>
                                <td>${promotion.id || 'N/A'}</td>
                                <td>${promotion.info || 'N/A'}</td>
                                <td>${promotion.discount || 'N/A'}</td>
                                <td>${promotion.startDateTime || 'N/A'}</td>
                                <td>${promotion.endDateTime || 'N/A'}</td>
                               
                                <td>
                                    <button class="btn-detail btn-detail-promotion-manager" data-id="${promotion.id || '#'}">
                                        <i class="fas fa-info-circle"></i>
                                        Chỉnh sửa
                                    </button>
                                </td>
                                <td>
                                    <button class="btn-update-status btn-active promotion-active" data-id="${promotion.id}" data-value="${promotion.statusPromotion}" style="display: ${btnActive}">
                                        <i class="fas fa-eye"></i>
                                        ACTIVE
                                    </button>
                                    <button class="btn-update-status btn-inactive promotion-inactive" data-id="${promotion.id}" data-value="${promotion.statusPromotion}" style="display: ${btnInactive}">
                                        <i class="fas fa-eye-slash"></i>
                                        INACTIVE
                                    </button>
                                </td>
                            </tr>
                        `);
                        });
                    }
                    handlePagination(data);
                },
                error: function () {
                    alert('Có lỗi xảy ra khi tải dữ liệu khuyến mãi.');
                }
            });
        }
    
        loadPromotions();
    
        function handlePagination(data) {
            $paging.empty();
    
            const totalPages = data.totalPages;
            const currentPage = data.number + 1;
            const hasPrevious = data.number > 0;
            const hasNext = data.number < totalPages - 1;
    
            if (hasPrevious) {
                $paging.append(`<a href="#" class="paging-item previous" data-page="${currentPage - 1}">&laquo; Previous</a>`);
            }
    
            for (let i = 1; i <= totalPages; i++) {
                $paging.append(`<a href="#" class="paging-item ${currentPage === i ? 'active' : ''}" data-page="${i}">${i}</a>`);
            }
    
            if (hasNext) {
                $paging.append(`<a href="#" class="paging-item next" data-page="${currentPage + 1}">Next &raquo;</a>`);
            }
    
            $('.paging-item').on('click', function (e) {
                e.preventDefault();
                loadPromotions($(this).data('page'));
            });
        }
    
        // Show add promotion form
        $(document).on('click', '.add-promotion', function (e) {
            e.preventDefault();
            $('.form-promotion-add').css('opacity', '1').css('top', '50%');
            $('.manager-container').css('opacity', '1').css('z-index','1');
        });
    
        // Show promotion detail form
        $(document).on('click', '.btn-detail', function (e) {
            e.preventDefault();
            var promotionId = $(this).data('id');
            callApiGetPromotionById(promotionId)
            $('.form-promotion-edit').css('opacity', '1').css('top', '50%');
            $('.manager-container').css('opacity', '1').css('z-index','1');
        });
    
        // Close form
        $(document).on('click', '.close-icon', function (e) {
            e.preventDefault();
            $('.form-promotion-add').css('opacity', '0').css('top', '150%');
            $('.manager-container').css('opacity', '0').css('z-index','-1');
        });
    
        $(document).on('click','.btn-update-status', function (){
            var $status = $(this).data("value");
            var $id = $(this).data("id");
            console.log($status);
            console.log($id);
            callApiUpdateStatus($id, $status);
        });
    
        // Handle submit of add new promotion form
        function savePromotion() {
            $(document).on('click', ".btn-add-promotion", function (e) {
                e.preventDefault();
    
                var formData = new FormData();
                formData.append('info', $('.promotion-info').val());
                formData.append('discount', $('.promotion-discount').val());
    
                var startDateTime = $('.promotion-startDate').val(); // already in correct ISO 8601 format
                var endDateTime = $('.promotion-endDate').val(); // already in correct ISO 8601 format
    
                formData.append('startDateTime', startDateTime);
                formData.append('endDateTime', endDateTime);
    
    
                formData.append('statusPromotion', $('.promotion-status').val());
    
                var imageFile = $('#image-add')[0].files[0];
                if (imageFile) {
                    formData.append('image', imageFile);
                } else {
                    console.error("No file selected or file input element is missing");
                    alert('Vui lòng chọn ảnh để tải lên.');
                    return;
                }
    
                callApiSavePromotion(formData);
            });
        }
    
    
        function callApiSavePromotion(formData) {
            $.ajax({
                url: "http://localhost:8080/api/pub/promotion",
                type: "POST",
                data: formData,
                contentType: false,
                processData: false,
                success: function (data) {
                    toastr.success('Khuyến mãi đã được thêm thành công!');
                    $('.form-promotion-add').css('opacity', '0').css('top', '150%');
                    $('.manager-container').css('opacity', '0').css('z-index','-1');
                    loadPromotions(); // Tải lại danh sách khuyến mãi
                },
                error: function (xhr) {
                    var response = JSON.parse(xhr.responseText);
                    toastr.error(response.message || 'Có lỗi xảy ra khi thêm khuyến mãi.');
                }
            });
        }
        savePromotion();
    
        function renderPromotionDetail(promotion){
            $('#promotion-id-update').val(promotion.id);
            $('#promotion-info-update').val(promotion.info);
            $('#promotion-discount-update').val(promotion.discount);
            $('#promotion-startDateTime-update').val(promotion.startDateTime);
            $('#promotion-endDateTime-update').val(promotion.endDateTime);
            $('#promotion-statusPromotion-update').val(promotion.statusPromotion);
            $('#promotion-imageUrl-update').val(promotion.imageUrl);
    
            $('#img').attr('src', promotion.imageUrl);
    
    
    
    
        }
    
        function callApiGetPromotionById(id){
            var settings = {
                url : "http://localhost:8080/api/pub/promotion/"+id,
                method: "GET",
                success: function (response){
                    console.log(response);
                    renderPromotionDetail(response.data);
                },
                error: function (xhr){
                    var response = JSON.parse(xhr.responseText);
                    console.log(response.message);
                }
            }
            $.ajax(settings);
        }
    
        function updatePromotion() {
            $(document).on('click', ".btn-edit-promotion",function (e){
                e.preventDefault();
                var form = new FormData();
                form.append('id', parseInt($('#promotion-id-update').val(),10));
                form.append('info', $('#promotion-info-update').val());
                form.append('discount', $('#promotion-discount-update').val());
                var startDateTime = $('#promotion-startDateTime-update').val(); // already in correct ISO 8601 format
                var endDateTime = $('#promotion-endDateTime-update').val(); // already in correct ISO 8601 format
    
                form.append('startDateTime', startDateTime);
                form.append('endDateTime', endDateTime);
                form.append('statusPromotion', $('#promotion-statusPromotion-update').val());
    
                var imageHidden = $('#promotion-imageUrl-update').val();
    
                var fileInput = $('#image-update')[0].files[0];
                if(fileInput){
                    form.append('image',fileInput);
                }else {
                    form.append('imageUrl', imageHidden);
                }
                callApiUpdateMovie(form)
    
            })
        }
    
        function callApiUpdateMovie(formData){
            $('.background').show();
            var settings = {
                url : "http://localhost:8080/api/pub/promotion",
                type: "PUT",
                data: formData,
                contentType: false,
                processData: false,
                success: function (data) {
                    toastr.success('Khuyến mãi đã được cập nhật thành công!');
                    $('.form-promotion-edit').css('opacity', '0').css('top', '150%');
                    $('.manager-container').css('opacity', '0').css('z-index','-1');
                    loadPromotions();
                },
                error: function (xhr) {
                    var response = JSON.parse(xhr.responseText);
                    toastr.error(response.message || 'Có lỗi xảy ra khi cập nhật khuyến mãi.');
                }
            }
            $.ajax(settings)
        }
    
        updatePromotion();

        function callApiUpdateStatus(id, currentStatus) {
            // Xác định trạng thái mới dựa trên trạng thái hiện tại
            var newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';

            $.ajax({
                url: "http://localhost:8080/api/pub/promotion/ACTIVE?id="+id+"&status="+currentStatus,
                method: "POST",
                contentType: "application/json",
                success: function(response) {
                    // Cập nhật giao diện người dùng dựa trên trạng thái mới
                    updatePromotionStatus(id, newStatus);
                    toastr.success(response.message);
                },
                error: function(xhr) {
                    var response = JSON.parse(xhr.responseText);
                    toastr.error(response.message);
                }
            });
        }

        function updatePromotionStatus(id, newStatus) {
            var $row = $('button[data-id="' + id + '"]').closest('tr');
            var $btnActive = $row.find('.btn-active');
            var $btnInactive = $row.find('.btn-inactive');

            if (newStatus === 'ACTIVE') {
                $btnActive.show();
                $btnInactive.hide();
            } else {
                $btnActive.hide();
                $btnInactive.show();
            }

            // Cập nhật giá trị data-value để đảm bảo lần click sau sẽ đúng trạng thái
            $btnActive.data("value", newStatus);
            $btnInactive.data("value", newStatus);
        }


    });
