package ee.gaile.service.repository.librarian;

import ee.gaile.entity.librarian.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LibrarianRepository extends JpaRepository<Books, Long> {
}
