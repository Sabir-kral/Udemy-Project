package az.developia.demo.Service;

import az.developia.demo.Entity.EmailVerificationEntity;
import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.StudentRequest;
import az.developia.demo.Request.TeacherRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Utility.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRepo userRepository;
    private final TeacherRepo teacherRepo;
    private final RoleRepo roleRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final JwtUtil jwtUtil;
    private final EmailVerificationRepository emailVerificationRepository;

    public MessageResponse register(TeacherRequest request) throws MessagingException {
        userService.isUserExists(request.getEmail());
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(100.0);
        user.setUserType("Teacher");
        userRepository.save(user);

        TeacherEntity teachers = new TeacherEntity();
        teachers.setName(request.getName());
        teachers.setSurname(request.getSurname());
        teachers.setPhone(request.getPhone());
        teachers.setEmail(request.getEmail());
        teachers.setPassword(passwordEncoder.encode(request.getPassword()));
        teachers.setRepeatPassword(passwordEncoder.encode(request.getPassword()));
        teachers.setTitle(request.getTitle());
        teachers.setDescription(request.getDescription());
        teachers.setProfilePictureUrl(request.getProfilePictureUrl());



        String code = generateCode();

        teacherRepo.save(teachers);

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
