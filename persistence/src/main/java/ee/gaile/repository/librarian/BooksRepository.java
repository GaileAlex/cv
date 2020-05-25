package ee.gaile.repository.librarian;

import ee.gaile.repository.entity.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    List<Books> findAllByReleaseDate(Date releaseDate);

    @Query("select a from Books a where a.releaseDate >= :releaseDate")
    List<Books> findAllWithReleaseDateBefore(@Param("releaseDate") Date releaseDate);
}
