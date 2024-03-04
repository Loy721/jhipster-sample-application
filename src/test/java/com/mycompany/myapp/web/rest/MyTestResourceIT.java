package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyTest;
import com.mycompany.myapp.repository.MyTestRepository;
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
 * Integration tests for the {@link MyTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyTestResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MyTestRepository myTestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyTestMockMvc;

    private MyTest myTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyTest createEntity(EntityManager em) {
        MyTest myTest = new MyTest().title(DEFAULT_TITLE);
        return myTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyTest createUpdatedEntity(EntityManager em) {
        MyTest myTest = new MyTest().title(UPDATED_TITLE);
        return myTest;
    }

    @BeforeEach
    public void initTest() {
        myTest = createEntity(em);
    }

    @Test
    @Transactional
    void createMyTest() throws Exception {
        int databaseSizeBeforeCreate = myTestRepository.findAll().size();
        // Create the MyTest
        restMyTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myTest)))
            .andExpect(status().isCreated());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeCreate + 1);
        MyTest testMyTest = myTestList.get(myTestList.size() - 1);
        assertThat(testMyTest.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createMyTestWithExistingId() throws Exception {
        // Create the MyTest with an existing ID
        myTest.setId(1L);

        int databaseSizeBeforeCreate = myTestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myTest)))
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyTests() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        // Get all the myTestList
        restMyTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        // Get the myTest
        restMyTestMockMvc
            .perform(get(ENTITY_API_URL_ID, myTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myTest.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingMyTest() throws Exception {
        // Get the myTest
        restMyTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();

        // Update the myTest
        MyTest updatedMyTest = myTestRepository.findById(myTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyTest are not directly saved in db
        em.detach(updatedMyTest);
        updatedMyTest.title(UPDATED_TITLE);

        restMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
        MyTest testMyTest = myTestList.get(myTestList.size() - 1);
        assertThat(testMyTest.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();
        myTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyTestWithPatch() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();

        // Update the myTest using partial update
        MyTest partialUpdatedMyTest = new MyTest();
        partialUpdatedMyTest.setId(myTest.getId());

        partialUpdatedMyTest.title(UPDATED_TITLE);

        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
        MyTest testMyTest = myTestList.get(myTestList.size() - 1);
        assertThat(testMyTest.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateMyTestWithPatch() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();

        // Update the myTest using partial update
        MyTest partialUpdatedMyTest = new MyTest();
        partialUpdatedMyTest.setId(myTest.getId());

        partialUpdatedMyTest.title(UPDATED_TITLE);

        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
        MyTest testMyTest = myTestList.get(myTestList.size() - 1);
        assertThat(testMyTest.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();
        myTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyTest() throws Exception {
        int databaseSizeBeforeUpdate = myTestRepository.findAll().size();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(myTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyTest in the database
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        int databaseSizeBeforeDelete = myTestRepository.findAll().size();

        // Delete the myTest
        restMyTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, myTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyTest> myTestList = myTestRepository.findAll();
        assertThat(myTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
