package az.developia.demo.Controller;

import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.ResendOtpRequest;
import az.developia.demo.Request.UserRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.UserResponse;
import az.developia.demo.Service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService service;

    @PostMapping("/verify")
    public MessageResponse verify(@RequestBody AddVerifyRequest request){
        return service.verify(request);
    }


    @PostMapping("/resendOTP")
    public MessageResponse resendOTP(@RequestBody ResendOtpRequest request) throws MessagingException{
        return service.resendOtp(request);
    }


}
