package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FilesTestSamples.*;
import static com.mycompany.myapp.domain.TestsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void testsTest() throws Exception {
        Files files = getFilesRandomSampleGenerator();
        Tests testsBack = getTestsRandomSampleGenerator();

        files.addTests(testsBack);
        assertThat(files.getTests()).containsOnly(testsBack);
        assertThat(testsBack.getFiles()).isEqualTo(files);

        files.removeTests(testsBack);
        assertThat(files.getTests()).doesNotContain(testsBack);
        assertThat(testsBack.getFiles()).isNull();

        files.tests(new HashSet<>(Set.of(testsBack)));
        assertThat(files.getTests()).containsOnly(testsBack);
        assertThat(testsBack.getFiles()).isEqualTo(files);

        files.setTests(new HashSet<>());
        assertThat(files.getTests()).doesNotContain(testsBack);
        assertThat(testsBack.getFiles()).isNull();
    }
}
