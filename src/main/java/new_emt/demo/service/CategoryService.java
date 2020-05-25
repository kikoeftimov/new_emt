package new_emt.demo.service;

import new_emt.demo.model.Category;

import java.util.List;

public interface CategoryService {

    Category findById(Long id);

    List<Category> findAll();

    Category saveCategory(Category category);

    Category editCategory(Category category, Long id);

    void deleteCategory(Long id);

}
