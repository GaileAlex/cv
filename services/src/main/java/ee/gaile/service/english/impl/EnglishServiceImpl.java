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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    private static final String GOOGLE_URL_API = "https://script.google.com/macros/s/AKfycbySZ0HD-ypIgQ8ZsaJCOakaiC_AlvxxqpVvsZIi1fA0KqZne0V4D3o59oLlWlydPjo5/exec";

    @Override
    public EnglishSentence getSentence() {
        EnglishSentenceEntity englishSentenceEntity = englishSentenceRepository.findByRandom();
        EnglishSentence content = modelMapper.convertValue(englishSentenceEntity, EnglishSentence.class);
        String result;

        if (content.getSentenceTranslate() == null) {
            result = translate(content.getSentence());

            content.setSentenceTranslate(result);
            englishSentenceEntity.setSentenceTranslate(result);
            englishSentenceRepository.save(englishSentenceEntity);
        }

        return content;
    }

    private String translate(String text) {
        try {
            String urlStr = GOOGLE_URL_API +
                    "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                    "&target=" + "en" +
                    "&source=" + "ru";
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
