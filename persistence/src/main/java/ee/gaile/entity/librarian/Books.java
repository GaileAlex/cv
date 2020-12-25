package ee.gaile.entity.librarian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Books")
@Table(name = "books")
public class Books {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_title", length = 200)
    private String title;

    @Column(name = "book_article", length = 200)
    private String author;

    @Column(name = "book_date")
    private LocalDate releaseDate;

    @Column(name = "book_text", length = 2000)
    private String bookText;

    public Books(String title, String author, LocalDate releaseDate, String bookText) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.bookText = bookText;
    }
}
