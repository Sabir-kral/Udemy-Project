package az.developia.demo.Service;

import az.developia.demo.Entity.*;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Mapper.StudentMapper;
import az.developia.demo.Mapper.TeacherMapper;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.TeacherRequest;
import az.developia.demo.Request.TeacherUpdateRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.StudentResponse;
import az.developia.demo.Response.TeacherResponse;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepo userRepository;
    private final TeacherRepo teacherRepo;
    private final CategoryRepo categoryRepo;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final StudentRepo studentRepo;
    private final LogService logService;
    private final ReviewRepo reviewRepo;

    public MessageResponse register(TeacherRequest request) throws MessagingException {
        userService.isUserExists(request.getEmail());

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(100.0);
        user.setUserType("Teacher");
        userRepository.save(user);

        TeacherEntity teacher = new TeacherEntity();
        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setRepeatPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setTitle(request.getTitle());
        teacher.setDescription(request.getDescription());
        teacher.setProfilePictureUrl(request.getProfilePictureUrl());
        teacher.setStudentCount(0);

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new CustomException("Passwords do not match", "BAD_REQUEST", 400);
        }

        CategoryEntity category = categoryRepo.findByName(request.getCategoryName())
                .orElseGet(() -> {
                    CategoryEntity newCategory = new CategoryEntity();
                    newCategory.setName(request.getCategoryName());
                    return categoryRepo.save(newCategory);
                });

        teacher.setCategory(category);
        teacher.setUser(user);
        teacherRepo.save(teacher);

        roleRepository.assignTeacherRoles(user.getId());

        String code = generateCode();
        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setToken(code);
        entity.setUser(user);
        entity.setEmail(request.getEmail());
        entity.setExpirationDate(LocalDateTime.now().plusMinutes(2));
        emailVerificationRepository.save(entity);

        mailService.verifyEmail(request.getEmail(), code);

        MessageResponse response = new MessageResponse();
        response.setMessage("Teacher registered successfully");
        response.setUserType(user.getUserType());
        response.setIsVerified(false);
        logService.add("Teacher registered with name: " + teacher.getName(), "TEACHER_REGISTERED");
        return response;
    }

    public TeacherResponse profile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TeacherEntity teacher = teacherRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found with email: " + username));
        return TeacherMapper.toDTO(teacher);
    }

    public List<StudentResponse> myStudents() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TeacherEntity teacher = teacherRepo.findByEmail(username)
                .orElseThrow(() -> new CustomException("Teacher not found", "NOT_FOUND", 404));

        return teacher.getStudents().stream()
                .map(StudentMapper::toDTO)
                .toList();
    }

    public MessageResponse update(String email, TeacherUpdateRequest request){
        TeacherEntity teacher = teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Not Found"));
        UserEntity users = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        users.setEmail(request.getEmail());

        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());
        teacher.setTitle(request.getTitle());
        teacher.setDescription(request.getDescription());
        teacher.setProfilePictureUrl(request.getProfilePictureUrl());

        if(request.getCategoryName() != null){
            CategoryEntity category = categoryRepo.findByName(request.getCategoryName())
                    .orElseGet(() -> {
                        CategoryEntity newCategory = new CategoryEntity();
                        newCategory.setName(request.getCategoryName());
                        return categoryRepo.save(newCategory);
                    });
            teacher.setCategory(category);
        }

        teacherRepo.save(teacher);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Update edildi");
        return messageResponse;
    }
    @Transactional
    public void delete(String email){
        TeacherEntity teacher = teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        UserEntity user = teacher.getUser();

        teacherRepo.delete(teacher);
        teacherRepo.flush();

        userRepository.delete(user);
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
}