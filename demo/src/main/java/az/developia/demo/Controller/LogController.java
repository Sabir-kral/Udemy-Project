package az.developia.demo.Controller;

import az.developia.demo.Response.LogResponse;
import az.developia.demo.Service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    @GetMapping
    public List<LogResponse> Logs(){
        return logService.Logs();
    }
}