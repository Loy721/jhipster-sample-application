package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MyFile.
 */
@Entity
@Table(name = "my_file")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @JsonIgnoreProperties(value = { "publishedBooks", "author" }, allowSetters = true)
    private Set<MyTest> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MyFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MyFile name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MyTest> getBooks() {
        return this.books;
    }

    public void setBooks(Set<MyTest> myTests) {
        if (this.books != null) {
            this.books.forEach(i -> i.setAuthor(null));
        }
        if (myTests != null) {
            myTests.forEach(i -> i.setAuthor(this));
        }
        this.books = myTests;
    }

    public MyFile books(Set<MyTest> myTests) {
        this.setBooks(myTests);
        return this;
    }

    public MyFile addBooks(MyTest myTest) {
        this.books.add(myTest);
        myTest.setAuthor(this);
        return this;
    }

    public MyFile removeBooks(MyTest myTest) {
        this.books.remove(myTest);
        myTest.setAuthor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyFile)) {
            return false;
        }
        return getId() != null && getId().equals(((MyFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyFile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
