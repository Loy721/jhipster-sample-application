package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyQuestion;
import com.mycompany.myapp.repository.MyQuestionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MyQuestion}.
 */
@RestController
@RequestMapping("/api/my-questions")
@Transactional
public class MyQuestionResource {

    private final Logger log = LoggerFactory.getLogger(MyQuestionResource.class);

    private static final String ENTITY_NAME = "myQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyQuestionRepository myQuestionRepository;

    public MyQuestionResource(MyQuestionRepository myQuestionRepository) {
        this.myQuestionRepository = myQuestionRepository;
    }

    /**
     * {@code POST  /my-questions} : Create a new myQuestion.
     *
     * @param myQuestion the myQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myQuestion, or with status {@code 400 (Bad Request)} if the myQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyQuestion> createMyQuestion(@RequestBody MyQuestion myQuestion) throws URISyntaxException {
        log.debug("REST request to save MyQuestion : {}", myQuestion);
        if (myQuestion.getId() != null) {
            throw new BadRequestAlertException("A new myQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyQuestion result = myQuestionRepository.save(myQuestion);
        return ResponseEntity
            .created(new URI("/api/my-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-questions/:id} : Updates an existing myQuestion.
     *
     * @param id the id of the myQuestion to save.
     * @param myQuestion the myQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myQuestion,
     * or with status {@code 400 (Bad Request)} if the myQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyQuestion> updateMyQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyQuestion myQuestion
    ) throws URISyntaxException {
        log.debug("REST request to update MyQuestion : {}, {}", id, myQuestion);
        if (myQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MyQuestion result = myQuestionRepository.save(myQuestion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myQuestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /my-questions/:id} : Partial updates given fields of an existing myQuestion, field will ignore if it is null
     *
     * @param id the id of the myQuestion to save.
     * @param myQuestion the myQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myQuestion,
     * or with status {@code 400 (Bad Request)} if the myQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the myQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the myQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyQuestion> partialUpdateMyQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyQuestion myQuestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyQuestion partially : {}, {}", id, myQuestion);
        if (myQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyQuestion> result = myQuestionRepository
            .findById(myQuestion.getId())
            .map(existingMyQuestion -> {
                if (myQuestion.getName() != null) {
                    existingMyQuestion.setName(myQuestion.getName());
                }

                return existingMyQuestion;
            })
            .map(myQuestionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /my-questions} : get all the myQuestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myQuestions in body.
     */
    @GetMapping("")
    public List<MyQuestion> getAllMyQuestions() {
        log.debug("REST request to get all MyQuestions");
        return myQuestionRepository.findAll();
    }

    /**
     * {@code GET  /my-questions/:id} : get the "id" myQuestion.
     *
     * @param id the id of the myQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyQuestion> getMyQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to get MyQuestion : {}", id);
        Optional<MyQuestion> myQuestion = myQuestionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myQuestion);
    }

    /**
     * {@code DELETE  /my-questions/:id} : delete the "id" myQuestion.
     *
     * @param id the id of the myQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete MyQuestion : {}", id);
        myQuestionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
