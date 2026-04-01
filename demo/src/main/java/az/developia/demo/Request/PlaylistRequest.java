package az.developia.demo.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PlaylistRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    private Long categoryId;
}