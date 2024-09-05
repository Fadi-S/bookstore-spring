package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDAOImpl implements BookDAO {

    private final SessionFactory sessionFactory;

    BookDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book saveBook(Book book) {
        Session session = sessionFactory.getCurrentSession();

        return session.merge(book);
    }

    @Override
    public Optional<Object[]> getBook(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Query<Object[]> query = session.createQuery(
                "SELECT b, AVG(r.rating) as averageRating " +
                "FROM Book b LEFT JOIN Review r ON b.id = r.book.id " +
                "WHERE b.id = :bookId " +
                "GROUP BY b.id", Object[].class);

        query.setParameter("bookId", id);

        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    @Override
    public void deleteBook(Long id) {
        Session session = sessionFactory.getCurrentSession();

        MutationQuery query = session.createMutationQuery("delete from Book where id=:id");

        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public void saveBooks(List<Book> books) {
        Session session = sessionFactory.getCurrentSession();

        int count = 0;
        for (Book book : books) {
            this.saveBook(book);

            if ( ++count % 20 == 0 ) {
                session.flush();
                session.clear();
            }
        }
    }

    @Override
    public List<Book> getBooks(List<Filter> filters, Sort sort, int page, int size) {
        Session session = sessionFactory.getCurrentSession();

        final List<String> fields = List.of("id", "title", "author", "genre", "priceInPennies");
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

    @Override
    public Boolean isPurchasedByUser(Long bookId, Long userId) {
        Session session = sessionFactory.getCurrentSession();

        Query<Long> query = session.createNativeQuery("Select orders.user_id from book " +
                "left join `book_order` on book_order.book_id=book.id " +
                "left join `orders` on orders.id=book_order.order_id " +
                "where book.id=:bookId and orders.user_id=:userId", Long.class);
        query.setParameter("bookId", bookId);
        query.setParameter("userId", userId);

        return ! query.getResultList().isEmpty();
    }
}
