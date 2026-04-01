package az.developia.demo.Controller;

import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Request.*;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.StudentResponse;
import az.developia.demo.Response.TeacherResponse;
import az.developia.demo.Service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService service;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deletePlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletePlaylist(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponse> updatePlaylist(
            @PathVariable Long id,
            @RequestBody PlaylistUpdateRequest request) {

        return ResponseEntity.ok(service.updatePlaylist(id, request));
    }
    @PostMapping("/register")
    @Operation(summary = "Teacher register API")
    public MessageResponse register(@RequestBody @Valid TeacherRequest request) throws MessagingException {
        return service.register(request);
    }
    //    @PostMapping("/verify")
//    public MessageResponse verify(@RequestBody AddVerifyRequest request) {
//        return service.verify(request);
//    }
    @GetMapping("/profile")
    public TeacherResponse profile(){
        return service.profile();
    }

    @GetMapping("/my-students")
    public List<StudentResponse> myStudents() {
        return service.myStudents();
    }
    @PutMapping("/profile")
    public MessageResponse update(@RequestParam String email, @RequestBody TeacherUpdateRequest request){
        return service.update(email,request);
    }
    @DeleteMapping("/delete")
    public void delete(@RequestParam String email){
        service.delete(email);
    }
}