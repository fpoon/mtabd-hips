package pl.dawidsowa.mtabd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dawidsowa.mtabd.service.dto.DictionaryValueDTO;

/**
 * Service Interface for managing DictionaryValue.
 */
public interface DictionaryValueService {

    /**
     * Save a dictionaryValue.
     *
     * @param dictionaryValueDTO the entity to save
     * @return the persisted entity
     */
    DictionaryValueDTO save(DictionaryValueDTO dictionaryValueDTO);

    /**
     * Get all the dictionaryValues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DictionaryValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dictionaryValue.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DictionaryValueDTO findOne(Long id);

    /**
     * Delete the "id" dictionaryValue.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
