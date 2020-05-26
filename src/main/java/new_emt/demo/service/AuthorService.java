package new_emt.demo.service;

import new_emt.demo.model.Author;

import java.util.List;

public interface AuthorService {

    Author findById(Long id);

    List<Author> findAll();

    Author save(Author author);

    void deleteById(Long id);
}
