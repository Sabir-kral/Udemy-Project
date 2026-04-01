package az.developia.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String phone;
    private String email;
    private LocalDate birthday;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String repeatPassword;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference
    @ToString.Exclude    // <--- Add this
    @EqualsAndHashCode.Exclude // <--- Add this
    private TeacherEntity teacher;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;

    @ManyToMany
    @JoinTable(
            name = "student_playlists",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<PlaylistEntity> purchasedPlaylists;

}