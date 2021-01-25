package ee.gaile.service.librarian;

import ee.gaile.entity.librarian.Books;
import ee.gaile.models.librarian.FilterWrapper;
import ee.gaile.models.librarian.SelectedFilter;
import ee.gaile.repository.librarian.LibrarianNativeRepo;
import ee.gaile.enums.Conditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class LibrarianService extends LibrarianNativeRepo {
    public LibrarianService(EntityManager em) {
        super(em);
    }

    //language=SQL
    private static final String QUERY = "select * from books ";

    //language=SQL
    private static final String COUNT_ROW = "SELECT count(*) AS count FROM books ";

    /**
     * Performs filtering based on user selected conditions.
     *
     * @return a list of books based on all filters.
     */
    public List<Books> filterOut(FilterWrapper filterWrapper, String condition) {
        StringBuilder bookQuery = new StringBuilder(QUERY).append(" where ");
        List<SelectedFilter> selectedFilterList = filterWrapper.getSelectedFilters();

        for (SelectedFilter selectedFilter : filterWrapper.getSelectedFilters()) {
            if ((selectedFilter.getSearchArea().equals("Author") || selectedFilter.getSearchArea().equals("Title"))
                    && selectedFilter.getTextRequest().equals("")) {
                return Collections.emptyList();
            }
        }

        for (int i = 0; i < selectedFilterList.size(); i++) {

            // Author, Title
            if (!selectedFilterList.get(i).getSearchArea().equals("Date")
                    && !selectedFilterList.get(i).getTextRequest().equals("")) {

                getByText(bookQuery, selectedFilterList.get(i));
            }
            // Date
            if (selectedFilterList.get(i).getSearchArea().equals("Date")) {

                getByDate(bookQuery, selectedFilterList.get(i));
            }
            if (selectedFilterList.size() - i > 1 && (!selectedFilterList.get(i).getTextRequest().equals("")
                    || selectedFilterList.get(i).getSearchArea().equals("Date"))) {
                bookQuery.append(Conditions.getQuery(condition));
            }

        }
        if (bookQuery.toString().trim().endsWith("and") || bookQuery.toString().trim().endsWith("or")) {
            String result = StringUtils.substring(bookQuery.toString().trim(), 0, -3);
            return getResult(result, Books.class);
        }
        return getResult(bookQuery.toString(), Books.class);
    }

    public List<Books> getAllBooks() {
        return getResult(QUERY, Books.class);
    }

    public BigInteger getCountRow() {
        return getCount(COUNT_ROW);
    }

    private void getByText(StringBuilder bookQuery, SelectedFilter selectedFilter) {
        bookQuery.append(Conditions.getQuery(selectedFilter.getSearchArea()))
                .append(Conditions.getQuery(selectedFilter.getConditionOption()))
                .append(selectedFilter.getTextRequest())
                .append("'")
                .append("   || '%' ");
    }

    private void getByDate(StringBuilder bookQuery, SelectedFilter selectedFilter) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");

        String stringDate = selectedFilter.getDay() +
                "/" +
                selectedFilter.getMonth() +
                "/" +
                selectedFilter.getYear();

        LocalDate localDate = LocalDate.parse(stringDate, df);

        // TODO: refactoring front needed
        if (selectedFilter.getConditionOption().equals("Begin with")) {
            selectedFilter.setConditionOption("Date from");
        }

        bookQuery.append(Conditions.getQuery(selectedFilter.getSearchArea()))
                .append(Conditions.getQuery(selectedFilter.getConditionOption()))
                .append(localDate)
                .append("'");
    }
}
