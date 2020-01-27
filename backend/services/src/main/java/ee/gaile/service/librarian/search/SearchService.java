package ee.gaile.service.librarian.search;

import ee.gaile.repository.entity.models.Books;
import ee.gaile.repository.entity.models.SelectedFilter;
import ee.gaile.repository.librarian.BooksRepository;
import ee.gaile.service.librarian.search.date.DateSearchList;
import ee.gaile.service.librarian.search.date.DateSearchRepository;
import ee.gaile.service.librarian.search.text.SearchByAuthorOrTitle;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchService {

    private final BooksRepository booksRepository;
    private List<SelectedFilter> selectedFilterList;
    private final String condition;

    public SearchService(BooksRepository booksRepository, List<SelectedFilter> selectedFilterList, String condition) {
        this.booksRepository = booksRepository;
        this.selectedFilterList = selectedFilterList;
        this.condition = condition;
    }

    /**
     * Performs filtering based on user selected conditions.
     *
     * @return a list of books based on all filters.
     * @throws ParseException
     */
    public List<Books> filterOut() throws ParseException {

        List<Books> booksList = (List<Books>) booksRepository.findAll();

        if (selectedFilterList.isEmpty()) {
            booksList.clear();
            return booksList;
        }

        if (condition.equals("allConditions") || condition.equals("noneOfTheCondition")) {

            List<Books> tempList = new ArrayList<>(booksList);

            for (SelectedFilter selectedFilter : selectedFilterList) {

                switch (selectedFilter.getSearchArea()) {
                    case "Author":
                    case "Title":
                        booksList = new SearchByAuthorOrTitle(booksList, selectedFilter).search();
                        break;
                    case "Date":
                        booksList = new DateSearchList(booksList, selectedFilter).search();
                        break;
                }
            }

            if (condition.equals("noneOfTheCondition")) {
                tempList.removeAll(booksList);
                booksList.clear();
                booksList.addAll(tempList);
            }
        } else {

            Set<Books> temp = new HashSet<>();

            for (SelectedFilter selectedFilter : selectedFilterList) {

                switch (selectedFilter.getSearchArea()) {
                    case "Author":
                    case "Title":
                        booksList = new SearchByAuthorOrTitle(booksList, selectedFilter).search();
                        break;
                    case "Date":
                        booksList = new DateSearchRepository(booksRepository, selectedFilter).search();
                        break;
                }
                temp.addAll(booksList);
            }
            booksList.clear();
            booksList.addAll(temp);
        }
        return booksList;
    }
}
