package ee.gaile.controller.librarian;

import ee.gaile.entity.models.Books;
import ee.gaile.service.JsonParse;
import ee.gaile.service.MindlyService;
import ee.gaile.service.search.SearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/librarian")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@AllArgsConstructor
public class LibrarianController {
    private final SearchService searchService;
    private final MindlyService repositoryService;

    @GetMapping("/find-all")
    public List<Books> findAll() {

        return repositoryService.getAllBooks();
    }

    @PostMapping(path = "/{condition}")
    public List<Books> getJson(@RequestBody String filters, @PathVariable String condition) throws ParseException, java.text.ParseException {

        return searchService.filterOut(new JsonParse(filters).parse(), condition);
    }
}
