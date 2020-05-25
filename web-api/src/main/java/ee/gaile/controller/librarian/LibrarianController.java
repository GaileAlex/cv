/*
package ee.gaile.controller.librarian;

import ee.gaile.repository.entity.models.Books;
import ee.gaile.service.JsonParse;
import ee.gaile.service.RepositoryService;
import ee.gaile.service.search.SearchService;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/librarian")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@AllArgsConstructor
public class LibrarianController {
    private SearchService searchService;
    private RepositoryService repositoryService;

    @GetMapping("/find-all")
    @ResponseStatus(HttpStatus.OK)
    public List<Books> findAll() {

        return repositoryService.getAllBooks();
    }

    @PostMapping(path = "/{condition}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Books> getJson(@RequestBody String filters, @PathVariable String condition) throws ParseException, java.text.ParseException {

        return searchService.filterOut(new JsonParse(filters).parse(), condition);
    }
}
*/
