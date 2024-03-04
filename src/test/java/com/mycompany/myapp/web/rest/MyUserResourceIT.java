package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyUser;
import com.mycompany.myapp.repository.MyUserRepository;
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
 * Integration tests for the {@link MyUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyUserMockMvc;

    private MyUser myUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUser createEntity(EntityManager em) {
        MyUser myUser = new MyUser().name(DEFAULT_NAME).surname(DEFAULT_SURNAME);
        return myUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUser createUpdatedEntity(EntityManager em) {
        MyUser myUser = new MyUser().name(UPDATED_NAME).surname(UPDATED_SURNAME);
        return myUser;
    }

    @BeforeEach
    public void initTest() {
        myUser = createEntity(em);
    }

    @Test
    @Transactional
    void createMyUser() throws Exception {
        int databaseSizeBeforeCreate = myUserRepository.findAll().size();
        // Create the MyUser
        restMyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myUser)))
            .andExpect(status().isCreated());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeCreate + 1);
        MyUser testMyUser = myUserList.get(myUserList.size() - 1);
        assertThat(testMyUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyUser.getSurname()).isEqualTo(DEFAULT_SURNAME);
    }

    @Test
    @Transactional
    void createMyUserWithExistingId() throws Exception {
        // Create the MyUser with an existing ID
        myUser.setId(1L);

        int databaseSizeBeforeCreate = myUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myUser)))
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyUsers() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        // Get all the myUserList
        restMyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)));
    }

    @Test
    @Transactional
    void getMyUser() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        // Get the myUser
        restMyUserMockMvc
            .perform(get(ENTITY_API_URL_ID, myUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME));
    }

    @Test
    @Transactional
    void getNonExistingMyUser() throws Exception {
        // Get the myUser
        restMyUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyUser() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();

        // Update the myUser
        MyUser updatedMyUser = myUserRepository.findById(myUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyUser are not directly saved in db
        em.detach(updatedMyUser);
        updatedMyUser.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMyUser))
            )
            .andExpect(status().isOk());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
        MyUser testMyUser = myUserList.get(myUserList.size() - 1);
        assertThat(testMyUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyUser.getSurname()).isEqualTo(UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void putNonExistingMyUser() throws Exception {
        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();
        myUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyUser() throws Exception {
        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyUser() throws Exception {
        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(myUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyUserWithPatch() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();

        // Update the myUser using partial update
        MyUser partialUpdatedMyUser = new MyUser();
        partialUpdatedMyUser.setId(myUser.getId());

        partialUpdatedMyUser.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyUser))
            )
            .andExpect(status().isOk());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
        MyUser testMyUser = myUserList.get(myUserList.size() - 1);
        assertThat(testMyUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyUser.getSurname()).isEqualTo(UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void fullUpdateMyUserWithPatch() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();

        // Update the myUser using partial update
        MyUser partialUpdatedMyUser = new MyUser();
        partialUpdatedMyUser.setId(myUser.getId());

        partialUpdatedMyUser.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMyUser))
            )
            .andExpect(status().isOk());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
        MyUser testMyUser = myUserList.get(myUserList.size() - 1);
        assertThat(testMyUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyUser.getSurname()).isEqualTo(UPDATED_SURNAME);
    }

    @Test
    @Transactional
    void patchNonExistingMyUser() throws Exception {
        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();
        myUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyUser() throws Exception {
        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyUser() throws Exception {
        int databaseSizeBeforeUpdate = myUserRepository.findAll().size();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(myUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUser in the database
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyUser() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        int databaseSizeBeforeDelete = myUserRepository.findAll().size();

        // Delete the myUser
        restMyUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, myUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyUser> myUserList = myUserRepository.findAll();
        assertThat(myUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
