package ee.gaile.configuration;

import ee.gaile.entity.librarian.Books;
import ee.gaile.service.librarian.LibrarianService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Service for filling the database with data
 *
 * @author Aleksei Gaile
 */
@AllArgsConstructor
@Component
public class BooksToRepoConfig {
    private final LibrarianService librarianService;

    /**
     * Fills the database with data for a test task librarian
     */
    @PostConstruct
    public void demoData() {
        if (librarianService.getCountRow().equals(BigInteger.ZERO)) {
            for (int i = 0; i < 20; i++) {
                int year = (int) (Math.random() * 40 + 1981);
                librarianService.save(new Books("Spring MVC Cookbook", "Alex Bretet",
                        LocalDate.ofYearDay(year, 1), "Welcome to the singular" +
                        " universe of Spring MVC Cookbook. We hope you are ready for this\n" +
                        "journey that will take you through modern Spring web development practices. We have been\n" +
                        "building the cloudstreetmarket.com , a stock exchange platform with social capabilities.\n" +
                        "We are about to take you through each step of its development process."));
                librarianService.save(new Books("Apache Maven 3 Cookbook", "Srirangan",
                        LocalDate.ofYearDay(year, 1), "Apache Maven is more" +
                        " than just build automation. When positioned at the very heart of your development" +
                        " strategy, Apache Maven can become a force multiplier not just for individual developers" +
                        " but for Agile teams and managers. This book covers implementation of Apache Maven with" +
                        " popular enterprise technologies/frameworks and introduces Agile collaboration techniques" +
                        " and software engineering best practices integrated with Apache Maven."));
                librarianService.save(new Books("Spring in Action", "Walls C.",
                        LocalDate.ofYearDay(year, 1), "Spring in Action, Fifth" +
                        " Edition was written to equip you to build amazing applications\n" +
                        "using the Spring Framework, Spring Boot, and a variety of ancillary members of the\n" +
                        "Spring ecosystem. It begins by showing you how to develop web-based, databasebacked\n" +
                        "Java applications with Spring and Spring Boot. It then expands on the essentials\n" +
                        "by showing how to integrate with other applications, program using reactive\n" +
                        "types, and then break an application into discrete microservices. Finally, it discusses\n" +
                        "how to ready an application for deployment."));
                librarianService.save(new Books("Spring Data", "Pollack M., Gierke O., Risberg T.," +
                        " Brisbin J., Hunger M.",
                        LocalDate.ofYearDay(year, 1), "The Spring Data project" +
                        " was coined at Spring One 2010 and originated from a hacking\n" +
                        "session of Rod Johnson (SpringSource) and Emil Eifrem (Neo Technologies) early that\n" +
                        "year. They were trying to integrate the Neo4j graph database with the Spring Framework\n" +
                        "and evaluated different approaches. The session created the foundation for what\n" +
                        "would eventually become the very first version of the Neo4j module of Spring Data, a\n" +
                        "new SpringSource project aimed at supporting the growing interest in NoSQL data\n" +
                        "stores, a trend that continues to this day."));
                librarianService.save(new Books("Pro Spring Boot", "Felipe Gutierrez.",
                        LocalDate.ofYearDay(year, 1), "The Spring Framework was" +
                        " released as an open source project and was accepted well. It became the best open source" +
                        " framework for creating enterprise applications in a fast, reliable, and elegant way by\n" +
                        "promoting the use of design patterns and becoming one of the first frameworks to use the" +
                        " Dependency of Injection pattern. The Spring Framework has won a lot of awards in the open" +
                        " source community and keeps up to date by creating new features and embracing new" +
                        " technologies. This helps developers focus only on\n" +
                        "the application business-logic and leave the heavy lifting to the Spring Framework."));
            }
            librarianService.save(new Books("Spring MVC Cookbook", "Alex Bretet",
                    LocalDate.ofYearDay(2021, 1), "Welcome to the singular" +
                    " universe of Spring MVC Cookbook. We hope you are ready for this\n" +
                    "journey that will take you through modern Spring web development practices. We have been\n" +
                    "building the cloudstreetmarket.com , a stock exchange platform with social capabilities.\n" +
                    "We are about to take you through each step of its development process."));
        }
    }
}
