package pl.dawidsowa.mtabd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dawidsowa.mtabd.service.dto.PerformerDTO;

/**
 * Service Interface for managing Performer.
 */
public interface PerformerService {

    /**
     * Save a performer.
     *
     * @param performerDTO the entity to save
     * @return the persisted entity
     */
    PerformerDTO save(PerformerDTO performerDTO);

    /**
     * Get all the performers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PerformerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" performer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PerformerDTO findOne(Long id);

    /**
     * Delete the "id" performer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
