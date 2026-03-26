package az.developia.demo.Controller;

import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.UserRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.UserResponse;
import az.developia.demo.Service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService service;


}
