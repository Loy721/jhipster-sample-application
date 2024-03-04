package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyQuestion;
import com.mycompany.myapp.repository.MyQuestionRepository;
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
 * Integration tests for the {@link MyQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyQuestionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MyQuestionRepository myQuestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyQuestionMockMvc;

    private MyQuestion myQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyQuestion createEntity(EntityManager em) {
        MyQuestion myQuestion = new MyQuestion().name(DEFAULT_NAME);
        return myQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyQuestion createUpdatedEntity(EntityManager em) {
        MyQuestion myQuestion = new MyQuestion().name(UPDATED_NAME);
        return myQuestion;
    }

    @BeforeEach
    public void initTest() {
        myQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createMyQuestion() throws Exception {
        int databaseSizeBeforeCreate = myQuestionRepository.findAll().size();
        // Create the MyQuestion
        restMyQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myQuestion)))
            .andExpect(status().isCreated());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        MyQuestion testMyQuestion = myQuestionList.get(myQuestionList.size() - 1);
        assertThat(testMyQuestion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createMyQuestionWithExistingId() throws Exception {
        // Create the MyQuestion with an existing ID
        myQuestion.setId(1L);

        int databaseSizeBeforeCreate = myQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyQuestions() throws Exception {
        // Initialize the database
        myQuestionRepository.saveAndFlush(myQuestion);

        // Get all the myQuestionList
        restMyQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMyQuestion() throws Exception {
        // Initialize the database
        myQuestionRepository.saveAndFlush(myQuestion);

        // Get the myQuestion
        restMyQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, myQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myQuestion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMyQuestion() throws Exception {
        // Get the myQuestion
        restMyQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyQuestion() throws Exception {
        // Initialize the database
        myQuestionRepository.saveAndFlush(myQuestion);

        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();

        // Update the myQuestion
        MyQuestion updatedMyQuestion = myQuestionRepository.findById(myQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyQuestion are not directly saved in db
        em.detach(updatedMyQuestion);
        updatedMyQuestion.name(UPDATED_NAME);

        restMyQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMyQuestion))
            )
            .andExpect(status().isOk());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
        MyQuestion testMyQuestion = myQuestionList.get(myQuestionList.size() - 1);
        assertThat(testMyQuestion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingMyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();
        myQuestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();
        myQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();
        myQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myQuestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyQuestionWithPatch() throws Exception {
        // Initialize the database
        myQuestionRepository.saveAndFlush(myQuestion);

        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();

        // Update the myQuestion using partial update
        MyQuestion partialUpdatedMyQuestion = new MyQuestion();
        partialUpdatedMyQuestion.setId(myQuestion.getId());

        partialUpdatedMyQuestion.name(UPDATED_NAME);

        restMyQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyQuestion))
            )
            .andExpect(status().isOk());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
        MyQuestion testMyQuestion = myQuestionList.get(myQuestionList.size() - 1);
        assertThat(testMyQuestion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMyQuestionWithPatch() throws Exception {
        // Initialize the database
        myQuestionRepository.saveAndFlush(myQuestion);

        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();

        // Update the myQuestion using partial update
        MyQuestion partialUpdatedMyQuestion = new MyQuestion();
        partialUpdatedMyQuestion.setId(myQuestion.getId());

        partialUpdatedMyQuestion.name(UPDATED_NAME);

        restMyQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyQuestion))
            )
            .andExpect(status().isOk());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
        MyQuestion testMyQuestion = myQuestionList.get(myQuestionList.size() - 1);
        assertThat(testMyQuestion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();
        myQuestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();
        myQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = myQuestionRepository.findAll().size();
        myQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(myQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyQuestion in the database
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyQuestion() throws Exception {
        // Initialize the database
        myQuestionRepository.saveAndFlush(myQuestion);

        int databaseSizeBeforeDelete = myQuestionRepository.findAll().size();

        // Delete the myQuestion
        restMyQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, myQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyQuestion> myQuestionList = myQuestionRepository.findAll();
        assertThat(myQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
