package new_emt.demo.web.rest;

import new_emt.demo.model.Category;
import new_emt.demo.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return this.categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id){
        return this.categoryService.findById(id);
    }

    @PostMapping
    public Category saveCategory(@Valid Category category){
        return this.categoryService.saveCategory(category);
    }

    @PatchMapping("/{id}/edit")
    public Category editCategory(@Valid Category category, @PathVariable Long id){
        return this.categoryService.editCategory(category, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        this.categoryService.deleteCategory(id);
    }
}
