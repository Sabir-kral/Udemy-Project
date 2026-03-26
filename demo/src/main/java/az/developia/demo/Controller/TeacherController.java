package az.developia.demo.Controller;

import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.StudentRequest;
import az.developia.demo.Request.TeacherRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService service;
    @PostMapping("/register")
    @Operation(summary = "Student register API")
    public MessageResponse register(@RequestBody @Valid TeacherRequest request) throws MessagingException {
        return service.register(request);
    }
    @PostMapping("/verify")
    public MessageResponse verify(@RequestBody AddVerifyRequest request) {
        return service.verify(request);
    }
    @GetMapping("/profile")
    public UserEntity profile(){
        return service.profile();
    }
}
