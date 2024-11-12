package ee.gaile.sync;

import ee.gaile.entity.english.EnglishSentenceEntity;
import ee.gaile.repository.english.EnglishSentenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnglishServiceSync implements SyncService {
    private final EnglishSentenceRepository englishSentenceRepository;

    private static final String GOOGLE_URL_API = "https://script.google.com/macros/s/AKfycbySZ0HD-ypIgQ8ZsaJCOakaiC_AlvxxqpVvsZIi1fA0KqZne0V4D3o59oLlWlydPjo5/exec";

    @Override
    public void sync() {
        List<EnglishSentenceEntity> englishSentenceEntities = englishSentenceRepository.findAllLimit();

        for (EnglishSentenceEntity englishSentenceEntity : englishSentenceEntities) {
            String translate = translate(englishSentenceEntity.getSentence());
            if (translate.contains("DOCTYPE")) {
                break;
            }
            englishSentenceEntity.setSentenceTranslate(translate);
            englishSentenceRepository.saveAndFlush(englishSentenceEntity);
        }
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

            con.disconnect();
            in.close();

            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
