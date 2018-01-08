package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import pl.dawidsowa.mtabd.domain.Performer;
import pl.dawidsowa.mtabd.service.dto.PerformerDTO;

/**
 * Mapper for the entity Performer and its DTO PerformerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PerformerMapper extends EntityMapper<PerformerDTO, Performer> {



    default Performer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Performer performer = new Performer();
        performer.setId(id);
        return performer;
    }
}
