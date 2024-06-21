package com.opl.api.repository;

import com.opl.api.domain.BlogItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BlogItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogItemRepository extends JpaRepository<BlogItem, Long> {}
