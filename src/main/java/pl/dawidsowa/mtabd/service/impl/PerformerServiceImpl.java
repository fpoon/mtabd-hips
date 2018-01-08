package pl.dawidsowa.mtabd.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawidsowa.mtabd.domain.Performer;
import pl.dawidsowa.mtabd.repository.PerformerRepository;
import pl.dawidsowa.mtabd.service.PerformerService;
import pl.dawidsowa.mtabd.service.dto.PerformerDTO;
import pl.dawidsowa.mtabd.service.mapper.PerformerMapper;


/**
 * Service Implementation for managing Performer.
 */
@Service
@Transactional
public class PerformerServiceImpl implements PerformerService {

    private final Logger log = LoggerFactory.getLogger(PerformerServiceImpl.class);

    private final PerformerRepository performerRepository;

    private final PerformerMapper performerMapper;

    public PerformerServiceImpl(PerformerRepository performerRepository, PerformerMapper performerMapper) {
        this.performerRepository = performerRepository;
        this.performerMapper = performerMapper;
    }

    /**
     * Save a performer.
     *
     * @param performerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PerformerDTO save(PerformerDTO performerDTO) {
        log.debug("Request to save Performer : {}", performerDTO);
        Performer performer = performerMapper.toEntity(performerDTO);
        performer = performerRepository.save(performer);
        return performerMapper.toDto(performer);
    }

    /**
     * Get all the performers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PerformerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Performers");
        return performerRepository.findAll(pageable)
            .map(performerMapper::toDto);
    }

    /**
     * Get one performer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PerformerDTO findOne(Long id) {
        log.debug("Request to get Performer : {}", id);
        Performer performer = performerRepository.findOne(id);
        return performerMapper.toDto(performer);
    }

    /**
     * Delete the performer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Performer : {}", id);
        performerRepository.delete(id);
    }
}
