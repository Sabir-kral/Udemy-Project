package az.developia.demo.Controller;

import az.developia.demo.Request.PlaylistRequest;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(@RequestBody @Valid PlaylistRequest request) {
        PlaylistResponse response = playlistService.createPlaylist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}