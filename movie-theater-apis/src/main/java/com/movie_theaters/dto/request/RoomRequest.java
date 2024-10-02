package com.movie_theaters.dto.request;

import com.movie_theaters.entity.Cinema;
import com.movie_theaters.entity.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data@NoArgsConstructor@AllArgsConstructor
public class RoomRequest {

    @NotBlank(message = "Tên phòng không được để trống")
    @Size(max = 100, message = "Tên phòng không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Vị trí không được để trống")
    @Size(max = 200, message = "Vị trí không được vượt quá 200 ký tự")
    private String location;

    @NotNull(message = "Loại phòng không được để trống")
    private RoomType roomType;

    private Long cinemaId;
}
