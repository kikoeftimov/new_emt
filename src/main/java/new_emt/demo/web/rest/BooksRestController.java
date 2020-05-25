package new_emt.demo.web.rest;

import new_emt.demo.model.Book;
import new_emt.demo.service.BookService;
import new_emt.demo.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksRestController {

    private BookService bookService;
    private CategoryService categoryService;

    public BooksRestController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.findAllBooks();
    }

    @GetMapping("/{id}")
    public Book findBookById(@PathVariable Long id){
        return bookService.findById(id);
    }

    @PostMapping
    public Book save(@Valid Book book, @RequestParam(required = false) MultipartFile image) throws IOException {
        return this.bookService.saveBook(book, image);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id,
                       @Valid Book book,
                       @RequestParam(required = false) MultipartFile image) throws IOException {
        return this.bookService.editBook(id, book, image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.bookService.deleteBook(id);
    }
}