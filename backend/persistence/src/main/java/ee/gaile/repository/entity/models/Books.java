package ee.gaile.repository.entity.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private Date releaseDate;

    @Column(length = 10485760)
    private String bookText;

    public Books(String title, String author, Date releaseDate, String bookText) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.bookText = bookText;
    }
}
