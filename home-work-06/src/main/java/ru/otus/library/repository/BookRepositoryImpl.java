package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(@NonNull Book book) {
        if (book.getId() == null) {
            em.persist(book);
            log.info("Created a new book with ID = {}", book.getId());
            return book;
        } else {
            log.info("Updated book with ID = {}", book.getId());
            return em.merge(book);
        }
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre", Book.class);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        log.info("Deleted book with id = {}", id);
    }

    @Override
    public void deleteAll() {
        em.createQuery("delete from Book").executeUpdate();
        log.info("Deleted all books");

    }
}
