package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dawidsowa.mtabd.domain.Service;
import pl.dawidsowa.mtabd.service.dto.ServiceDTO;

/**
 * Mapper for the entity Service and its DTO ServiceDTO.
 */
@Mapper(componentModel = "spring", uses = {PerformerMapper.class})
public interface ServiceMapper extends EntityMapper<ServiceDTO, Service> {

    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "performer.id", target = "performerId")
    ServiceDTO toDto(Service service);

    @Mapping(source = "serviceId", target = "service")
    @Mapping(source = "performerId", target = "performer")
    Service toEntity(ServiceDTO serviceDTO);

    default Service fromId(Long id) {
        if (id == null) {
            return null;
        }
        Service service = new Service();
        service.setId(id);
        return service;
    }
}
