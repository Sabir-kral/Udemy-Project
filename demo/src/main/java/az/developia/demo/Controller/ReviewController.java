package az.developia.demo.Controller;

import az.developia.demo.Request.ReviewRequest;
import az.developia.demo.Response.MessageResponse;
import az.developia.demo.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public MessageResponse addReview(@RequestBody ReviewRequest request) {
        return reviewService.addReview(request);
    }
}
