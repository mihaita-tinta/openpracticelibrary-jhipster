package com.opl.api.service.mapper;

import com.opl.api.domain.PracticeItem;
import com.opl.api.domain.Tag;
import com.opl.api.service.dto.PracticeItemDTO;
import com.opl.api.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "practiceItem", source = "practiceItem", qualifiedByName = "practiceItemId")
    TagDTO toDto(Tag s);

    @Named("practiceItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PracticeItemDTO toDtoPracticeItemId(PracticeItem practiceItem);
}
