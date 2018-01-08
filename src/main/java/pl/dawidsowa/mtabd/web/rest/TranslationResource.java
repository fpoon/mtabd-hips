package pl.dawidsowa.mtabd.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawidsowa.mtabd.service.TranslationService;
import pl.dawidsowa.mtabd.service.dto.TranslationDTO;
import pl.dawidsowa.mtabd.web.rest.errors.BadRequestAlertException;
import pl.dawidsowa.mtabd.web.rest.util.HeaderUtil;
import pl.dawidsowa.mtabd.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Translation.
 */
@RestController
@RequestMapping("/api")
public class TranslationResource {

    private final Logger log = LoggerFactory.getLogger(TranslationResource.class);

    private static final String ENTITY_NAME = "translation";

    private final TranslationService translationService;

    public TranslationResource(TranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * POST  /translations : Create a new translation.
     *
     * @param translationDTO the translationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new translationDTO, or with status 400 (Bad Request) if the translation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/translations")
    @Timed
    public ResponseEntity<TranslationDTO> createTranslation(@RequestBody TranslationDTO translationDTO) throws URISyntaxException {
        log.debug("REST request to save Translation : {}", translationDTO);
        if (translationDTO.getId() != null) {
            throw new BadRequestAlertException("A new translation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TranslationDTO result = translationService.save(translationDTO);
        return ResponseEntity.created(new URI("/api/translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /translations : Updates an existing translation.
     *
     * @param translationDTO the translationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated translationDTO,
     * or with status 400 (Bad Request) if the translationDTO is not valid,
     * or with status 500 (Internal Server Error) if the translationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/translations")
    @Timed
    public ResponseEntity<TranslationDTO> updateTranslation(@RequestBody TranslationDTO translationDTO) throws URISyntaxException {
        log.debug("REST request to update Translation : {}", translationDTO);
        if (translationDTO.getId() == null) {
            return createTranslation(translationDTO);
        }
        TranslationDTO result = translationService.save(translationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, translationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /translations : get all the translations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of translations in body
     */
    @GetMapping("/translations")
    @Timed
    public ResponseEntity<List<TranslationDTO>> getAllTranslations(Pageable pageable) {
        log.debug("REST request to get a page of Translations");
        Page<TranslationDTO> page = translationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/translations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /translations/:id : get the "id" translation.
     *
     * @param id the id of the translationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the translationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/translations/{id}")
    @Timed
    public ResponseEntity<TranslationDTO> getTranslation(@PathVariable Long id) {
        log.debug("REST request to get Translation : {}", id);
        TranslationDTO translationDTO = translationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(translationDTO));
    }

    /**
     * DELETE  /translations/:id : delete the "id" translation.
     *
     * @param id the id of the translationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/translations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        log.debug("REST request to delete Translation : {}", id);
        translationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
