package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyFileTestSamples.*;
import static com.mycompany.myapp.domain.MyQuestionTestSamples.*;
import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MyTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyTest.class);
        MyTest myTest1 = getMyTestSample1();
        MyTest myTest2 = new MyTest();
        assertThat(myTest1).isNotEqualTo(myTest2);

        myTest2.setId(myTest1.getId());
        assertThat(myTest1).isEqualTo(myTest2);

        myTest2 = getMyTestSample2();
        assertThat(myTest1).isNotEqualTo(myTest2);
    }

    @Test
    void publishedBooksTest() throws Exception {
        MyTest myTest = getMyTestRandomSampleGenerator();
        MyQuestion myQuestionBack = getMyQuestionRandomSampleGenerator();

        myTest.addPublishedBooks(myQuestionBack);
        assertThat(myTest.getPublishedBooks()).containsOnly(myQuestionBack);
        assertThat(myQuestionBack.getPublisher()).isEqualTo(myTest);

        myTest.removePublishedBooks(myQuestionBack);
        assertThat(myTest.getPublishedBooks()).doesNotContain(myQuestionBack);
        assertThat(myQuestionBack.getPublisher()).isNull();

        myTest.publishedBooks(new HashSet<>(Set.of(myQuestionBack)));
        assertThat(myTest.getPublishedBooks()).containsOnly(myQuestionBack);
        assertThat(myQuestionBack.getPublisher()).isEqualTo(myTest);

        myTest.setPublishedBooks(new HashSet<>());
        assertThat(myTest.getPublishedBooks()).doesNotContain(myQuestionBack);
        assertThat(myQuestionBack.getPublisher()).isNull();
    }

    @Test
    void authorTest() throws Exception {
        MyTest myTest = getMyTestRandomSampleGenerator();
        MyFile myFileBack = getMyFileRandomSampleGenerator();

        myTest.setAuthor(myFileBack);
        assertThat(myTest.getAuthor()).isEqualTo(myFileBack);

        myTest.author(null);
        assertThat(myTest.getAuthor()).isNull();
    }
}
