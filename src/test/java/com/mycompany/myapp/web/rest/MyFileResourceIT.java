package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyFile;
import com.mycompany.myapp.repository.MyFileRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MyFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyFileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MyFileRepository myFileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyFileMockMvc;

    private MyFile myFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyFile createEntity(EntityManager em) {
        MyFile myFile = new MyFile().name(DEFAULT_NAME);
        return myFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyFile createUpdatedEntity(EntityManager em) {
        MyFile myFile = new MyFile().name(UPDATED_NAME);
        return myFile;
    }

    @BeforeEach
    public void initTest() {
        myFile = createEntity(em);
    }

    @Test
    @Transactional
    void createMyFile() throws Exception {
        int databaseSizeBeforeCreate = myFileRepository.findAll().size();
        // Create the MyFile
        restMyFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myFile)))
            .andExpect(status().isCreated());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeCreate + 1);
        MyFile testMyFile = myFileList.get(myFileList.size() - 1);
        assertThat(testMyFile.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createMyFileWithExistingId() throws Exception {
        // Create the MyFile with an existing ID
        myFile.setId(1L);

        int databaseSizeBeforeCreate = myFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myFile)))
            .andExpect(status().isBadRequest());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyFiles() throws Exception {
        // Initialize the database
        myFileRepository.saveAndFlush(myFile);

        // Get all the myFileList
        restMyFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMyFile() throws Exception {
        // Initialize the database
        myFileRepository.saveAndFlush(myFile);

        // Get the myFile
        restMyFileMockMvc
            .perform(get(ENTITY_API_URL_ID, myFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMyFile() throws Exception {
        // Get the myFile
        restMyFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyFile() throws Exception {
        // Initialize the database
        myFileRepository.saveAndFlush(myFile);

        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();

        // Update the myFile
        MyFile updatedMyFile = myFileRepository.findById(myFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyFile are not directly saved in db
        em.detach(updatedMyFile);
        updatedMyFile.name(UPDATED_NAME);

        restMyFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMyFile))
            )
            .andExpect(status().isOk());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
        MyFile testMyFile = myFileList.get(myFileList.size() - 1);
        assertThat(testMyFile.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingMyFile() throws Exception {
        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();
        myFile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyFile() throws Exception {
        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();
        myFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyFile() throws Exception {
        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();
        myFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myFile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyFileWithPatch() throws Exception {
        // Initialize the database
        myFileRepository.saveAndFlush(myFile);

        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();

        // Update the myFile using partial update
        MyFile partialUpdatedMyFile = new MyFile();
        partialUpdatedMyFile.setId(myFile.getId());

        partialUpdatedMyFile.name(UPDATED_NAME);

        restMyFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyFile))
            )
            .andExpect(status().isOk());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
        MyFile testMyFile = myFileList.get(myFileList.size() - 1);
        assertThat(testMyFile.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMyFileWithPatch() throws Exception {
        // Initialize the database
        myFileRepository.saveAndFlush(myFile);

        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();

        // Update the myFile using partial update
        MyFile partialUpdatedMyFile = new MyFile();
        partialUpdatedMyFile.setId(myFile.getId());

        partialUpdatedMyFile.name(UPDATED_NAME);

        restMyFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyFile))
            )
            .andExpect(status().isOk());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
        MyFile testMyFile = myFileList.get(myFileList.size() - 1);
        assertThat(testMyFile.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMyFile() throws Exception {
        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();
        myFile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyFile() throws Exception {
        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();
        myFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myFile))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyFile() throws Exception {
        int databaseSizeBeforeUpdate = myFileRepository.findAll().size();
        myFile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(myFile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyFile in the database
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyFile() throws Exception {
        // Initialize the database
        myFileRepository.saveAndFlush(myFile);

        int databaseSizeBeforeDelete = myFileRepository.findAll().size();

        // Delete the myFile
        restMyFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, myFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyFile> myFileList = myFileRepository.findAll();
        assertThat(myFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
