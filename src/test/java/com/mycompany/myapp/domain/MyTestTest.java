package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FileTestSamples.*;
import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static com.mycompany.myapp.domain.QuestionTestSamples.*;
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
    void myQuestionsTest() throws Exception {
        MyTest myTest = getMyTestRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        myTest.addMyQuestions(questionBack);
        assertThat(myTest.getMyQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getMyTest()).isEqualTo(myTest);

        myTest.removeMyQuestions(questionBack);
        assertThat(myTest.getMyQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getMyTest()).isNull();

        myTest.myQuestions(new HashSet<>(Set.of(questionBack)));
        assertThat(myTest.getMyQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getMyTest()).isEqualTo(myTest);

        myTest.setMyQuestions(new HashSet<>());
        assertThat(myTest.getMyQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getMyTest()).isNull();
    }

    @Test
    void fileTest() throws Exception {
        MyTest myTest = getMyTestRandomSampleGenerator();
        File fileBack = getFileRandomSampleGenerator();

        myTest.setFile(fileBack);
        assertThat(myTest.getFile()).isEqualTo(fileBack);

        myTest.file(null);
        assertThat(myTest.getFile()).isNull();
    }
}
