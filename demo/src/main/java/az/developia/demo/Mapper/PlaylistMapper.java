package az.developia.demo.Mapper;

import az.developia.demo.Entity.PlaylistEntity;
import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Response.TeacherResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PlaylistMapper {
    public static PlaylistResponse toDTO(PlaylistEntity entity) {
        PlaylistResponse dto = new PlaylistResponse();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setAverageRating(entity.getAverageRating());
        dto.setTotalReviews(entity.getTotalReviews());
        dto.setPicture(entity.getPictureUrl());

        if (entity.getCategory() != null) {
            dto.setCategoryName(entity.getCategory().getName());
        }

        return dto;
    }

    public static List<PlaylistResponse> toDTOList(List<PlaylistEntity> entities) {
        return entities.stream().map(PlaylistMapper::toDTO).collect(Collectors.toList());
    }
}