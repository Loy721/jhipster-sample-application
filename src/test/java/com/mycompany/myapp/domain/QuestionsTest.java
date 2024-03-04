package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuestionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questions.class);
        Questions questions1 = getQuestionsSample1();
        Questions questions2 = new Questions();
        assertThat(questions1).isNotEqualTo(questions2);

        questions2.setId(questions1.getId());
        assertThat(questions1).isEqualTo(questions2);

        questions2 = getQuestionsSample2();
        assertThat(questions1).isNotEqualTo(questions2);
    }
}
