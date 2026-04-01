package az.developia.demo.Service;

import az.developia.demo.Entity.*;
import az.developia.demo.Exception.CustomException;
import az.developia.demo.Mapper.PlaylistMapper;
import az.developia.demo.Mapper.StudentMapper;
import az.developia.demo.Repo.*;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.StudentRequest;
import az.developia.demo.Request.StudentUpdateRequest;
import az.developia.demo.Request.TeacherUpdateRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Response.StudentResponse;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepo userRepository;
    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final LogService logService;
    private final PlaylistRepo playlistRepo;

    public MessageResponse register(StudentRequest request) throws MessagingException {
        userService.isUserExists(request.getEmail());




        TeacherEntity teacher = teacherRepo.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(100.0);
        user.setUserType("Student");
        user.setIsVerified(false);


        StudentEntity student = new StudentEntity();
        student.setName(request.getName());
        student.setSurname(request.getSurname());
        student.setPhone(request.getPhone());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRepeatPassword(passwordEncoder.encode(request.getPassword()));
        student.setBirthday(request.getBirthday());
        student.setUser(user);
        student.setTeacher(teacher);

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new CustomException("Passwords do not match", "BAD_REQUEST", 400);
        }

        userRepository.save(user);
        roleRepository.assignStudentRoles(user.getId());

        studentRepo.save(student);

        teacher.setStudentCount((teacher.getStudentCount() == null ? 0 : teacher.getStudentCount()) + 1);
        teacherRepo.save(teacher);

        String code = generateCode();
        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setToken(code);
        entity.setUser(user);
        entity.setEmail(request.getEmail());
        entity.setExpirationDate(LocalDateTime.now().plusMinutes(2));
        emailVerificationRepository.save(entity);

        mailService.verifyEmail(request.getEmail(), code);

        MessageResponse response = new MessageResponse();
        response.setMessage("Student registered successfully");
        response.setUserType(user.getUserType());
        response.setIsVerified(false);
        logService.add("Student registered with name: "+student.getName(),"STUDENT_REGISTERED");
        return response;
    }

    public StudentResponse profile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        StudentEntity student = studentRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + username));
        return StudentMapper.toDTO(student);
    }

    public MessageResponse updateStudentProfile(Map<String, Object> body, UserEntity user) {
        StudentEntity student = studentRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName((String) body.get("name"));
        student.setSurname((String) body.get("surname"));
        student.setPhone((String) body.get("phone"));

        if (body.get("birthday") != null) {
            student.setBirthday(LocalDate.parse((String) body.get("birthday")));
        }

        studentRepo.save(student);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Student Updated");
        return messageResponse;
    }

    private String generateCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            codeBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return codeBuilder.toString();
    }
    public MessageResponse update(String email, StudentUpdateRequest request){
        StudentEntity student = studentRepo.findByEmail(email).orElseThrow(()->new RuntimeException("Not Found"));
        UserEntity users = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Not Found"));
        users.setEmail(email);
        student.setName(request.getName());
        student.setSurname(request.getSurname());
        student.setPhone(request.getPhone());
        student.setBirthday(request.getBirthday());
        studentRepo.save(student);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Update edildi");
        return messageResponse;
    }


    @Transactional
    public void delete(String email) {
        studentRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Student Not Found"));

        studentRepo.deleteByEmailNative(email);

        userRepository.deleteByEmailNative(email);
    }

    @Transactional
    public PlaylistResponse purchasePlaylist(Long playlistId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        StudentEntity student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        UserEntity user = student.getUser();

        PlaylistEntity playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (student.getPurchasedPlaylists() != null &&
                student.getPurchasedPlaylists().contains(playlist)) {
            throw new CustomException("Playlist already purchased", "BAD_REQUEST", 400);
        }


        if (user.getBalance() < playlist.getPrice()) {
            throw new CustomException("Insufficient balance", "BAD_REQUEST", 400);
        }

        user.setBalance(user.getBalance() - playlist.getPrice());

        if (student.getPurchasedPlaylists() == null) {
            student.setPurchasedPlaylists(new ArrayList<>());
        }

        student.getPurchasedPlaylists().add(playlist);

        studentRepo.save(student);
        userRepository.save(user);


        return PlaylistMapper.toDTO(playlist);
    }

}