package az.developia.demo.Service;

import az.developia.demo.Entity.CategoryEntity;
import az.developia.demo.Repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public List<CategoryEntity> getAll() {
        return categoryRepo.findAll();
    }
}