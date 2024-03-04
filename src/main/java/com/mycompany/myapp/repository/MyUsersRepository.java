package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MyUsers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyUsers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyUsersRepository extends JpaRepository<MyUsers, Long> {}
