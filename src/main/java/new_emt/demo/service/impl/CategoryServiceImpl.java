package new_emt.demo.service.impl;

import new_emt.demo.model.Category;
import new_emt.demo.model.exceptions.CategoryNotFoundException;
import new_emt.demo.repository.CategoryRepository;
import new_emt.demo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public Category editCategory(Category category, Long id) {
        Category c = this.findById(id);
        c.setName(category.getName());
        c.setDescription(category.getDescription());
        return this.categoryRepository.save(c);
    }

    @Override
    public void deleteCategory(Long id) {
        this.categoryRepository.deleteById(id);
    }
}
