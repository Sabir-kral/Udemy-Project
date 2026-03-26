package az.developia.demo.Controller;

import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Repo.UserRepo;
import az.developia.demo.Request.LoginRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Service.CustomUserDetailsService;
import az.developia.demo.Utility.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request ) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        }catch (BadCredentialsException e) {
            throw new RuntimeException("Daxil edilen melumatlar yanlisdir");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        UserEntity users = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new LoginResponse(users.getId(),request.getEmail(), users.getUserType(), accessToken,users.getBalance());
    }
}