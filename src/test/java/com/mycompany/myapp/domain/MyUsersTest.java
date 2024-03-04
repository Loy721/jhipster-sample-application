package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyUsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyUsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyUsers.class);
        MyUsers myUsers1 = getMyUsersSample1();
        MyUsers myUsers2 = new MyUsers();
        assertThat(myUsers1).isNotEqualTo(myUsers2);

        myUsers2.setId(myUsers1.getId());
        assertThat(myUsers1).isEqualTo(myUsers2);

        myUsers2 = getMyUsersSample2();
        assertThat(myUsers1).isNotEqualTo(myUsers2);
    }
}
