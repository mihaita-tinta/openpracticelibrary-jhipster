package com.opl.api.repository;

import com.opl.api.domain.MediaAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MediaAsset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {}
