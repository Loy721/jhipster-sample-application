package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BookTestSamples.*;
import static com.mycompany.myapp.domain.PublisherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PublisherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Publisher.class);
        Publisher publisher1 = getPublisherSample1();
        Publisher publisher2 = new Publisher();
        assertThat(publisher1).isNotEqualTo(publisher2);

        publisher2.setId(publisher1.getId());
        assertThat(publisher1).isEqualTo(publisher2);

        publisher2 = getPublisherSample2();
        assertThat(publisher1).isNotEqualTo(publisher2);
    }

    @Test
    void publishedBooksTest() throws Exception {
        Publisher publisher = getPublisherRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        publisher.addPublishedBooks(bookBack);
        assertThat(publisher.getPublishedBooks()).containsOnly(bookBack);
        assertThat(bookBack.getPublisher()).isEqualTo(publisher);

        publisher.removePublishedBooks(bookBack);
        assertThat(publisher.getPublishedBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getPublisher()).isNull();

        publisher.publishedBooks(new HashSet<>(Set.of(bookBack)));
        assertThat(publisher.getPublishedBooks()).containsOnly(bookBack);
        assertThat(bookBack.getPublisher()).isEqualTo(publisher);

        publisher.setPublishedBooks(new HashSet<>());
        assertThat(publisher.getPublishedBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getPublisher()).isNull();
    }
}
