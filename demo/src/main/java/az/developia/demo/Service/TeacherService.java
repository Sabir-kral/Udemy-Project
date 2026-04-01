package az.developia.demo.Service;

import az.developia.demo.Entity.*;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Mapper.StudentMapper;
import az.developia.demo.Mapper.TeacherMapper;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.PlaylistUpdateRequest;
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
    private final PlaylistRepo playlistRepo;

    @Transactional
    public MessageResponse register(TeacherRequest request) throws MessagingException {
        // 1️⃣ Teacher entity qurulur
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

        // 2️⃣ User entity hazırlanır (amma DB-yə save olunmur hələ)
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(100.0);
        user.setUserType("Teacher");
        user.setIsVerified(false);

        // 3️⃣ Password yoxlaması
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new CustomException("Passwords do not match", "BAD_REQUEST", 400);
        }

        // 4️⃣ Category tapılır və ya yaradılır
        CategoryEntity category = categoryRepo.findByName(request.getCategoryName())
                .orElseGet(() -> {
                    CategoryEntity newCategory = new CategoryEntity();
                    newCategory.setName(request.getCategoryName());
                    return categoryRepo.save(newCategory);
                });
        teacher.setCategory(category);

        // 5️⃣ User və Teacher DB-yə save olunur
        userRepository.save(user);
        teacher.setUser(user);
        teacherRepo.save(teacher);

        // 6️⃣ Role təyin edilir
        roleRepository.assignTeacherRoles(user.getId());

        // 7️⃣ Email verification
        String code = generateCode();
        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setToken(code);
        entity.setUser(user);
        entity.setEmail(request.getEmail());
        entity.setExpirationDate(LocalDateTime.now().plusMinutes(2));
        emailVerificationRepository.save(entity);

        mailService.verifyEmail(request.getEmail(), code);

        // 8️⃣ Response
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
    @Transactional
    public MessageResponse updatePlaylist(Long id, PlaylistUpdateRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        TeacherEntity teacher = teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        PlaylistEntity playlist = playlistRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getTeacher().getId().equals(teacher.getId())) {
            throw new CustomException("Access denied", "FORBIDDEN", 403);
        }


        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            playlist.setCategory(category);
        }


        if (request.getTitle() != null) {
            playlist.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            playlist.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            playlist.setPrice(request.getPrice());
        }

        playlistRepo.save(playlist);

        MessageResponse response = new MessageResponse();
        response.setMessage("Playlist updated successfully");

        return response;
    }

    @Transactional
    public MessageResponse deletePlaylist(Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        TeacherEntity teacher = teacherRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));


        PlaylistEntity playlist = playlistRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getTeacher().getId().equals(teacher.getId())) {
            throw new CustomException("Access denied", "FORBIDDEN", 403);
        }

        List<StudentEntity> students = studentRepo.findAll();

        for (StudentEntity student : students) {
            if (student.getPurchasedPlaylists() != null) {
                student.getPurchasedPlaylists().removeIf(p -> p.getId().equals(id));
            }
        }

        studentRepo.saveAll(students);


        playlistRepo.delete(playlist);

        MessageResponse response = new MessageResponse();
        response.setMessage("Playlist deleted successfully");

        return response;
    }
}