package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MyFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyFileRepository extends JpaRepository<MyFile, Long> {}
