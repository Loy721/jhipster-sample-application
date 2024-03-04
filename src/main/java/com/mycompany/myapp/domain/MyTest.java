package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MyTest.
 */
@Entity
@Table(name = "my_test")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @JsonIgnoreProperties(value = { "publisher" }, allowSetters = true)
    private Set<MyQuestion> publishedBooks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private File author;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MyTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public MyTest title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<MyQuestion> getPublishedBooks() {
        return this.publishedBooks;
    }

    public void setPublishedBooks(Set<MyQuestion> myQuestions) {
        if (this.publishedBooks != null) {
            this.publishedBooks.forEach(i -> i.setPublisher(null));
        }
        if (myQuestions != null) {
            myQuestions.forEach(i -> i.setPublisher(this));
        }
        this.publishedBooks = myQuestions;
    }

    public MyTest publishedBooks(Set<MyQuestion> myQuestions) {
        this.setPublishedBooks(myQuestions);
        return this;
    }

    public MyTest addPublishedBooks(MyQuestion myQuestion) {
        this.publishedBooks.add(myQuestion);
        myQuestion.setPublisher(this);
        return this;
    }

    public MyTest removePublishedBooks(MyQuestion myQuestion) {
        this.publishedBooks.remove(myQuestion);
        myQuestion.setPublisher(null);
        return this;
    }

    public File getAuthor() {
        return this.author;
    }

    public void setAuthor(File file) {
        this.author = file;
    }

    public MyTest author(File file) {
        this.setAuthor(file);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyTest)) {
            return false;
        }
        return getId() != null && getId().equals(((MyTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyTest{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
