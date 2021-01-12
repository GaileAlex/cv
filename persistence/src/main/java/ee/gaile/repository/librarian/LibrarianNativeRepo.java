package ee.gaile.repository.librarian;

import ee.gaile.entity.librarian.Books;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Slf4j
public class LibrarianNativeRepo {
    EntityManager em;

    public LibrarianNativeRepo(EntityManager em) {
        this.em = em;
    }

    public <T> List<T> getResult(String queryText, Class resultClass) {

        try {
            Query query = em.createNativeQuery(queryText, resultClass);
            return query.getResultList();
        } catch (Exception e) {
            log.error(String.format("Invalid request parameters for %s", resultClass), e);
            throw e;
        }
    }

    @Transactional
    public void save(Books entityClass) {
        em.persist(entityClass);
    }

    public BigInteger getCount(String queryText) {
        Query query = em.createNativeQuery(queryText);
        return (BigInteger) query.getSingleResult();
    }
}
