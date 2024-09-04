package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.FilterType;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ImageService imageService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, editor);
    }

    public BookController(BookService bookService, ImageService imageService) {
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

    private void sleep(TimeUnit unit, int time) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public List<BookForListDTO> index(@RequestParam Map<String, String> allParams) {
        int size = parseInteger(allParams.get("size"), 10);
        if (size > 100) size = 100;

        int page = parseInteger(allParams.get("page"), 1);

        Map<String, String> filterParams = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("filters["))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Filter> filters = new ArrayList<>();

        for (Map.Entry<String, String> entry : filterParams.entrySet()) {
            String key = entry.getKey().substring(8, entry.getKey().length() - 1);
            String value = entry.getValue();

            FilterType filterType = FilterType.EQUALS;
            if (value.startsWith("~")) {
                value = value.substring(1);
                filterType = FilterType.FUZZY;
            } else if (value.startsWith(">")) {
                value = value.substring(1);
                filterType = FilterType.GREATER_THAN;
            } else if (value.startsWith("<")) {
                value = value.substring(1);
                filterType = FilterType.LESS_THAN;
            }

            filters.add(new Filter(key, value, filterType));
        }

        Sort sort = new Sort("id", Sort.Direction.DESC);

        if (allParams.containsKey("sort")) {
            String sortParam = allParams.get("sort");
            sort.setDirection(sortParam.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC);
            sort.setField(sortParam.replaceFirst("[-+]", ""));
        }

        // TODO: Remove after testing
//        sleep(TimeUnit.SECONDS, 3);


        return this.bookService.getBooks(filters, sort , page, size);
    }

    @GetMapping("/images/{image}")
    public byte[] getCover(@PathVariable String image, HttpServletResponse response) throws IOException {
        String imageDirectory = Book.coverPath();

        response.setHeader("Cache-Control", "public, max-age=86400");

        if (image.equals("default")) {
            return imageService.getImage("src/main/resources", "book_default.png");
        }

        return imageService.getImage(imageDirectory, image);
    }

    @GetMapping("/{id}")
    public Book show(@PathVariable Long id) {
        // TODO: Remove after testing
//        sleep(TimeUnit.SECONDS, 3);
        return this.bookService.getBook(id);
    }


    @PostMapping
    public void create(@ModelAttribute("book") Book book) {
        this.bookService.saveBook(book);
    }
}
