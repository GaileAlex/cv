package ee.gaile.service.english.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.gaile.entity.english.EnglishSentenceEntity;
import ee.gaile.models.english.EnglishSentence;
import ee.gaile.repository.english.EnglishSentenceRepository;
import ee.gaile.service.english.EnglishService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Aleksei Gaile 08-Nov-24
 */
@Service
@RequiredArgsConstructor
public class EnglishServiceImpl implements EnglishService {
    private final EnglishSentenceRepository englishSentenceRepository;
    private final ObjectMapper modelMapper;

    @Override
    public EnglishSentence getSentence() {
        EnglishSentenceEntity englishSentenceEntity = englishSentenceRepository.findByRandom();

        return modelMapper.convertValue(englishSentenceEntity, EnglishSentence.class);
    }

    private void set() {
        String fileName = "Т. II. Золотой телёнок. Роман - 2015.docx";

        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(fileName)))) {

            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            String docText = xwpfWordExtractor.getText();

            List<String> result = new ArrayList<>();

            BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
            iterator.setText(docText);
            int start = iterator.first();
            for (int end = iterator.next();
                 end != BreakIterator.DONE;
                 start = end, end = iterator.next()) {
                result.add(docText.substring(start, end));
            }

            List<String> sentences = result.stream()
                    .filter(s -> s.length() > 10)
                    .toList();

            List<EnglishSentenceEntity> englishSentenceEntities = new ArrayList<>();
            sentences.forEach(sentence -> {
                EnglishSentenceEntity englishSentenceEntity = new EnglishSentenceEntity();
                englishSentenceEntity.setSentence(sentence);
                englishSentenceEntities.add(englishSentenceEntity);
            });

            englishSentenceRepository.saveAll(englishSentenceEntities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
