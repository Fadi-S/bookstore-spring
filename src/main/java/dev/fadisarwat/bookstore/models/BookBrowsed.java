package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="book_user_browsed")
public class BookBrowsed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "browsed_at")
    private Date browsedAt;

    public BookBrowsed() {}

    public BookBrowsed(Book book, User user) {
        this.book = book;
        this.user = user;
        this.browsedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getBrowsedAt() {
        return browsedAt;
    }

    public void setBrowsedAt(Date browsedAt) {
        this.browsedAt = browsedAt;
    }
}
