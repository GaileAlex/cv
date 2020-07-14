package ee.gaile.service.librarian;

import ee.gaile.entity.enums.Conditions;
import ee.gaile.entity.librarian.Books;
import ee.gaile.entity.models.FilterWrapper;
import ee.gaile.entity.models.SelectedFilter;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class LibrarianService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibrarianService.class);

    EntityManager em;

    //language=SQL
    private static final String QUERY = "select * from books";


    /**
     * Performs filtering based on user selected conditions.
     *
     * @return a list of books based on all filters.
     * @throws ParseException
     */
    public List<Books> filterOut(FilterWrapper filterWrapper, String condition) throws ParseException {
        StringBuilder bookQuery = new StringBuilder(QUERY).append(" where ");
        List<SelectedFilter> selectedFilterList = filterWrapper.getFilters();


        for (int i = 0; i < selectedFilterList.size(); i++) {
            if (!selectedFilterList.get(i).getSearchArea().equals("Date") && !selectedFilterList.get(i).getTextRequest().isEmpty()) {
                bookQuery.append(Conditions.getQuery(selectedFilterList.get(i).getSearchArea()))
                        .append(Conditions.getQuery(selectedFilterList.get(i).getConditionOption()))
                        .append("'")
                        .append(selectedFilterList.get(i).getTextRequest())
                        .append("'")
                        .append("   || '%' ");
            }
            if (selectedFilterList.size() - i > 1) {
                bookQuery.append(Conditions.getQuery(condition));
            }

        }

        Query query = em.createNativeQuery(bookQuery.toString(), Books.class);
        List<Books> booksList=new ArrayList<>();
        try {
           booksList = query.getResultList();

        } catch (PersistenceException pe){
            LOGGER.info("Invalid request parameters");
        }

        return booksList;
    }

    public List<Books> getAllBooks() {
        Query query = em.createNativeQuery(QUERY, Books.class);
        return query.getResultList();
    }
}
