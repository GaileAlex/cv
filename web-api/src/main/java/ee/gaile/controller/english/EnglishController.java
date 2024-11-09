package ee.gaile.controller.english;

import ee.gaile.models.english.EnglishSentence;
import ee.gaile.service.english.EnglishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/english")
@RequiredArgsConstructor
@Tag(name = "EnglishController", description = "English controller")
public class EnglishController {
    private final EnglishService englishService;

    @GetMapping
    @Operation(summary = "Service for english sentences")
    public ResponseEntity<EnglishSentence> findAll() {
        return new ResponseEntity<>(englishService.getSentence(), HttpStatus.OK);
    }

}
