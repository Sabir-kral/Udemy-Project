package az.developia.demo.Service;

import az.developia.demo.Entity.*;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.PlaylistRequest;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepo playlistRepo;
    private final TeacherRepo teacherRepo;
    private final CategoryRepo categoryRepo;

    @Transactional
    public PlaylistResponse createPlaylist(PlaylistRequest request) {
        // 1. Teacher tap (logged-in)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TeacherEntity teacher = teacherRepo.findByEmail(username)
                .orElseThrow(() -> new CustomException("Teacher not found", "NOT_FOUND", 404));

        // 2. Category tap
        CategoryEntity category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException("Category not found", "NOT_FOUND", 404));

        // 3. Playlist yarat
        PlaylistEntity playlist = PlaylistEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .teacher(teacher)
                .category(category)
                .averageRating(0.0)
                .totalReviews(0)
                .build();

        playlistRepo.save(playlist);

        // 4. Response yarat
        return PlaylistResponse.builder()
                .id(playlist.getId())
                .title(playlist.getTitle())
                .description(playlist.getDescription())
                .price(playlist.getPrice())
                .categoryId(category.getId())
                .teacherId(teacher.getId())
                .averageRating(playlist.getAverageRating())
                .totalReviews(playlist.getTotalReviews())
                .build();
    }
}