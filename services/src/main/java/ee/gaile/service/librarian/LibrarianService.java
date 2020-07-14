package ee.gaile.service.librarian;

import ee.gaile.entity.enums.Conditions;
import ee.gaile.entity.librarian.Books;
import ee.gaile.entity.models.FilterWrapper;
import ee.gaile.entity.models.SelectedFilter;
import ee.gaile.service.repository.librarian.LibrarianNativeRepo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;


@Service
public class LibrarianService extends LibrarianNativeRepo {

    public LibrarianService(EntityManager em) {
        super(em);
    }

    //language=SQL
    private static final String QUERY = "select * from books ";

    /**
     * Performs filtering based on user selected conditions.
     *
     * @return a list of books based on all filters.
     */
    public List<Books> filterOut(FilterWrapper filterWrapper, String condition) {
        StringBuilder bookQuery = new StringBuilder(QUERY).append(" where ");
        List<SelectedFilter> selectedFilterList = filterWrapper.getFilters();

        for (int i = 0; i < selectedFilterList.size(); i++) {
            if (!selectedFilterList.get(i).getSearchArea().equals("Date")
                    && !selectedFilterList.get(i).getTextRequest().isEmpty()) {
                bookQuery.append(Conditions.getQuery(selectedFilterList.get(i).getSearchArea()))
                        .append(Conditions.getQuery(selectedFilterList.get(i).getConditionOption()))
                        .append("'")
                        .append(selectedFilterList.get(i).getTextRequest())
                        .append("'")
                        .append("   || '%' ");

                if (selectedFilterList.size() - i > 1) {
                    bookQuery.append(Conditions.getQuery(condition));
                }
            }
        }
        List<Books> booksList = getResult(bookQuery.toString(), Books.class);

        return booksList;
    }

    public List<Books> getAllBooks() {
        return getResult(QUERY, Books.class);
    }
}
