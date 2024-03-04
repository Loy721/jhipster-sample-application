package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AuthorTestSamples.*;
import static com.mycompany.myapp.domain.BookTestSamples.*;
import static com.mycompany.myapp.domain.PublisherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void authorTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        book.setAuthor(authorBack);
        assertThat(book.getAuthor()).isEqualTo(authorBack);

        book.author(null);
        assertThat(book.getAuthor()).isNull();
    }

    @Test
    void publisherTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Publisher publisherBack = getPublisherRandomSampleGenerator();

        book.setPublisher(publisherBack);
        assertThat(book.getPublisher()).isEqualTo(publisherBack);

        book.publisher(null);
        assertThat(book.getPublisher()).isNull();
    }
}
