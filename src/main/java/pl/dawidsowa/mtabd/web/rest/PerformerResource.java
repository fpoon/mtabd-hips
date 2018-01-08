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
import pl.dawidsowa.mtabd.service.PerformerService;
import pl.dawidsowa.mtabd.service.dto.PerformerDTO;
import pl.dawidsowa.mtabd.web.rest.errors.BadRequestAlertException;
import pl.dawidsowa.mtabd.web.rest.util.HeaderUtil;
import pl.dawidsowa.mtabd.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Performer.
 */
@RestController
@RequestMapping("/api")
public class PerformerResource {

    private final Logger log = LoggerFactory.getLogger(PerformerResource.class);

    private static final String ENTITY_NAME = "performer";

    private final PerformerService performerService;

    public PerformerResource(PerformerService performerService) {
        this.performerService = performerService;
    }

    /**
     * POST  /performers : Create a new performer.
     *
     * @param performerDTO the performerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new performerDTO, or with status 400 (Bad Request) if the performer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/performers")
    @Timed
    public ResponseEntity<PerformerDTO> createPerformer(@RequestBody PerformerDTO performerDTO) throws URISyntaxException {
        log.debug("REST request to save Performer : {}", performerDTO);
        if (performerDTO.getId() != null) {
            throw new BadRequestAlertException("A new performer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerformerDTO result = performerService.save(performerDTO);
        return ResponseEntity.created(new URI("/api/performers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /performers : Updates an existing performer.
     *
     * @param performerDTO the performerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated performerDTO,
     * or with status 400 (Bad Request) if the performerDTO is not valid,
     * or with status 500 (Internal Server Error) if the performerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/performers")
    @Timed
    public ResponseEntity<PerformerDTO> updatePerformer(@RequestBody PerformerDTO performerDTO) throws URISyntaxException {
        log.debug("REST request to update Performer : {}", performerDTO);
        if (performerDTO.getId() == null) {
            return createPerformer(performerDTO);
        }
        PerformerDTO result = performerService.save(performerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, performerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /performers : get all the performers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of performers in body
     */
    @GetMapping("/performers")
    @Timed
    public ResponseEntity<List<PerformerDTO>> getAllPerformers(Pageable pageable) {
        log.debug("REST request to get a page of Performers");
        Page<PerformerDTO> page = performerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/performers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /performers/:id : get the "id" performer.
     *
     * @param id the id of the performerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the performerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/performers/{id}")
    @Timed
    public ResponseEntity<PerformerDTO> getPerformer(@PathVariable Long id) {
        log.debug("REST request to get Performer : {}", id);
        PerformerDTO performerDTO = performerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(performerDTO));
    }

    /**
     * DELETE  /performers/:id : delete the "id" performer.
     *
     * @param id the id of the performerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/performers/{id}")
    @Timed
    public ResponseEntity<Void> deletePerformer(@PathVariable Long id) {
        log.debug("REST request to delete Performer : {}", id);
        performerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
