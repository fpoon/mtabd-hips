package pl.dawidsowa.mtabd.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawidsowa.mtabd.domain.Dictionary;
import pl.dawidsowa.mtabd.repository.DictionaryRepository;
import pl.dawidsowa.mtabd.service.DictionaryService;
import pl.dawidsowa.mtabd.service.dto.DictionaryDTO;
import pl.dawidsowa.mtabd.service.mapper.DictionaryMapper;


/**
 * Service Implementation for managing Dictionary.
 */
@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

    private final Logger log = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    private final DictionaryRepository dictionaryRepository;

    private final DictionaryMapper dictionaryMapper;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, DictionaryMapper dictionaryMapper) {
        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryMapper = dictionaryMapper;
    }

    /**
     * Save a dictionary.
     *
     * @param dictionaryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DictionaryDTO save(DictionaryDTO dictionaryDTO) {
        log.debug("Request to save Dictionary : {}", dictionaryDTO);
        Dictionary dictionary = dictionaryMapper.toEntity(dictionaryDTO);
        dictionary = dictionaryRepository.save(dictionary);
        return dictionaryMapper.toDto(dictionary);
    }

    /**
     * Get all the dictionaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DictionaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dictionaries");
        return dictionaryRepository.findAll(pageable)
            .map(dictionaryMapper::toDto);
    }

    /**
     * Get one dictionary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DictionaryDTO findOne(Long id) {
        log.debug("Request to get Dictionary : {}", id);
        Dictionary dictionary = dictionaryRepository.findOne(id);
        return dictionaryMapper.toDto(dictionary);
    }

    /**
     * Delete the dictionary by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dictionary : {}", id);
        dictionaryRepository.delete(id);
    }
}
