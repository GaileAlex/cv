package ee.gaile.service.repository.librarian;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Slf4j
public class LibrarianNativeRepo {
    EntityManager em;

  public   LibrarianNativeRepo(EntityManager em){
        this.em=em;
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
}
