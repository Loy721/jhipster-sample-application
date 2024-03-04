package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FileTestSamples.*;
import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(File.class);
        File file1 = getFileSample1();
        File file2 = new File();
        assertThat(file1).isNotEqualTo(file2);

        file2.setId(file1.getId());
        assertThat(file1).isEqualTo(file2);

        file2 = getFileSample2();
        assertThat(file1).isNotEqualTo(file2);
    }

    @Test
    void booksTest() throws Exception {
        File file = getFileRandomSampleGenerator();
        MyTest myTestBack = getMyTestRandomSampleGenerator();

        file.addBooks(myTestBack);
        assertThat(file.getBooks()).containsOnly(myTestBack);
        assertThat(myTestBack.getAuthor()).isEqualTo(file);

        file.removeBooks(myTestBack);
        assertThat(file.getBooks()).doesNotContain(myTestBack);
        assertThat(myTestBack.getAuthor()).isNull();

        file.books(new HashSet<>(Set.of(myTestBack)));
        assertThat(file.getBooks()).containsOnly(myTestBack);
        assertThat(myTestBack.getAuthor()).isEqualTo(file);

        file.setBooks(new HashSet<>());
        assertThat(file.getBooks()).doesNotContain(myTestBack);
        assertThat(myTestBack.getAuthor()).isNull();
    }
}
