package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyUsers;
import com.mycompany.myapp.repository.MyUsersRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MyUsers}.
 */
@RestController
@RequestMapping("/api/my-users")
@Transactional
public class MyUsersResource {

    private final Logger log = LoggerFactory.getLogger(MyUsersResource.class);

    private static final String ENTITY_NAME = "myUsers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyUsersRepository myUsersRepository;

    public MyUsersResource(MyUsersRepository myUsersRepository) {
        this.myUsersRepository = myUsersRepository;
    }

    /**
     * {@code POST  /my-users} : Create a new myUsers.
     *
     * @param myUsers the myUsers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myUsers, or with status {@code 400 (Bad Request)} if the myUsers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyUsers> createMyUsers(@RequestBody MyUsers myUsers) throws URISyntaxException {
        log.debug("REST request to save MyUsers : {}", myUsers);
        if (myUsers.getId() != null) {
            throw new BadRequestAlertException("A new myUsers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyUsers result = myUsersRepository.save(myUsers);
        return ResponseEntity
            .created(new URI("/api/my-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-users/:id} : Updates an existing myUsers.
     *
     * @param id the id of the myUsers to save.
     * @param myUsers the myUsers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUsers,
     * or with status {@code 400 (Bad Request)} if the myUsers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myUsers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyUsers> updateMyUsers(@PathVariable(value = "id", required = false) final Long id, @RequestBody MyUsers myUsers)
        throws URISyntaxException {
        log.debug("REST request to update MyUsers : {}, {}", id, myUsers);
        if (myUsers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUsers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MyUsers result = myUsersRepository.save(myUsers);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myUsers.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /my-users/:id} : Partial updates given fields of an existing myUsers, field will ignore if it is null
     *
     * @param id the id of the myUsers to save.
     * @param myUsers the myUsers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUsers,
     * or with status {@code 400 (Bad Request)} if the myUsers is not valid,
     * or with status {@code 404 (Not Found)} if the myUsers is not found,
     * or with status {@code 500 (Internal Server Error)} if the myUsers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyUsers> partialUpdateMyUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyUsers myUsers
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyUsers partially : {}, {}", id, myUsers);
        if (myUsers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUsers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyUsers> result = myUsersRepository
            .findById(myUsers.getId())
            .map(existingMyUsers -> {
                if (myUsers.getName() != null) {
                    existingMyUsers.setName(myUsers.getName());
                }
                if (myUsers.getSurname() != null) {
                    existingMyUsers.setSurname(myUsers.getSurname());
                }

                return existingMyUsers;
            })
            .map(myUsersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myUsers.getId().toString())
        );
    }

    /**
     * {@code GET  /my-users} : get all the myUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myUsers in body.
     */
    @GetMapping("")
    public List<MyUsers> getAllMyUsers() {
        log.debug("REST request to get all MyUsers");
        return myUsersRepository.findAll();
    }

    /**
     * {@code GET  /my-users/:id} : get the "id" myUsers.
     *
     * @param id the id of the myUsers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myUsers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyUsers> getMyUsers(@PathVariable("id") Long id) {
        log.debug("REST request to get MyUsers : {}", id);
        Optional<MyUsers> myUsers = myUsersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myUsers);
    }

    /**
     * {@code DELETE  /my-users/:id} : delete the "id" myUsers.
     *
     * @param id the id of the myUsers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyUsers(@PathVariable("id") Long id) {
        log.debug("REST request to delete MyUsers : {}", id);
        myUsersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
