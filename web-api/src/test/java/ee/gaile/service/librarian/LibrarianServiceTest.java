package ee.gaile.service.librarian;

import ee.gaile.ApplicationIT;
import ee.gaile.entity.librarian.Books;
import ee.gaile.models.librarian.FilterWrapper;
import ee.gaile.models.librarian.SelectedFilter;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class LibrarianServiceTest extends ApplicationIT {

    @Autowired
    private LibrarianService librarianService;

    @Test
    void getBooksByFilter_filterOut() {
        String condition = "allConditions";
        SelectedFilter selectedFilter = new SelectedFilter("Begin with", "1", "1",
                "Date", "", "2021");
        SelectedFilter selectedFilter2 = new SelectedFilter("Contains", "1", "1",
                "Author", "Alex", "2021");
        List<SelectedFilter> selectedFilters = new ArrayList<>();
        selectedFilters.add(selectedFilter);
        selectedFilters.add(selectedFilter2);
        FilterWrapper filterWrapper = new FilterWrapper();
        filterWrapper.setSelectedFilters(selectedFilters);

        List<Books> books = librarianService.filterOut(filterWrapper, condition);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(books.get(0).getAuthor()).isEqualTo("Alex Bretet");
            softly.assertThat(books.get(0).getTitle()).isEqualTo("Spring MVC Cookbook");
            softly.assertThat(books.get(0).getReleaseDate()).isEqualTo("2021-01-01");
        });

    }

    @Test
    void getEmptyList_filterOut() {
        String condition = "allConditions";
        SelectedFilter selectedFilter = new SelectedFilter("Begin with", "1", "1",
                "Title", "", "2021");
        List<SelectedFilter> selectedFilters = new ArrayList<>();
        selectedFilters.add(selectedFilter);
        FilterWrapper filterWrapper = new FilterWrapper();
        filterWrapper.setSelectedFilters(selectedFilters);

        List<Books> books = librarianService.filterOut(filterWrapper, condition);

        SoftAssertions.assertSoftly(softly -> softly.assertThat(books.size()).isEqualTo(0));

    }

    @Test
    void getAllBooks() {
        List<Books> books = librarianService.getAllBooks();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(books.size()).isEqualTo(101));
    }

    @Test
    void getCountRow() {
        BigInteger books = librarianService.getCountRow();

        SoftAssertions.assertSoftly(softly -> softly.assertThat(books).isEqualTo(101));
    }
}
