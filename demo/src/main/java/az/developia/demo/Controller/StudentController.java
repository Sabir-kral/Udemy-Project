package az.developia.demo.Controller;

import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.StudentRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @PostMapping("/register")
    @Operation(summary = "Student register API")
    public MessageResponse register(@RequestBody @Valid StudentRequest studentRequest) throws MessagingException {
        return service.register(studentRequest);
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
