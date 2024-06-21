package com.opl.api.repository;

import com.opl.api.domain.PracticeItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PracticeItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PracticeItemRepository extends JpaRepository<PracticeItem, Long> {}
