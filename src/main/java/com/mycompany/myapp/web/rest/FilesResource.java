package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Files;
import com.mycompany.myapp.repository.FilesRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Files}.
 */
@RestController
@RequestMapping("/api/files")
@Transactional
public class FilesResource {

    private final Logger log = LoggerFactory.getLogger(FilesResource.class);

    private static final String ENTITY_NAME = "files";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilesRepository filesRepository;

    public FilesResource(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    /**
     * {@code POST  /files} : Create a new files.
     *
     * @param files the files to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new files, or with status {@code 400 (Bad Request)} if the files has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Files> createFiles(@RequestBody Files files) throws URISyntaxException {
        log.debug("REST request to save Files : {}", files);
        if (files.getId() != null) {
            throw new BadRequestAlertException("A new files cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Files result = filesRepository.save(files);
        return ResponseEntity
            .created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /files/:id} : Updates an existing files.
     *
     * @param id the id of the files to save.
     * @param files the files to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated files,
     * or with status {@code 400 (Bad Request)} if the files is not valid,
     * or with status {@code 500 (Internal Server Error)} if the files couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Files> updateFiles(@PathVariable(value = "id", required = false) final Long id, @RequestBody Files files)
        throws URISyntaxException {
        log.debug("REST request to update Files : {}, {}", id, files);
        if (files.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, files.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Files result = filesRepository.save(files);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, files.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /files/:id} : Partial updates given fields of an existing files, field will ignore if it is null
     *
     * @param id the id of the files to save.
     * @param files the files to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated files,
     * or with status {@code 400 (Bad Request)} if the files is not valid,
     * or with status {@code 404 (Not Found)} if the files is not found,
     * or with status {@code 500 (Internal Server Error)} if the files couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Files> partialUpdateFiles(@PathVariable(value = "id", required = false) final Long id, @RequestBody Files files)
        throws URISyntaxException {
        log.debug("REST request to partial update Files partially : {}, {}", id, files);
        if (files.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, files.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Files> result = filesRepository
            .findById(files.getId())
            .map(existingFiles -> {
                if (files.getTopic() != null) {
                    existingFiles.setTopic(files.getTopic());
                }
                if (files.getContent() != null) {
                    existingFiles.setContent(files.getContent());
                }

                return existingFiles;
            })
            .map(filesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, files.getId().toString())
        );
    }

    /**
     * {@code GET  /files} : get all the files.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files in body.
     */
    @GetMapping("")
    public List<Files> getAllFiles() {
        log.debug("REST request to get all Files");
        return filesRepository.findAll();
    }

    /**
     * {@code GET  /files/:id} : get the "id" files.
     *
     * @param id the id of the files to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the files, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Files> getFiles(@PathVariable("id") Long id) {
        log.debug("REST request to get Files : {}", id);
        Optional<Files> files = filesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(files);
    }

    /**
     * {@code DELETE  /files/:id} : delete the "id" files.
     *
     * @param id the id of the files to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiles(@PathVariable("id") Long id) {
        log.debug("REST request to delete Files : {}", id);
        filesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
