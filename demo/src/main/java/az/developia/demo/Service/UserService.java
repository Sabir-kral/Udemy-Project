package az.developia.demo.Service;

import az.developia.demo.Entity.EmailVerificationEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Entity.*;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Mapper.UserMapper;
import az.developia.demo.Repo.EmailVerificationRepository;
import az.developia.demo.Repo.RoleRepo;
import az.developia.demo.Repo.UserRepo;
import az.developia.demo.Mapper.PlaylistMapper;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.LoginRequest;
import az.developia.demo.Request.UserRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Request.ResendOtpRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.UserResponse;
import az.developia.demo.Utility.JwtUtil;
import az.developia.demo.Response.PlaylistResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailService mailService;
    private final LogService logService;
    private final StudentRepo studentRepo;
    private final PlaylistRepo playlistRepo;
    private final TeacherRepo teacherRepo;

    public void isUserExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException("User already exists", "Bad Request", 400);
        }
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public MessageResponse verify(AddVerifyRequest request) {
        EmailVerificationEntity otp = emailVerificationRepository
                .findByEmailAndToken(request.getEmail(), request.getCode())
                .orElseThrow(() -> new CustomException("Yanlış OTP kodu", "Invalid OTP", 400));

        if (otp.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new CustomException("OTP vaxtı keçib", "OTP expired", 410);
        }

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("User not found", "Not Found", 404));

        emailVerificationRepository.delete(otp);
        user.setIsVerified(true);
        userRepository.save(user);

        MessageResponse response = new MessageResponse();
        response.setMessage("verified you can now login");
        response.setIsVerified(true);
        logService.add("User verified with email: " + user.getEmail(), "USER_VERIFIED");

        return response;
    }

    public MessageResponse resendOtp(ResendOtpRequest request) throws MessagingException {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("User not found", "Not Found", 404));

        if (user.getIsVerified() != null && user.getIsVerified()) {
            throw new CustomException("User already verified", "Bad Request", 400);
        }

        String code = generateCode();
        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setEmail(request.getEmail());
        entity.setUser(user);
        entity.setToken(code);
        entity.setExpirationDate(LocalDateTime.now().plusMinutes(5));
        emailVerificationRepository.save(entity);

        mailService.verifyEmail(request.getEmail(), code);

        MessageResponse response = new MessageResponse();
        response.setMessage("OTP resend olundu");
        response.setIsVerified(false);
        logService.add("User resend otp with email: " + user.getEmail(), "USER_RESEND_OTP");

        return response;
    }

    private String generateCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            codeBuilder.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return codeBuilder.toString();
    }
    public List<PlaylistResponse> all(){
        return PlaylistMapper.toDTOList(playlistRepo.findAll());
    }
    public List<PlaylistResponse> getMyPlaylists() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getUserType().equalsIgnoreCase("Teacher")) {

            TeacherEntity teacher = teacherRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            return playlistRepo.findByTeacherId(teacher.getId())
                    .stream()
                    .map(PlaylistMapper::toDTO)
                    .toList();
        }


        if (user.getUserType().equalsIgnoreCase("Student")) {

            StudentEntity student = studentRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            if (student.getPurchasedPlaylists() == null) {
                return List.of();
            }

            return student.getPurchasedPlaylists()
                    .stream()
                    .map(PlaylistMapper::toDTO)
                    .toList();
        }

        throw new RuntimeException("Invalid user type");
    }
}