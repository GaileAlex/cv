package ee.gaile.repository.librarian;

import ee.gaile.repository.entity.models.Books;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BooksRepository extends CrudRepository<Books, Long> {

    List<Books> findAllByReleaseDate(Date releaseDate);

    @Query("select a from Books a where a.releaseDate >= :releaseDate")
    List<Books> findAllWithReleaseDateBefore(@Param("releaseDate") Date releaseDate);
}
