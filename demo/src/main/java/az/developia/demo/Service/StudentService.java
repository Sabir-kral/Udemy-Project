package az.developia.demo.Service;

import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Repo.RoleRepo;
import az.developia.demo.Repo.StudentRepo;
import az.developia.demo.Repo.UserRepo;
import az.developia.demo.Request.StudentRequest;
import az.developia.demo.Request.UserRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import az.developia.demo.*;
import az.developia.demo.Utility.JwtUtil;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Entity.EmailVerificationEntity;
import az.developia.demo.Repo.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserRepo userRepository;
    private final StudentRepo studentRepo;
    private final RoleRepo roleRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final JwtUtil jwtUtil;
    private final EmailVerificationRepository emailVerificationRepository;

    public MessageResponse register(StudentRequest request) throws MessagingException {
        userService.isUserExists(request.getEmail());
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(100.0);
        user.setUserType("Student");
        userRepository.save(user);

        StudentEntity students = new StudentEntity();
        students.setName(request.getName());
        students.setSurname(request.getSurname());
        students.setPhone(request.getPhone());
        students.setEmail(request.getEmail());
        students.setPassword(passwordEncoder.encode(request.getPassword()));
        students.setRepeatPassword(passwordEncoder.encode(request.getPassword()));
        students.setBirthday(request.getBirthday());



        String code = generateCode();

        studentRepo.save(students);

        roleRepository.assignStudentRoles(user.getId());


        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setToken(code);
        entity.setUser(user);
        entity.setEmail(request.getEmail());
        entity.setExpirationDate(LocalDateTime.now().plusMinutes(2));

        emailVerificationRepository.save(entity);

        mailService.verifyEmail(request.getEmail(), code);

        MessageResponse response = new MessageResponse();
        response.setMessage("ugurlu oldu");
        response.setUserType(user.getUserType());
        response.setIsVerified(false);


        return response;
    }

    public MessageResponse verify(AddVerifyRequest request) {

        EmailVerificationEntity otp = emailVerificationRepository.findByEmailAndToken(request.getEmail(), request.getCode())
                .orElseThrow(() -> new CustomException("Yanlış OTP kodu", "Invalid OTP", 400));

        if (otp.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new CustomException("OTP vaxtı keçib", "OTP expired", 410);

        }

        emailVerificationRepository.delete(otp);

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

        MessageResponse response = new MessageResponse();
        response.setMessage("verified you can now login");
        response.setIsVerified(true);
        return response;
    }



    private String generateCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public UserEntity profile(){
        return userService.getCurrentUser();
    }
}
