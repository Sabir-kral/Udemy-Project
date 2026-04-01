package az.developia.demo.Service;

import az.developia.demo.Entity.*;
import az.developia.demo.Mapper.PlaylistMapper;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.PictureRequest;
import az.developia.demo.Request.PlaylistRequest;
import az.developia.demo.Request.PlaylistUpdateRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepo playlistRepo;
    private final TeacherRepo teacherRepo;
    private final CategoryRepo categoryRepo;

    @Transactional
    public PlaylistResponse createPlaylist(PlaylistRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        TeacherEntity teacher = teacherRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        CategoryEntity category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

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
        return PlaylistMapper.toDTO(playlist);
    }
    public List<PlaylistResponse> getPlaylistsByCategory(Long categoryId) {

        List<PlaylistEntity> playlists = playlistRepo.findByCategoryId(categoryId);

        return playlists.stream()
                .map(PlaylistMapper::toDTO)
                .toList();
    }
    public PlaylistResponse findById(Long playlistId){
        PlaylistEntity playlist = playlistRepo.findById(playlistId).orElseThrow(()->new RuntimeException("Not Found"));
        return PlaylistMapper.toDTO(playlist);
    }
    @Transactional
    public MessageResponse uploadFromUrl(Long id, PictureRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        TeacherEntity teacher = teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        PlaylistEntity playlist = playlistRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Access denied");
        }

        try {
            URL url = new URL(request.getUrl());
            InputStream in = url.openStream();

            String filename = "playlist_" + id + "_" + System.currentTimeMillis() + ".jpg";
            Path path = Paths.get("uploads/" + filename);

            Files.createDirectories(path.getParent());
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);

            String savedUrl = "http://localhost:8080/uploads/" + filename;
            playlist.setPictureUrl(savedUrl);

            playlistRepo.save(playlist);

            MessageResponse response = new MessageResponse();
            response.setMessage("Picture uploaded successfully");

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to download image");
        }
    }
    @Transactional
    public MessageResponse deletePicture(Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        TeacherEntity teacher = teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        PlaylistEntity playlist = playlistRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // 🔐 ownership check
        if (!playlist.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Access denied");
        }

        // ❗ əgər şəkil yoxdursa
        if (playlist.getPictureUrl() == null) {
            throw new RuntimeException("Picture not found");
        }

        try {
            // 🧹 local file sil
            String filePath = playlist.getPictureUrl()
                    .replace("http://localhost:8080/", ""); // uploads/xxx.jpg

            Path path = Paths.get(filePath);

            Files.deleteIfExists(path);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file");
        }

        // 🗑️ DB-dən sil
        playlist.setPictureUrl(null);
        playlistRepo.save(playlist);

        MessageResponse response = new MessageResponse();
        response.setMessage("Picture deleted successfully");

        return response;
    }

}
