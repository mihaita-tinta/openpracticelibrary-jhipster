package com.opl.api.service.mapper;

import com.opl.api.domain.MediaAsset;
import com.opl.api.domain.PageItem;
import com.opl.api.service.dto.MediaAssetDTO;
import com.opl.api.service.dto.PageItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PageItem} and its DTO {@link PageItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface PageItemMapper extends EntityMapper<PageItemDTO, PageItem> {
    @Mapping(target = "jumbotronImage", source = "jumbotronImage", qualifiedByName = "mediaAssetId")
    PageItemDTO toDto(PageItem s);

    @Named("mediaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MediaAssetDTO toDtoMediaAssetId(MediaAsset mediaAsset);
}
