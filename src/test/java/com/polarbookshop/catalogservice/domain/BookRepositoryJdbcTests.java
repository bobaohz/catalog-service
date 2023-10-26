package com.polarbookshop.catalogservice.domain;

import java.util.Iterator;
import java.util.Optional;
import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    private static Logger LOGGER = LoggerFactory.getLogger(BookRepositoryJdbcTests.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234561237";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90);
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());

        var book2 = Book.of("1234561238", "Title2", "Author2", 12.92);
        jdbcAggregateTemplate.insert(book2);

        Iterator<Book> iterator = bookRepository.findAll().iterator();
        int count = 0;
        while(iterator.hasNext()){
            LOGGER.info("book {}", iterator.next());
            count++;
        }
        Assertions.assertEquals(2, count);
    }

    @Test
    void whenCreateBookNotAuthenticatedThenNoAuditMetadata() {
        var bookToCreate = Book.of("1232343456", "Title",
                "Author", 12.90, "Polarsophia");
        var createdBook = bookRepository.save(bookToCreate);

        assertThat(createdBook.createdBy()).isNull();
        assertThat(createdBook.lastModifiedBy()).isNull();
    }

    @Test
    @WithMockUser("bob")
    void whenCreateBookAuthenticatedThenAuditMetadata() {
        var bookToCreate = Book.of("1232343457", "Title",
                "Author", 12.90, "Polarsophia");
        var createdBook = bookRepository.save(bookToCreate);

        assertThat(createdBook.createdBy())
                .isEqualTo("bob");
        assertThat(createdBook.lastModifiedBy())
                .isEqualTo("bob");
    }
}