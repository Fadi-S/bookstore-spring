package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.services.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> index() {
        return this.bookService.getBooks();
    }

    @PostMapping
    public void create(@ModelAttribute("book") Book book) {
        this.bookService.saveBook(book);
    }

    @GetMapping("/{id}")
    public Book show(@PathVariable Long id) {
        return this.bookService.getBook(id);
    }

}
