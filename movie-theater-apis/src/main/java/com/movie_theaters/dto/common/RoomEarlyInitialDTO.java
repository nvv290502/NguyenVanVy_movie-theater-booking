package com.movie_theaters.dto.common;

import com.movie_theaters.entity.enums.RoomStatus;
import com.movie_theaters.entity.enums.RoomType;
import com.movie_theaters.entity.enums.ScreenSize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomEarlyInitialDTO {
    private Long id;
    @NotBlank(message = "Tên phòng không được để trống")
    @Size(max = 100, message = "Tên phòng không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Vị trí không được để trống")
    @Size(max = 200, message = "Vị trí không được vượt quá 200 ký tự")
    private String location;

    @NotNull(message = "Kích cỡ màn hình không được để trống")
    private ScreenSize screenSize;

    @NotNull(message = "Loại phòng không được để trống")
    private RoomType roomType;

    private RoomStatus roomStatus;

}
