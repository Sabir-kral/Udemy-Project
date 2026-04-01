package az.developia.demo.Controller;

import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.ResendOtpRequest;
import az.developia.demo.Request.UserRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Response.UserResponse;
import az.developia.demo.Service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService service;

    @PostMapping("/verify")
    public MessageResponse verify(@RequestBody AddVerifyRequest request) {
        return service.verify(request);
    }

    @GetMapping("/playlists")
    public List<PlaylistResponse> All() {
        return service.all();
    }

    @PostMapping("/resendOTP")
    public MessageResponse resendOTP(@RequestBody ResendOtpRequest request) throws MessagingException {
        return service.resendOtp(request);
    }

    @GetMapping("/playlists/mine")
    public ResponseEntity<List<PlaylistResponse>> getMyPlaylists() {
        return ResponseEntity.ok(service.getMyPlaylists());
    }
}