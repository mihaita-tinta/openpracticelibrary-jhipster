package com.opl.api.service.mapper;

import com.opl.api.domain.Author;
import com.opl.api.service.dto.AuthorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {}
