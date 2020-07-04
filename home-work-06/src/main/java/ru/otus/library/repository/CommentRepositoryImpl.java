package ru.otus.library.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            log.info("Created a new comment with ID = {}", comment.getId());
            return comment;
        } else {
            log.info("Updated comment with ID = {}", comment.getId());
            return em.merge(comment);
        }
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public void deleteById(long id) {
        //em.remove(em.find(Comment.class, id));
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        log.info("Deleted comment with id = {}", id);
    }

    @Override
    public void deleteAll() {
        em.createQuery("delete from Comment").executeUpdate();
        log.info("Deleted all comments");
    }
}
