package az.developia.demo.Service;

import az.developia.demo.Entity.EmailVerificationEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Mapper.UserMapper;
import az.developia.demo.Repo.EmailVerificationRepository;
import az.developia.demo.Repo.RoleRepo;
import az.developia.demo.Repo.UserRepo;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.LoginRequest;
import az.developia.demo.Request.UserRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.UserResponse;
import az.developia.demo.Utility.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepository;


    public void isUserExists(String username) {
        if (userRepository.findByEmail(username).isPresent()) {
            throw new CustomException("User already exists", "Bad Request", 400);
        }
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // user1
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
