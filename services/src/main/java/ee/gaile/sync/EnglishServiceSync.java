package ee.gaile.sync;

import ee.gaile.entity.english.EnglishSentenceEntity;
import ee.gaile.repository.english.EnglishSentenceRepository;
import ee.gaile.service.english.EnglishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnglishServiceSync implements SyncService {
    private final EnglishSentenceRepository englishSentenceRepository;
    private final EnglishService englishService;

    @Override
    public void sync() {
        List<EnglishSentenceEntity> englishSentenceEntities = englishSentenceRepository.findAllLimit();

        englishSentenceEntities.forEach(englishSentenceEntity -> {
            String translate = englishService.translate(englishSentenceEntity.getSentence());
            englishSentenceEntity.setSentenceTranslate(translate);
            englishSentenceRepository.saveAndFlush(englishSentenceEntity);
        });
    }

}
