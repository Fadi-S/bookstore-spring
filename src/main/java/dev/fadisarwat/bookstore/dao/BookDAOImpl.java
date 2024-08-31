package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDAOImpl implements BookDAO {

    private SessionFactory sessionFactory;

    BookDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBook(Book book) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(book);
    }

    @Override
    public Book getBook(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Book.class, id);
    }

    @Override
    public void deleteBook(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Query<Book> query = session.createQuery("delete from Book where id=:id", Book.class);

        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public List<Book> getBooks(List<Filter> filters, Sort sort, int page, int size) {
        Session session = sessionFactory.getCurrentSession();

        final List<String> fields = List.of("title", "author", "genre", "priceInPennies");
        if (sort != null && !fields.contains(sort.getField())) {
            throw new IllegalArgumentException("Invalid sort field");
        }

        filters = Optional.ofNullable(filters)
                .orElse(List.of())
                .stream()
                .filter(filter -> fields.contains(filter.getField()))
                .toList();

        StringBuilder queryString = new StringBuilder("from Book");

        if (!filters.isEmpty()) {
            queryString.append(" where ");
            for (Filter filter : filters) {
                queryString
                        .append(filter.getQuery())
                        .append(" and ");
            }
            queryString.delete(queryString.length() - 5, queryString.length());
        }

        if (sort != null) {
            queryString.append(" order by ")
                    .append(sort.getField())
                    .append(" ")
                    .append(sort.getDirection());
        }

        Query<Book> query = session.createQuery(queryString.toString(), Book.class);

        if (page > 0 && size > 0) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }

        for (Filter filter : filters) {
            query.setParameter(filter.getField(), filter.getValue());
        }

        return query.getResultList();
    }
}
