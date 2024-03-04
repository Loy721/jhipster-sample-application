package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyQuestionTestSamples.*;
import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyQuestion.class);
        MyQuestion myQuestion1 = getMyQuestionSample1();
        MyQuestion myQuestion2 = new MyQuestion();
        assertThat(myQuestion1).isNotEqualTo(myQuestion2);

        myQuestion2.setId(myQuestion1.getId());
        assertThat(myQuestion1).isEqualTo(myQuestion2);

        myQuestion2 = getMyQuestionSample2();
        assertThat(myQuestion1).isNotEqualTo(myQuestion2);
    }

    @Test
    void publisherTest() throws Exception {
        MyQuestion myQuestion = getMyQuestionRandomSampleGenerator();
        MyTest myTestBack = getMyTestRandomSampleGenerator();

        myQuestion.setPublisher(myTestBack);
        assertThat(myQuestion.getPublisher()).isEqualTo(myTestBack);

        myQuestion.publisher(null);
        assertThat(myQuestion.getPublisher()).isNull();
    }
}
