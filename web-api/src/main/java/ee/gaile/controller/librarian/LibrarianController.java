package ee.gaile.controller.librarian;

import ee.gaile.entity.librarian.Books;
import ee.gaile.service.librarian.JsonParse;
import ee.gaile.service.mindly.MindlyService;
import ee.gaile.service.librarian.SearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_V1_PREFIX + "/librarian")
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
