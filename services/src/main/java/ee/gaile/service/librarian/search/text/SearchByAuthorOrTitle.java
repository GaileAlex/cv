package ee.gaile.service.librarian.search.text;



import ee.gaile.entity.librarian.Books;
import ee.gaile.entity.models.SelectedFilter;

import java.util.ArrayList;
import java.util.List;

public class SearchByAuthorOrTitle {

    private List<Books> booksListForSearch;
    private SelectedFilter selectedFilter;

    public SearchByAuthorOrTitle(List<Books> booksListForSearch, SelectedFilter selectedFilter) {
        this.booksListForSearch = booksListForSearch;
        this.selectedFilter = selectedFilter;
    }

    /**
     * Searches for books in the list in accordance with the terms of the user by author and title
     *
     * @return a list of books according to search terms
     */
    public List<Books> search() {

        List<Books> foundBooks = new ArrayList<>();

        switch (selectedFilter.getConditionOption()) {

            case "Contains":
                for (Books books : booksListForSearch) {

                    if (selectedFilter.getSearchArea().equals("Author")) {

                        if (books.getAuthor().toLowerCase().contains(selectedFilter.getTextRequest().toLowerCase())) {
                            foundBooks.add(books);
                        }
                    } else {

                        if (books.getTitle().toLowerCase().contains(selectedFilter.getTextRequest().toLowerCase())) {
                            foundBooks.add(books);
                        }
                    }
                }
                break;

            case "Begin with":
                for (Books books : booksListForSearch) {

                    if (selectedFilter.getSearchArea().equals("Author")) {

                        if (books.getAuthor().toLowerCase().startsWith(selectedFilter.getTextRequest().toLowerCase())) {
                            foundBooks.add(books);
                        }
                    } else {

                        if (books.getTitle().toLowerCase().startsWith(selectedFilter.getTextRequest().toLowerCase())) {
                            foundBooks.add(books);
                        }
                    }
                }
                break;
        }
        return foundBooks;
    }
}
