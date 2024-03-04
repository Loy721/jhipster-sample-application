package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.UserTest;
import com.mycompany.myapp.repository.UserTestRepository;
import com.mycompany.myapp.service.UserTestService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.UserTest}.
 */
@Service
@Transactional
public class UserTestServiceImpl implements UserTestService {

    private final Logger log = LoggerFactory.getLogger(UserTestServiceImpl.class);

    private final UserTestRepository userTestRepository;

    public UserTestServiceImpl(UserTestRepository userTestRepository) {
        this.userTestRepository = userTestRepository;
    }

    @Override
    public UserTest save(UserTest userTest) {
        log.debug("Request to save UserTest : {}", userTest);
        return userTestRepository.save(userTest);
    }

    @Override
    public UserTest update(UserTest userTest) {
        log.debug("Request to update UserTest : {}", userTest);
        return userTestRepository.save(userTest);
    }

    @Override
    public Optional<UserTest> partialUpdate(UserTest userTest) {
        log.debug("Request to partially update UserTest : {}", userTest);

        return userTestRepository
            .findById(userTest.getId())
            .map(existingUserTest -> {
                if (userTest.getGrade() != null) {
                    existingUserTest.setGrade(userTest.getGrade());
                }

                return existingUserTest;
            })
            .map(userTestRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTest> findAll() {
        log.debug("Request to get all UserTests");
        return userTestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserTest> findOne(Long id) {
        log.debug("Request to get UserTest : {}", id);
        return userTestRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserTest : {}", id);
        userTestRepository.deleteById(id);
    }
}
