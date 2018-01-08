package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import pl.dawidsowa.mtabd.domain.Dictionary;
import pl.dawidsowa.mtabd.service.dto.DictionaryDTO;

/**
 * Mapper for the entity Dictionary and its DTO DictionaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DictionaryMapper extends EntityMapper<DictionaryDTO, Dictionary> {



    default Dictionary fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
