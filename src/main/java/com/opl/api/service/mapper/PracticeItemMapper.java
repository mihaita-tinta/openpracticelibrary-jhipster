package com.opl.api.service.mapper;

import com.opl.api.domain.MediaAsset;
import com.opl.api.domain.PracticeItem;
import com.opl.api.service.dto.MediaAssetDTO;
import com.opl.api.service.dto.PracticeItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PracticeItem} and its DTO {@link PracticeItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface PracticeItemMapper extends EntityMapper<PracticeItemDTO, PracticeItem> {
    @Mapping(target = "coverImage", source = "coverImage", qualifiedByName = "mediaAssetId")
    PracticeItemDTO toDto(PracticeItem s);

    @Named("mediaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MediaAssetDTO toDtoMediaAssetId(MediaAsset mediaAsset);
}
