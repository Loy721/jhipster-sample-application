package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserTest;
import com.mycompany.myapp.repository.UserTestRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserTest}.
 */
@RestController
@RequestMapping("/api/user-tests")
@Transactional
public class UserTestResource {

    private final Logger log = LoggerFactory.getLogger(UserTestResource.class);

    private static final String ENTITY_NAME = "userTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTestRepository userTestRepository;

    public UserTestResource(UserTestRepository userTestRepository) {
        this.userTestRepository = userTestRepository;
    }

    /**
     * {@code POST  /user-tests} : Create a new userTest.
     *
     * @param userTest the userTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userTest, or with status {@code 400 (Bad Request)} if the userTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserTest> createUserTest(@RequestBody UserTest userTest) throws URISyntaxException {
        log.debug("REST request to save UserTest : {}", userTest);
        if (userTest.getId() != null) {
            throw new BadRequestAlertException("A new userTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserTest result = userTestRepository.save(userTest);
        return ResponseEntity
            .created(new URI("/api/user-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-tests/:id} : Updates an existing userTest.
     *
     * @param id the id of the userTest to save.
     * @param userTest the userTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTest,
     * or with status {@code 400 (Bad Request)} if the userTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserTest> updateUserTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTest userTest
    ) throws URISyntaxException {
        log.debug("REST request to update UserTest : {}, {}", id, userTest);
        if (userTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserTest result = userTestRepository.save(userTest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-tests/:id} : Partial updates given fields of an existing userTest, field will ignore if it is null
     *
     * @param id the id of the userTest to save.
     * @param userTest the userTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTest,
     * or with status {@code 400 (Bad Request)} if the userTest is not valid,
     * or with status {@code 404 (Not Found)} if the userTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the userTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserTest> partialUpdateUserTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTest userTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserTest partially : {}, {}", id, userTest);
        if (userTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserTest> result = userTestRepository
            .findById(userTest.getId())
            .map(existingUserTest -> {
                if (userTest.getGrade() != null) {
                    existingUserTest.setGrade(userTest.getGrade());
                }

                return existingUserTest;
            })
            .map(userTestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTest.getId().toString())
        );
    }

    /**
     * {@code GET  /user-tests} : get all the userTests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTests in body.
     */
    @GetMapping("")
    public List<UserTest> getAllUserTests() {
        log.debug("REST request to get all UserTests");
        return userTestRepository.findAll();
    }

    /**
     * {@code GET  /user-tests/:id} : get the "id" userTest.
     *
     * @param id the id of the userTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserTest> getUserTest(@PathVariable("id") Long id) {
        log.debug("REST request to get UserTest : {}", id);
        Optional<UserTest> userTest = userTestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userTest);
    }

    /**
     * {@code DELETE  /user-tests/:id} : delete the "id" userTest.
     *
     * @param id the id of the userTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserTest(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserTest : {}", id);
        userTestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
