package ee.gaile.service.librarian.search.date;

import ee.gaile.entity.librarian.Books;
import ee.gaile.entity.models.SelectedFilter;
import ee.gaile.service.repository.librarian.LibrarianRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DateSearchRepository {
    private SelectedFilter selectedFilter;
    private final LibrarianRepository booksRepository;

    public DateSearchRepository(LibrarianRepository booksRepository, SelectedFilter selectedFilter) {
        this.booksRepository = booksRepository;
        this.selectedFilter = selectedFilter;
    }

    /**
     * Searches for books in the repository according to user conditions
     *
     * @return a list of books based on a date selected by the user
     * @throws ParseException
     */
    public List<Books> search() throws ParseException {
        List<Books> foundBooks = new ArrayList<>();

        switch (selectedFilter.getConditionOption()) {
            case "Contains":
                foundBooks = booksRepository.findAllByReleaseDate(new SimpleDateFormat("yyyy").
                        parse(selectedFilter.getYear()));
                break;
            case "Begin with":
                foundBooks = booksRepository.findAllWithReleaseDateBefore(new SimpleDateFormat("yyyy").
                        parse(selectedFilter.getYear()));
                break;
        }
        return foundBooks;
    }
}
