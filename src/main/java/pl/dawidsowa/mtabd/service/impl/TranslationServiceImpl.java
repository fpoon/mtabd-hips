package pl.dawidsowa.mtabd.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawidsowa.mtabd.domain.Translation;
import pl.dawidsowa.mtabd.repository.TranslationRepository;
import pl.dawidsowa.mtabd.service.TranslationService;
import pl.dawidsowa.mtabd.service.dto.TranslationDTO;
import pl.dawidsowa.mtabd.service.mapper.TranslationMapper;


/**
 * Service Implementation for managing Translation.
 */
@Service
@Transactional
public class TranslationServiceImpl implements TranslationService {

    private final Logger log = LoggerFactory.getLogger(TranslationServiceImpl.class);

    private final TranslationRepository translationRepository;

    private final TranslationMapper translationMapper;

    public TranslationServiceImpl(TranslationRepository translationRepository, TranslationMapper translationMapper) {
        this.translationRepository = translationRepository;
        this.translationMapper = translationMapper;
    }

    /**
     * Save a translation.
     *
     * @param translationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranslationDTO save(TranslationDTO translationDTO) {
        log.debug("Request to save Translation : {}", translationDTO);
        Translation translation = translationMapper.toEntity(translationDTO);
        translation = translationRepository.save(translation);
        return translationMapper.toDto(translation);
    }

    /**
     * Get all the translations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranslationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Translations");
        return translationRepository.findAll(pageable)
            .map(translationMapper::toDto);
    }

    /**
     * Get one translation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranslationDTO findOne(Long id) {
        log.debug("Request to get Translation : {}", id);
        Translation translation = translationRepository.findOne(id);
        return translationMapper.toDto(translation);
    }

    /**
     * Delete the translation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Translation : {}", id);
        translationRepository.delete(id);
    }
}
