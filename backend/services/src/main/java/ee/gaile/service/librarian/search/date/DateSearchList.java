package ee.gaile.service.librarian.search.date;


import ee.gaile.repository.entity.models.Books;
import ee.gaile.repository.entity.models.SelectedFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateSearchList {

    private List<Books> booksListForSearch;
    private SelectedFilter selectedFilter;

    public DateSearchList(List<Books> booksListForSearch, SelectedFilter selectedFilter) {
        this.booksListForSearch = booksListForSearch;
        this.selectedFilter = selectedFilter;
    }

    /**
     * Searches for books in the list according to user conditions
     *
     * @return a list of books based on a date selected by the user
     * @throws ParseException
     */
    public List<Books> search() throws ParseException {

        List<Books> foundBooks = new ArrayList<>();

        switch (selectedFilter.getConditionOption()) {

            case "Contains":
                for (Books books : booksListForSearch) {

                    if (new SimpleDateFormat("yyyy").parse(String.valueOf(books.getReleaseDate())).
                            equals(new SimpleDateFormat("yyyy").parse(selectedFilter.getYear()))) {
                        foundBooks.add(books);
                    }
                }
                break;

            case "Begin with":
                for (Books books : booksListForSearch) {
                    Date filterDate = new SimpleDateFormat("yyyy").parse(selectedFilter.getYear());
                    Date bookDate = new SimpleDateFormat("yyyy").parse(String.valueOf(books.getReleaseDate()));

                    if (filterDate.before(bookDate) || filterDate.equals(bookDate)) {
                        foundBooks.add(books);
                    }
                }
                break;
        }
        return foundBooks;
    }
}
