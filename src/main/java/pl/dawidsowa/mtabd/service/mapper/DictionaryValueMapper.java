package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dawidsowa.mtabd.domain.DictionaryValue;
import pl.dawidsowa.mtabd.service.dto.DictionaryValueDTO;

/**
 * Mapper for the entity DictionaryValue and its DTO DictionaryValueDTO.
 */
@Mapper(componentModel = "spring", uses = {TranslationMapper.class, DictionaryMapper.class})
public interface DictionaryValueMapper extends EntityMapper<DictionaryValueDTO, DictionaryValue> {

    @Mapping(source = "translation.id", target = "translationId")
    @Mapping(source = "dictionary.id", target = "dictionaryId")
    DictionaryValueDTO toDto(DictionaryValue dictionaryValue);

    @Mapping(source = "translationId", target = "translation")
    @Mapping(source = "dictionaryId", target = "dictionary")
    DictionaryValue toEntity(DictionaryValueDTO dictionaryValueDTO);

    default DictionaryValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        DictionaryValue dictionaryValue = new DictionaryValue();
        dictionaryValue.setId(id);
        return dictionaryValue;
    }
}
