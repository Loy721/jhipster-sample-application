package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyFileTestSamples.*;
import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MyFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyFile.class);
        MyFile myFile1 = getMyFileSample1();
        MyFile myFile2 = new MyFile();
        assertThat(myFile1).isNotEqualTo(myFile2);

        myFile2.setId(myFile1.getId());
        assertThat(myFile1).isEqualTo(myFile2);

        myFile2 = getMyFileSample2();
        assertThat(myFile1).isNotEqualTo(myFile2);
    }

    @Test
    void booksTest() throws Exception {
        MyFile myFile = getMyFileRandomSampleGenerator();
        MyTest myTestBack = getMyTestRandomSampleGenerator();

        myFile.addBooks(myTestBack);
        assertThat(myFile.getBooks()).containsOnly(myTestBack);
        assertThat(myTestBack.getAuthor()).isEqualTo(myFile);

        myFile.removeBooks(myTestBack);
        assertThat(myFile.getBooks()).doesNotContain(myTestBack);
        assertThat(myTestBack.getAuthor()).isNull();

        myFile.books(new HashSet<>(Set.of(myTestBack)));
        assertThat(myFile.getBooks()).containsOnly(myTestBack);
        assertThat(myTestBack.getAuthor()).isEqualTo(myFile);

        myFile.setBooks(new HashSet<>());
        assertThat(myFile.getBooks()).doesNotContain(myTestBack);
        assertThat(myTestBack.getAuthor()).isNull();
    }
}
