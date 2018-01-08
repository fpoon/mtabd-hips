package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import pl.dawidsowa.mtabd.domain.Translation;
import pl.dawidsowa.mtabd.service.dto.TranslationDTO;

/**
 * Mapper for the entity Translation and its DTO TranslationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranslationMapper extends EntityMapper<TranslationDTO, Translation> {



    default Translation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Translation translation = new Translation();
        translation.setId(id);
        return translation;
    }
}
