package com.opl.api.repository;

import com.opl.api.domain.LinkItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LinkItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LinkItemRepository extends JpaRepository<LinkItem, Long> {}
