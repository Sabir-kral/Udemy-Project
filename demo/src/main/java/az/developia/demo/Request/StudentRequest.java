package az.developia.demo.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    @Size(min = 2,max = 50,message = "min 2 max 50 herfden ibaret olmalidir")
    @NotBlank(message = "Bos ola bilmez")
    private String name;
    @Size(min = 2,max = 50,message = "min 2 max 50 herfden ibaret olmalidir")
    @NotBlank(message = "Bos ola bilmez")
    private String surname;
    @NotBlank(message = "Bos ola bilmez")
    @Size(min = 12, max = 12, message = "Tam 12 simvol olmalidır")
    private String phone;
    @Email
    @NotBlank(message = "Bos ola bilmez")
    private String email;

    private LocalDate birthday;
    @NotBlank(message = "Bos ola bilmez")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "Minimum 8 simvol, en azi 1 boyuk, 1 kicik herf, 1 reqem və 1 xususi simvol olmalidır"
    )
    private String password;
    @NotBlank(message = "Bos ola bilmez")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "Minimum 8 simvol, en azi 1 boyuk, 1 kicik herf, 1 reqem və 1 xususi simvol olmalidır"
    )
    private String repeatPassword;
}
