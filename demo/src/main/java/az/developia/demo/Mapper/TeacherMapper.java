package az.developia.demo.Mapper;


import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Response.StudentResponse;
import az.developia.demo.Response.TeacherResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherMapper {

    public static TeacherResponse toDTO(TeacherEntity entity) {
        TeacherResponse dto = new TeacherResponse();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setProfilePictureUrl(entity.getProfilePictureUrl());
        dto.setAverageRating(entity.getAverageRating());
        dto.setTotalReviews(entity.getTotalReviews());

        dto.setStudentCount(entity.getStudents() != null ? entity.getStudents().size() : 0);

        if (entity.getStudents() != null) {
            dto.setStudents(entity.getStudents().stream()
                    .map(StudentMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static List<TeacherResponse> toDTOList(List<TeacherEntity> entities) {
        return entities.stream().map(TeacherMapper::toDTO).collect(Collectors.toList());
    }
}