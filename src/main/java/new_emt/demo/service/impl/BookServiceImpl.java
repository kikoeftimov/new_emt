package new_emt.demo.service.impl;

import new_emt.demo.model.Book;
import new_emt.demo.model.Category;
import new_emt.demo.model.exceptions.BookIsAlreadyInShoppingCartException;
import new_emt.demo.model.exceptions.BookNotFoundException;
import new_emt.demo.repository.BookRepository;
import new_emt.demo.service.BookService;
import new_emt.demo.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public List<Book> findAllBooks() {
        return this.bookRepository.findAll();
    }

    @Override
    public Book saveBook(Book book, MultipartFile image) throws IOException {
        Category category = this.categoryService.findById(book.getCategory().getId());
        book.setCategory(category);
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            book.setImageBase64(base64Image);
        }
        return this.bookRepository.save(book);
    }

    @Override
    public Book editBook(Long id, Book book, MultipartFile image) throws IOException {
        Book b = this.findById(id);
        Category category = this.categoryService.findById(book.getCategory().getId());
        b.setCategory(category);
        b.setName(book.getName());
        b.setNumberOfBooks(book.getNumberOfBooks());
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            b.setImageBase64(base64Image);
        }
        return this.bookRepository.save(b);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = this.findById(id);
        if(book.getShoppingCarts().size() > 0){
            throw new BookIsAlreadyInShoppingCartException(book.getName());
        }
        this.bookRepository.deleteById(id);
    }
}
