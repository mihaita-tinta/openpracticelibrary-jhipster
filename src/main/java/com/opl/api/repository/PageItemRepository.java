package com.opl.api.repository;

import com.opl.api.domain.PageItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PageItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageItemRepository extends JpaRepository<PageItem, Long> {}
