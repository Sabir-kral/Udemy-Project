package az.developia.demo.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentUpdateRequest {
    private String name;
    private String surname;
    private String phone;
    private LocalDate birthday;
}