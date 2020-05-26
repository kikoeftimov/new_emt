package new_emt.demo.service;

import new_emt.demo.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    Book findById(Long id);

    List<Book> findAllBooks();

    Book saveBook(Book book, MultipartFile image) throws IOException;

    Book editBook(Long id, Book book, MultipartFile image) throws IOException;

    void deleteBook(Long id);
    
}
