package az.developia.demo.Controller;

import az.developia.demo.Entity.CategoryEntity;
import az.developia.demo.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("categories")
    public List<CategoryEntity> getAllCategories() {
        return categoryService.getAll();
    }
}