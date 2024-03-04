package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.UserTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTest.class);
        UserTest userTest1 = getUserTestSample1();
        UserTest userTest2 = new UserTest();
        assertThat(userTest1).isNotEqualTo(userTest2);

        userTest2.setId(userTest1.getId());
        assertThat(userTest1).isEqualTo(userTest2);

        userTest2 = getUserTestSample2();
        assertThat(userTest1).isNotEqualTo(userTest2);
    }
}
