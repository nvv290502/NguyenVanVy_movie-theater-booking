package com.movie_theaters.dto;

import com.movie_theaters.entity.Cinema;
import com.movie_theaters.entity.enums.RoomStatus;
import com.movie_theaters.entity.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class RoomDTO {
    private Long id;
    @NotBlank(message = "Tên phòng không được để trống")
    @Size(max = 100, message = "Tên phòng không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Vị trí không được để trống")
    @Size(max = 200, message = "Vị trí không được vượt quá 200 ký tự")
    private String location;

    @NotNull(message = "Số ghế không được để trống")
    @Min(value = 1, message = "Số ghế phải lớn hơn 0")
    private Integer seatNumbers;

    @NotNull(message = "Loại phòng không được để trống")
    private RoomType roomType;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean isEnabled;

    @NotNull(message = "Trạng thái phòng không được để trống")
    private RoomStatus roomStatus;

    private Cinema cinema;
}
