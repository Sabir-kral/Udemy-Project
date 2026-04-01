package az.developia.demo.Controller;

import az.developia.demo.Entity.PlaylistEntity;
import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Request.AddVerifyRequest;
import az.developia.demo.Request.PlaylistUpdateRequest;
import az.developia.demo.Request.StudentRequest;
import az.developia.demo.Request.StudentUpdateRequest;
import az.developia.demo.Response.LoginResponse;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Response.PlaylistResponse;
import az.developia.demo.Response.StudentResponse;
import az.developia.demo.Service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;
    public MessageResponse register(@RequestBody @Valid StudentRequest studentRequest) throws MessagingException {
        return service.register(studentRequest);
    }

        @PutMapping("/profile")
        public MessageResponse update(@RequestParam String email, @RequestBody StudentUpdateRequest request){
            return service.update(email,request);
        }
        @DeleteMapping("/delete")
        public void delete(@RequestParam String email){
            service.delete(email);
        }
        @GetMapping("/profile")
        public StudentResponse profile(){
                return service.profile();
            }


        @PostMapping("/playlists/{id}/buy")
        public ResponseEntity<PlaylistResponse> purchase(@PathVariable Long id) {
            return ResponseEntity.ok(service.purchasePlaylist(id));
        }





    }