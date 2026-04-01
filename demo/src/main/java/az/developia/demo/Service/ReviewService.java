package az.developia.demo.Service;

import az.developia.demo.Entity.ReviewEntity;
import az.developia.demo.Entity.StudentEntity;
import az.developia.demo.Entity.TeacherEntity;
import az.developia.demo.Entity.UserEntity;
import az.developia.demo.Repo.ReviewRepo;
import az.developia.demo.Repo.StudentRepo;
import az.developia.demo.Repo.TeacherRepo;
import az.developia.demo.Request.ReviewRequest;
import az.developia.demo.Response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final TeacherRepo teacherRepo;
    private final StudentRepo studentRepo;
    private final UserService userService;

    public MessageResponse addReview(ReviewRequest request) {

        UserEntity user = userService.getCurrentUser();

        StudentEntity student = studentRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        TeacherEntity teacher = teacherRepo.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        ReviewEntity review = new ReviewEntity();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStudent(student);
        review.setTeacher(teacher);

        reviewRepo.save(review);


        List<ReviewEntity> reviews = reviewRepo.findByTeacherId(teacher.getId());

        double average = reviews.stream()
                .mapToDouble(ReviewEntity::getRating)
                .average()
                .orElse(0.0);

        teacher.setAverageRating(average);
        teacher.setTotalReviews(reviews.size());

        teacherRepo.save(teacher);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Review elave olundu");
        return messageResponse;
    }
}