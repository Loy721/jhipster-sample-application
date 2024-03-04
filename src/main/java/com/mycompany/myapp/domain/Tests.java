package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Tests.
 */
@Entity
@Table(name = "tests")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tests implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "questions")
    private String questions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Files files;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tests id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tests name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestions() {
        return this.questions;
    }

    public Tests questions(String questions) {
        this.setQuestions(questions);
        return this;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public Files getFiles() {
        return this.files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public Tests files(Files files) {
        this.setFiles(files);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tests)) {
            return false;
        }
        return getId() != null && getId().equals(((Tests) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tests{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", questions='" + getQuestions() + "'" +
            "}";
    }
}
