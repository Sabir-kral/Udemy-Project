package az.developia.demo.Controller;

import az.developia.demo.Entity.PlaylistEntity;
import az.developia.demo.Request.PictureRequest;
import az.developia.demo.Request.PlaylistRequest;
import az.developia.demo.Request.PlaylistUpdateRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping("/create")
    public PlaylistResponse createPlaylist(@RequestBody PlaylistRequest request) {
        return playlistService.createPlaylist(request);
    }

    @GetMapping("/playlist")
    public List<PlaylistResponse> category(@RequestParam Long categoryId){
        return playlistService.getPlaylistsByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public PlaylistResponse findById(@PathVariable Long id){
        return playlistService.findById(id);
    }
    @PostMapping("/api/playlists/{id}/upload-from-url")
    public ResponseEntity<MessageResponse> uploadFromUrl(
            @PathVariable Long id,
            @RequestBody PictureRequest request) {

        return ResponseEntity.ok(playlistService.uploadFromUrl(id, request));
    }
    @DeleteMapping("/{id}/delete-picture")
    public ResponseEntity<MessageResponse> deletePicture(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.deletePicture(id));
    }

}