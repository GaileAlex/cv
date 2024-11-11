package ee.gaile.repository.english;

import ee.gaile.entity.english.EnglishSentenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksei Gaile 08-Nov-24
 */
@Repository
public interface EnglishSentenceRepository extends JpaRepository<EnglishSentenceEntity, Long> {

    @Query(value = "select * from english_sentences " +
            " order by random() limit 1", nativeQuery = true)
    EnglishSentenceEntity findByRandom();

    @Query(value = "select * from english_sentences " +
            " where sentence_translate is null limit 1000", nativeQuery = true)
    List<EnglishSentenceEntity> findAllLimit();

}
