package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.dto.BookFormDTO;
import dev.fadisarwat.bookstore.json.JsonResponse;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.ImageService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

    private final BookService bookService;
    private final ImageService imageService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, editor);
    }

    public AdminBookController(BookService bookService, ImageService imageService) {
        this.bookService = bookService;
        this.imageService = imageService;
    }

    private Integer parseInteger(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }



    @PostMapping
    public BookForListDTO create(@ModelAttribute BookFormDTO bookDTO) throws IOException {
        Book book = new Book(
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getGenre(),
                bookDTO.getOverview(),
                bookDTO.getPriceInPennies(),
                bookDTO.getQuantity()
        );

        book = this.bookService.saveBook(book);

        String coverPath = imageService.saveImageToStorage(Book.coverPath(), bookDTO.getCover());
        book.setCover(coverPath);

        book = this.bookService.saveBook(book);

        return BookForListDTO.fromBook(book);
    }

    @PatchMapping(path="/{bookId}", consumes = {"multipart/form-data"})
    public BookForListDTO update(@ModelAttribute BookFormDTO bookDTO, @PathVariable Long bookId) throws IOException {
        Book book = bookService.getBook(bookId);

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setOverview(bookDTO.getOverview());
        book.setPriceInPennies(bookDTO.getPriceInPennies());
        book.setQuantity(bookDTO.getQuantity());

        book = this.bookService.saveBook(book);

        if(bookDTO.getCover() != null) {
            String coverPath = imageService.saveImageToStorage(Book.coverPath(), bookDTO.getCover());

            if (book.getCover(true) == null)
                imageService.deleteImage(Book.coverPath(), book.getCover());
            book.setCover(coverPath);
            book = this.bookService.saveBook(book);
        }

        return BookForListDTO.fromBook(book);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<JsonResponse> delete(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);

        JsonResponse response = new JsonResponse();
        response.setMessage("Book deleted successfully");

        return response.get();
    }
}
