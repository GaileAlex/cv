package ee.gaile.repository.english;

import ee.gaile.entity.english.EnglishSentenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Aleksei Gaile 08-Nov-24
 */
@Repository
public interface EnglishSentenceRepository extends JpaRepository<EnglishSentenceEntity, Long> {

    @Query(value = "select * from english_sentences " +
            " order by random() limit 1", nativeQuery = true)
    EnglishSentenceEntity findByRandom();

}
