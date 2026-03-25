package az.developia.demo.Response;

import lombok.Data;

@Data
public class StudentResponse {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Double balance;
}
