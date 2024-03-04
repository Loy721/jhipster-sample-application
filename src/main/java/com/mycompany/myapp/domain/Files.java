package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Files.
 */
@Entity
@Table(name = "files")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "content")
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "files")
    @JsonIgnoreProperties(value = { "files" }, allowSetters = true)
    private Set<Tests> tests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Files id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return this.topic;
    }

    public Files topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return this.content;
    }

    public Files content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Tests> getTests() {
        return this.tests;
    }

    public void setTests(Set<Tests> tests) {
        if (this.tests != null) {
            this.tests.forEach(i -> i.setFiles(null));
        }
        if (tests != null) {
            tests.forEach(i -> i.setFiles(this));
        }
        this.tests = tests;
    }

    public Files tests(Set<Tests> tests) {
        this.setTests(tests);
        return this;
    }

    public Files addTests(Tests tests) {
        this.tests.add(tests);
        tests.setFiles(this);
        return this;
    }

    public Files removeTests(Tests tests) {
        this.tests.remove(tests);
        tests.setFiles(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Files)) {
            return false;
        }
        return getId() != null && getId().equals(((Files) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Files{" +
            "id=" + getId() +
            ", topic='" + getTopic() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
