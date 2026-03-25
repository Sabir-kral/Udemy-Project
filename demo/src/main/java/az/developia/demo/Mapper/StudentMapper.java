package az.developia.demo.Mapper;

import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Response.StudentResponse;

public class StudentMapper {

    public static StudentResponse toDTO(StudentEntity entity) {
        StudentResponse dto = new StudentResponse();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setBalance(entity.getUser().getBalance());
        return dto;
    }
}