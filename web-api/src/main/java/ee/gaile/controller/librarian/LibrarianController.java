package ee.gaile.controller.librarian;

import ee.gaile.entity.librarian.Books;
import ee.gaile.models.FilterWrapper;
import ee.gaile.service.librarian.LibrarianService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/librarian")
@AllArgsConstructor
public class LibrarianController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibrarianController.class);

    private final LibrarianService searchService;

    @GetMapping("/find-all")
    public ResponseEntity<List<Books>> findAll() {
        return new ResponseEntity<>(searchService.getAllBooks(), HttpStatus.OK);
    }

    @PostMapping(path = "/{condition}")
    public ResponseEntity<List<Books>> getBooksByFilter(@PathVariable(value = "condition") String condition,
                                                        @RequestBody FilterWrapper selectedFilters) {
        LOGGER.info("Getting list of the books");

        return new ResponseEntity<>(searchService.filterOut(selectedFilters, condition), HttpStatus.OK);
    }
}
