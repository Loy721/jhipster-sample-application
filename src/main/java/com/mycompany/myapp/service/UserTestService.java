package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserTest;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.UserTest}.
 */
public interface UserTestService {
    /**
     * Save a userTest.
     *
     * @param userTest the entity to save.
     * @return the persisted entity.
     */
    UserTest save(UserTest userTest);

    /**
     * Updates a userTest.
     *
     * @param userTest the entity to update.
     * @return the persisted entity.
     */
    UserTest update(UserTest userTest);

    /**
     * Partially updates a userTest.
     *
     * @param userTest the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserTest> partialUpdate(UserTest userTest);

    /**
     * Get all the userTests.
     *
     * @return the list of entities.
     */
    List<UserTest> findAll();

    /**
     * Get the "id" userTest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserTest> findOne(Long id);

    /**
     * Delete the "id" userTest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
