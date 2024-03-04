package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyFile;
import com.mycompany.myapp.repository.MyFileRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MyFile}.
 */
@RestController
@RequestMapping("/api/my-files")
@Transactional
public class MyFileResource {

    private final Logger log = LoggerFactory.getLogger(MyFileResource.class);

    private static final String ENTITY_NAME = "myFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyFileRepository myFileRepository;

    public MyFileResource(MyFileRepository myFileRepository) {
        this.myFileRepository = myFileRepository;
    }

    /**
     * {@code POST  /my-files} : Create a new myFile.
     *
     * @param myFile the myFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myFile, or with status {@code 400 (Bad Request)} if the myFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyFile> createMyFile(@RequestBody MyFile myFile) throws URISyntaxException {
        log.debug("REST request to save MyFile : {}", myFile);
        if (myFile.getId() != null) {
            throw new BadRequestAlertException("A new myFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MyFile result = myFileRepository.save(myFile);
        return ResponseEntity
            .created(new URI("/api/my-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-files/:id} : Updates an existing myFile.
     *
     * @param id the id of the myFile to save.
     * @param myFile the myFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myFile,
     * or with status {@code 400 (Bad Request)} if the myFile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyFile> updateMyFile(@PathVariable(value = "id", required = false) final Long id, @RequestBody MyFile myFile)
        throws URISyntaxException {
        log.debug("REST request to update MyFile : {}, {}", id, myFile);
        if (myFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MyFile result = myFileRepository.save(myFile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myFile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /my-files/:id} : Partial updates given fields of an existing myFile, field will ignore if it is null
     *
     * @param id the id of the myFile to save.
     * @param myFile the myFile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myFile,
     * or with status {@code 400 (Bad Request)} if the myFile is not valid,
     * or with status {@code 404 (Not Found)} if the myFile is not found,
     * or with status {@code 500 (Internal Server Error)} if the myFile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyFile> partialUpdateMyFile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyFile myFile
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyFile partially : {}, {}", id, myFile);
        if (myFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myFile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyFile> result = myFileRepository
            .findById(myFile.getId())
            .map(existingMyFile -> {
                if (myFile.getName() != null) {
                    existingMyFile.setName(myFile.getName());
                }

                return existingMyFile;
            })
            .map(myFileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myFile.getId().toString())
        );
    }

    /**
     * {@code GET  /my-files} : get all the myFiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myFiles in body.
     */
    @GetMapping("")
    public List<MyFile> getAllMyFiles() {
        log.debug("REST request to get all MyFiles");
        return myFileRepository.findAll();
    }

    /**
     * {@code GET  /my-files/:id} : get the "id" myFile.
     *
     * @param id the id of the myFile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myFile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyFile> getMyFile(@PathVariable("id") Long id) {
        log.debug("REST request to get MyFile : {}", id);
        Optional<MyFile> myFile = myFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myFile);
    }

    /**
     * {@code DELETE  /my-files/:id} : delete the "id" myFile.
     *
     * @param id the id of the myFile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete MyFile : {}", id);
        myFileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
