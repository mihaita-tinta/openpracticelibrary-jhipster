package com.opl.api.service.mapper;

import com.opl.api.domain.LinkItem;
import com.opl.api.domain.PracticeItem;
import com.opl.api.service.dto.LinkItemDTO;
import com.opl.api.service.dto.PracticeItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LinkItem} and its DTO {@link LinkItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface LinkItemMapper extends EntityMapper<LinkItemDTO, LinkItem> {
    @Mapping(target = "practiceItem", source = "practiceItem", qualifiedByName = "practiceItemId")
    LinkItemDTO toDto(LinkItem s);

    @Named("practiceItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PracticeItemDTO toDtoPracticeItemId(PracticeItem practiceItem);
}
