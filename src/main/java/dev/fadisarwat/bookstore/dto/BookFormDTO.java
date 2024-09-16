package dev.fadisarwat.bookstore.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class BookFormDTO {
    @NotNull
    @Size(min = 1, message = "is required")
    private String title;

    @NotNull
    @Size(min = 1, message = "is required")
    private String author;

    @NotNull
    @Size(min = 1, message = "is required")
    private String genre;

    @NotNull
    @Size(min = 1, message = "is required")
    private String overview;

    @NotNull
    @DecimalMin("0.01")
    private Double price;

    @NotNull
    @Min(1)
    private Long quantity;

    @Nullable
    private MultipartFile cover;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getOverview() {
        return overview;
    }

    public Long getPriceInPennies() {
        if(price == null) return null;

        return (long) Math.floor(price * 100);
    }

    public MultipartFile getCover() {
        return cover;
    }

    public @NotNull Long getQuantity() {
        return quantity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPrice(@NotNull Double price) {
        this.price = price;
    }

    public void setQuantity(@NotNull Long quantity) {
        this.quantity = quantity;
    }

    public void setCover(@NotNull MultipartFile cover) {
        this.cover = cover;
    }
}
