package com.polarbookshop.catalogservice.demo;


import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@ConditionalOnProperty(name = "polar.testdata.enabled", havingValue = "true")
public class BookDataLoader {
    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        System.out.println("loadBookTestData is loadBookTestData.....");
        boolean needToInitBookData = true;
        for (Book book : bookRepository.findAll()) {
            needToInitBookData = false;
            System.out.println("existing books = " + book);
        }

        if (needToInitBookData) {
            System.out.println("loadBookTestData..... begin to delete all books");
            var book1 = Book.of("1234567891", "Northern Lights",
                    "Lyra Silverstar", 9.90);
            var book2 = Book.of("1234567892", "Polar Journey",
                    "Iorek Polarson", 12.90);
            bookRepository.saveAll(List.of(book1, book2));
            System.out.println("loadBookTestData is saved two books.....");
        }

    }
}