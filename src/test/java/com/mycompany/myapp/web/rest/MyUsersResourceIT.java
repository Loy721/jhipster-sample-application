package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyUsers;
import com.mycompany.myapp.repository.MyUsersRepository;
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
 * Integration tests for the {@link MyUsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyUsersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MyUsersRepository myUsersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyUsersMockMvc;

    private MyUsers myUsers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUsers createEntity(EntityManager em) {
        MyUsers myUsers = new MyUsers().name(DEFAULT_NAME).surname(DEFAULT_SURNAME);
        return myUsers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUsers createUpdatedEntity(EntityManager em) {
        MyUsers myUsers = new MyUsers().name(UPDATED_NAME).surname(UPDATED_SURNAME);
        return myUsers;
    }

    @BeforeEach
    public void initTest() {
        myUsers = createEntity(em);
    }

    @Test
    @Transactional
    void createMyUsers() throws Exception {
        int databaseSizeBeforeCreate = myUsersRepository.findAll().size();
        // Create the MyUsers
        restMyUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myUsers)))
            .andExpect(status().isCreated());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeCreate + 1);
        MyUsers testMyUsers = myUsersList.get(myUsersList.size() - 1);
        assertThat(testMyUsers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyUsers.getSurname()).isEqualTo(DEFAULT_SURNAME);
    }

    @Test
    @Transactional
    void createMyUsersWithExistingId() throws Exception {
        // Create the MyUsers with an existing ID
        myUsers.setId(1L);

        int databaseSizeBeforeCreate = myUsersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myUsers)))
            .andExpect(status().isBadRequest());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyUsers() throws Exception {
        // Initialize the database
        myUsersRepository.saveAndFlush(myUsers);

        // Get all the myUsersList
        restMyUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)));
    }

    @Test
    @Transactional
    void getMyUsers() throws Exception {
        // Initialize the database
        myUsersRepository.saveAndFlush(myUsers);

        // Get the myUsers
        restMyUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, myUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myUsers.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME));
    }

    @Test
    @Transactional
    void getNonExistingMyUsers() throws Exception {
        // Get the myUsers
        restMyUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyUsers() throws Exception {
        // Initialize the database
        myUsersRepository.saveAndFlush(myUsers);

        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();

        // Update the myUsers
        MyUsers updatedMyUsers = myUsersRepository.findById(myUsers.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyUsers are not directly saved in db
        em.detach(updatedMyUsers);
        updatedMyUsers.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyUsers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMyUsers))
            )
            .andExpect(status().isOk());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
        MyUsers testMyUsers = myUsersList.get(myUsersList.size() - 1);
        assertThat(testMyUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyUsers.getSurname()).isEqualTo(UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void putNonExistingMyUsers() throws Exception {
        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();
        myUsers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myUsers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyUsers() throws Exception {
        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();
        myUsers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyUsers() throws Exception {
        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();
        myUsers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myUsers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyUsersWithPatch() throws Exception {
        // Initialize the database
        myUsersRepository.saveAndFlush(myUsers);

        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();

        // Update the myUsers using partial update
        MyUsers partialUpdatedMyUsers = new MyUsers();
        partialUpdatedMyUsers.setId(myUsers.getId());

        restMyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyUsers))
            )
            .andExpect(status().isOk());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
        MyUsers testMyUsers = myUsersList.get(myUsersList.size() - 1);
        assertThat(testMyUsers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyUsers.getSurname()).isEqualTo(DEFAULT_SURNAME);
    }

    @Test
    @Transactional
    void fullUpdateMyUsersWithPatch() throws Exception {
        // Initialize the database
        myUsersRepository.saveAndFlush(myUsers);

        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();

        // Update the myUsers using partial update
        MyUsers partialUpdatedMyUsers = new MyUsers();
        partialUpdatedMyUsers.setId(myUsers.getId());

        partialUpdatedMyUsers.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyUsers))
            )
            .andExpect(status().isOk());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
        MyUsers testMyUsers = myUsersList.get(myUsersList.size() - 1);
        assertThat(testMyUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyUsers.getSurname()).isEqualTo(UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void patchNonExistingMyUsers() throws Exception {
        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();
        myUsers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyUsers() throws Exception {
        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();
        myUsers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myUsers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyUsers() throws Exception {
        int databaseSizeBeforeUpdate = myUsersRepository.findAll().size();
        myUsers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(myUsers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUsers in the database
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyUsers() throws Exception {
        // Initialize the database
        myUsersRepository.saveAndFlush(myUsers);

        int databaseSizeBeforeDelete = myUsersRepository.findAll().size();

        // Delete the myUsers
        restMyUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, myUsers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyUsers> myUsersList = myUsersRepository.findAll();
        assertThat(myUsersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
