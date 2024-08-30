package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;

@Entity
@Table(name="review")
public class Review {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer rating;

    @Column
    private String body;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
