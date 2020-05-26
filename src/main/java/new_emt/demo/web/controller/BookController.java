package new_emt.demo.web.controller;

import new_emt.demo.model.Author;
import new_emt.demo.model.Book;
import new_emt.demo.model.Category;
import new_emt.demo.model.exceptions.BookIsAlreadyInShoppingCartException;
import new_emt.demo.repository.AuthorRepository;
import new_emt.demo.service.AuthorService;
import new_emt.demo.service.BookService;
import new_emt.demo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    public BookController(BookService bookService, CategoryService categoryService, AuthorService authorService, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public String getBooksPage(Model model){
        List<Book> books = this.bookService.findAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("add-new")
    public String createNewBook(Model model){
        List<Category> categories = this.categoryService.findAll();
        List<Author> authors = this.authorService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", this.authorRepository.findAll());
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable Long id){
        try {
            Book book = this.bookService.findById(id);
            List<Category> categories = this.categoryService.findAll();
            List<Author> authors = this.authorService.findAll();
            model.addAttribute("book", book);
            model.addAttribute("categories", categories);
            model.addAttribute("authors", this.authorRepository.findAll());
            return "add-book";

        }catch (RuntimeException ex){
            return "redirect:/books?error=" + ex.getLocalizedMessage();
        }
    }

    @PostMapping
    public String saveBook(@Valid Book book,
             BindingResult bindingResult,
             @RequestParam MultipartFile image,
             Model model) throws IOException
    {
        if(bindingResult.hasErrors()){
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("authors", this.authorRepository.findAll());
            return "add-book";
        }
        try {
            this.bookService.saveBook(book, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/books";
    }



    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id){
        try {
            this.bookService.deleteBook(id);
        }
        catch (BookIsAlreadyInShoppingCartException ex) {
            return String.format("redirect:/books?error=%s", ex.getMessage());
        }
        return "redirect:/books";
    }
}
