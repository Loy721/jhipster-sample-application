package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Publisher.
 */
@Entity
@Table(name = "publisher")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Publisher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @JsonIgnoreProperties(value = { "author", "publisher" }, allowSetters = true)
    private Set<Book> publishedBooks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Publisher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Publisher name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getPublishedBooks() {
        return this.publishedBooks;
    }

    public void setPublishedBooks(Set<Book> books) {
        if (this.publishedBooks != null) {
            this.publishedBooks.forEach(i -> i.setPublisher(null));
        }
        if (books != null) {
            books.forEach(i -> i.setPublisher(this));
        }
        this.publishedBooks = books;
    }

    public Publisher publishedBooks(Set<Book> books) {
        this.setPublishedBooks(books);
        return this;
    }

    public Publisher addPublishedBooks(Book book) {
        this.publishedBooks.add(book);
        book.setPublisher(this);
        return this;
    }

    public Publisher removePublishedBooks(Book book) {
        this.publishedBooks.remove(book);
        book.setPublisher(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Publisher)) {
            return false;
        }
        return getId() != null && getId().equals(((Publisher) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Publisher{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
