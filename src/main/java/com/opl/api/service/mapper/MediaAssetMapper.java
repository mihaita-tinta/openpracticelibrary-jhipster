package com.opl.api.service.mapper;

import com.opl.api.domain.MediaAsset;
import com.opl.api.domain.PracticeItem;
import com.opl.api.service.dto.MediaAssetDTO;
import com.opl.api.service.dto.PracticeItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MediaAsset} and its DTO {@link MediaAssetDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaAssetMapper extends EntityMapper<MediaAssetDTO, MediaAsset> {
    @Mapping(target = "practices", source = "practices", qualifiedByName = "practiceItemId")
    MediaAssetDTO toDto(MediaAsset s);

    @Named("practiceItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PracticeItemDTO toDtoPracticeItemId(PracticeItem practiceItem);
}
