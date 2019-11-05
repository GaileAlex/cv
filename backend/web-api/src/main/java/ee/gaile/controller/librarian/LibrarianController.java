package ee.gaile.controller.librarian;

import ee.gaile.repository.entity.models.Books;
import ee.gaile.repository.librarian.BooksRepository;
import ee.gaile.service.librarian.JsonParse;
import ee.gaile.service.librarian.search.SearchService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/librarian",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class LibrarianController {

    private SearchService searchService;
    private BooksRepository booksRepository;

    @Autowired
    public LibrarianController(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @GetMapping(produces = "application/json")
    public List<Books> getSearchResults() throws java.text.ParseException {

        return searchService.filterOut();
    }

    @PostMapping(path = "/{condition}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity getJson(@RequestBody String filters, @PathVariable String condition) throws ParseException {

        searchService = new SearchService(booksRepository, new JsonParse(filters).parse(), condition);

        return new ResponseEntity(HttpStatus.OK);
    }
}