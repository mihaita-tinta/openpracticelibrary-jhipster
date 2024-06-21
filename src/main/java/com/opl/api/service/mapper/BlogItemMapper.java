package com.opl.api.service.mapper;

import com.opl.api.domain.BlogItem;
import com.opl.api.domain.MediaAsset;
import com.opl.api.service.dto.BlogItemDTO;
import com.opl.api.service.dto.MediaAssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BlogItem} and its DTO {@link BlogItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BlogItemMapper extends EntityMapper<BlogItemDTO, BlogItem> {
    @Mapping(target = "jumbotronImage", source = "jumbotronImage", qualifiedByName = "mediaAssetId")
    BlogItemDTO toDto(BlogItem s);

    @Named("mediaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MediaAssetDTO toDtoMediaAssetId(MediaAsset mediaAsset);
}
