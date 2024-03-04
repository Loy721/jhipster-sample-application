package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FilesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Files.class);
        Files files1 = getFilesSample1();
        Files files2 = new Files();
        assertThat(files1).isNotEqualTo(files2);

        files2.setId(files1.getId());
        assertThat(files1).isEqualTo(files2);

        files2 = getFilesSample2();
        assertThat(files1).isNotEqualTo(files2);
    }
}
