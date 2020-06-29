package ee.gaile.service.repository.librarian;

import ee.gaile.entity.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LibrarianRepository extends JpaRepository<Books, Long> {

    List<Books> findAllByReleaseDate(Date releaseDate);

    @Query("select a from Books a where a.releaseDate >= :releaseDate")
    List<Books> findAllWithReleaseDateBefore(@Param("releaseDate") Date releaseDate);
}
