package pl.dawidsowa.mtabd.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawidsowa.mtabd.domain.DictionaryValue;
import pl.dawidsowa.mtabd.repository.DictionaryValueRepository;
import pl.dawidsowa.mtabd.service.DictionaryValueService;
import pl.dawidsowa.mtabd.service.dto.DictionaryValueDTO;
import pl.dawidsowa.mtabd.service.mapper.DictionaryValueMapper;


/**
 * Service Implementation for managing DictionaryValue.
 */
@Service
@Transactional
public class DictionaryValueServiceImpl implements DictionaryValueService {

    private final Logger log = LoggerFactory.getLogger(DictionaryValueServiceImpl.class);

    private final DictionaryValueRepository dictionaryValueRepository;

    private final DictionaryValueMapper dictionaryValueMapper;

    public DictionaryValueServiceImpl(DictionaryValueRepository dictionaryValueRepository, DictionaryValueMapper dictionaryValueMapper) {
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.dictionaryValueMapper = dictionaryValueMapper;
    }

    /**
     * Save a dictionaryValue.
     *
     * @param dictionaryValueDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DictionaryValueDTO save(DictionaryValueDTO dictionaryValueDTO) {
        log.debug("Request to save DictionaryValue : {}", dictionaryValueDTO);
        DictionaryValue dictionaryValue = dictionaryValueMapper.toEntity(dictionaryValueDTO);
        dictionaryValue = dictionaryValueRepository.save(dictionaryValue);
        return dictionaryValueMapper.toDto(dictionaryValue);
    }

    /**
     * Get all the dictionaryValues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DictionaryValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DictionaryValues");
        return dictionaryValueRepository.findAll(pageable)
            .map(dictionaryValueMapper::toDto);
    }

    /**
     * Get one dictionaryValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DictionaryValueDTO findOne(Long id) {
        log.debug("Request to get DictionaryValue : {}", id);
        DictionaryValue dictionaryValue = dictionaryValueRepository.findOne(id);
        return dictionaryValueMapper.toDto(dictionaryValue);
    }

    /**
     * Delete the dictionaryValue by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DictionaryValue : {}", id);
        dictionaryValueRepository.delete(id);
    }
}
