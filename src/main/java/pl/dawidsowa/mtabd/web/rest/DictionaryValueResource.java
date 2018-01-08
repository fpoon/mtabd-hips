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
import pl.dawidsowa.mtabd.service.DictionaryValueService;
import pl.dawidsowa.mtabd.service.dto.DictionaryValueDTO;
import pl.dawidsowa.mtabd.web.rest.errors.BadRequestAlertException;
import pl.dawidsowa.mtabd.web.rest.util.HeaderUtil;
import pl.dawidsowa.mtabd.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DictionaryValue.
 */
@RestController
@RequestMapping("/api")
public class DictionaryValueResource {

    private final Logger log = LoggerFactory.getLogger(DictionaryValueResource.class);

    private static final String ENTITY_NAME = "dictionaryValue";

    private final DictionaryValueService dictionaryValueService;

    public DictionaryValueResource(DictionaryValueService dictionaryValueService) {
        this.dictionaryValueService = dictionaryValueService;
    }

    /**
     * POST  /dictionary-values : Create a new dictionaryValue.
     *
     * @param dictionaryValueDTO the dictionaryValueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dictionaryValueDTO, or with status 400 (Bad Request) if the dictionaryValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dictionary-values")
    @Timed
    public ResponseEntity<DictionaryValueDTO> createDictionaryValue(@RequestBody DictionaryValueDTO dictionaryValueDTO) throws URISyntaxException {
        log.debug("REST request to save DictionaryValue : {}", dictionaryValueDTO);
        if (dictionaryValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new dictionaryValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DictionaryValueDTO result = dictionaryValueService.save(dictionaryValueDTO);
        return ResponseEntity.created(new URI("/api/dictionary-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dictionary-values : Updates an existing dictionaryValue.
     *
     * @param dictionaryValueDTO the dictionaryValueDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dictionaryValueDTO,
     * or with status 400 (Bad Request) if the dictionaryValueDTO is not valid,
     * or with status 500 (Internal Server Error) if the dictionaryValueDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dictionary-values")
    @Timed
    public ResponseEntity<DictionaryValueDTO> updateDictionaryValue(@RequestBody DictionaryValueDTO dictionaryValueDTO) throws URISyntaxException {
        log.debug("REST request to update DictionaryValue : {}", dictionaryValueDTO);
        if (dictionaryValueDTO.getId() == null) {
            return createDictionaryValue(dictionaryValueDTO);
        }
        DictionaryValueDTO result = dictionaryValueService.save(dictionaryValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dictionaryValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dictionary-values : get all the dictionaryValues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dictionaryValues in body
     */
    @GetMapping("/dictionary-values")
    @Timed
    public ResponseEntity<List<DictionaryValueDTO>> getAllDictionaryValues(Pageable pageable) {
        log.debug("REST request to get a page of DictionaryValues");
        Page<DictionaryValueDTO> page = dictionaryValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dictionary-values");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dictionary-values/:id : get the "id" dictionaryValue.
     *
     * @param id the id of the dictionaryValueDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dictionaryValueDTO, or with status 404 (Not Found)
     */
    @GetMapping("/dictionary-values/{id}")
    @Timed
    public ResponseEntity<DictionaryValueDTO> getDictionaryValue(@PathVariable Long id) {
        log.debug("REST request to get DictionaryValue : {}", id);
        DictionaryValueDTO dictionaryValueDTO = dictionaryValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dictionaryValueDTO));
    }

    /**
     * DELETE  /dictionary-values/:id : delete the "id" dictionaryValue.
     *
     * @param id the id of the dictionaryValueDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dictionary-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteDictionaryValue(@PathVariable Long id) {
        log.debug("REST request to delete DictionaryValue : {}", id);
        dictionaryValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
