package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="book")
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String genre;

    @Column(name="price_in_pennies")
    private Long priceInPennies;

    @Column
    private Long quantity;

    @Column
    private String cover;

    @Column
    private String overview;

    @OneToMany(mappedBy="book", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Transient
    private Double averageRating;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getPriceInPennies() {
        return priceInPennies;
    }

    public void setPriceInPennies(Long priceInPennies) {
        this.priceInPennies = priceInPennies;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        if(reviews == null) {
            reviews = new ArrayList<>();
        }

        reviews.add(review);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return ((Book) obj).getId().equals(this.id);
        }

        return super.equals(obj);
    }

    public String getCover() {
        return cover == null ? "default" : cover;
    }

    public static String coverPath() {
        return "src/main/resources/static/images/books";
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String description) {
        this.overview = description;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
