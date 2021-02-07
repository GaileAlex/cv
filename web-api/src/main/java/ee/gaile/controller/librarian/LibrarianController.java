package ee.gaile.controller.librarian;

import ee.gaile.entity.librarian.Books;
import ee.gaile.models.librarian.FilterWrapper;
import ee.gaile.service.librarian.LibrarianService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

/**
 * REST service controller for retrieving data to the tables of the librarian test task
 *
 * @author Aleksei Gaile
 */
@RestController
@AllArgsConstructor
@RequestMapping(API_V1_PREFIX + "/librarian")
public class LibrarianController {
    private final LibrarianService searchService;

    @GetMapping("/find-all")
    public ResponseEntity<List<Books>> findAll() {
        return new ResponseEntity<>(searchService.getAllBooks(), HttpStatus.OK);
    }

    @PostMapping(path = "/{condition}")
    public ResponseEntity<List<Books>> getBooksByFilter(@PathVariable(value = "condition") String condition,
                                                        @RequestBody FilterWrapper selectedFilters) {
        return new ResponseEntity<>(searchService.filterOut(selectedFilters, condition), HttpStatus.OK);
    }
}
