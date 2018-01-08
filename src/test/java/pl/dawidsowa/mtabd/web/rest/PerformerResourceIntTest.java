package pl.dawidsowa.mtabd.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.dawidsowa.mtabd.MtabdApp;
import pl.dawidsowa.mtabd.domain.Performer;
import pl.dawidsowa.mtabd.repository.PerformerRepository;
import pl.dawidsowa.mtabd.service.PerformerService;
import pl.dawidsowa.mtabd.service.dto.PerformerDTO;
import pl.dawidsowa.mtabd.service.mapper.PerformerMapper;
import pl.dawidsowa.mtabd.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dawidsowa.mtabd.web.rest.TestUtil.createFormattingConversionService;

/**
 * Test class for the PerformerResource REST controller.
 *
 * @see PerformerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtabdApp.class)
public class PerformerResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Autowired
    private PerformerRepository performerRepository;

    @Autowired
    private PerformerMapper performerMapper;

    @Autowired
    private PerformerService performerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPerformerMockMvc;

    private Performer performer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PerformerResource performerResource = new PerformerResource(performerService);
        this.restPerformerMockMvc = MockMvcBuilders.standaloneSetup(performerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Performer createEntity(EntityManager em) {
        Performer performer = new Performer()
            .username(DEFAULT_USERNAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .age(DEFAULT_AGE);
        return performer;
    }

    @Before
    public void initTest() {
        performer = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerformer() throws Exception {
        int databaseSizeBeforeCreate = performerRepository.findAll().size();

        // Create the Performer
        PerformerDTO performerDTO = performerMapper.toDto(performer);
        restPerformerMockMvc.perform(post("/api/performers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performerDTO)))
            .andExpect(status().isCreated());

        // Validate the Performer in the database
        List<Performer> performerList = performerRepository.findAll();
        assertThat(performerList).hasSize(databaseSizeBeforeCreate + 1);
        Performer testPerformer = performerList.get(performerList.size() - 1);
        assertThat(testPerformer.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testPerformer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPerformer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerformer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPerformer.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createPerformerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = performerRepository.findAll().size();

        // Create the Performer with an existing ID
        performer.setId(1L);
        PerformerDTO performerDTO = performerMapper.toDto(performer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerformerMockMvc.perform(post("/api/performers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Performer in the database
        List<Performer> performerList = performerRepository.findAll();
        assertThat(performerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPerformers() throws Exception {
        // Initialize the database
        performerRepository.saveAndFlush(performer);

        // Get all the performerList
        restPerformerMockMvc.perform(get("/api/performers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performer.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    @Transactional
    public void getPerformer() throws Exception {
        // Initialize the database
        performerRepository.saveAndFlush(performer);

        // Get the performer
        restPerformerMockMvc.perform(get("/api/performers/{id}", performer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(performer.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getNonExistingPerformer() throws Exception {
        // Get the performer
        restPerformerMockMvc.perform(get("/api/performers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerformer() throws Exception {
        // Initialize the database
        performerRepository.saveAndFlush(performer);
        int databaseSizeBeforeUpdate = performerRepository.findAll().size();

        // Update the performer
        Performer updatedPerformer = performerRepository.findOne(performer.getId());
        // Disconnect from session so that the updates on updatedPerformer are not directly saved in db
        em.detach(updatedPerformer);
        updatedPerformer
            .username(UPDATED_USERNAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .age(UPDATED_AGE);
        PerformerDTO performerDTO = performerMapper.toDto(updatedPerformer);

        restPerformerMockMvc.perform(put("/api/performers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performerDTO)))
            .andExpect(status().isOk());

        // Validate the Performer in the database
        List<Performer> performerList = performerRepository.findAll();
        assertThat(performerList).hasSize(databaseSizeBeforeUpdate);
        Performer testPerformer = performerList.get(performerList.size() - 1);
        assertThat(testPerformer.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testPerformer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPerformer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerformer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPerformer.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingPerformer() throws Exception {
        int databaseSizeBeforeUpdate = performerRepository.findAll().size();

        // Create the Performer
        PerformerDTO performerDTO = performerMapper.toDto(performer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPerformerMockMvc.perform(put("/api/performers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performerDTO)))
            .andExpect(status().isCreated());

        // Validate the Performer in the database
        List<Performer> performerList = performerRepository.findAll();
        assertThat(performerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePerformer() throws Exception {
        // Initialize the database
        performerRepository.saveAndFlush(performer);
        int databaseSizeBeforeDelete = performerRepository.findAll().size();

        // Get the performer
        restPerformerMockMvc.perform(delete("/api/performers/{id}", performer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Performer> performerList = performerRepository.findAll();
        assertThat(performerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Performer.class);
        Performer performer1 = new Performer();
        performer1.setId(1L);
        Performer performer2 = new Performer();
        performer2.setId(performer1.getId());
        assertThat(performer1).isEqualTo(performer2);
        performer2.setId(2L);
        assertThat(performer1).isNotEqualTo(performer2);
        performer1.setId(null);
        assertThat(performer1).isNotEqualTo(performer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformerDTO.class);
        PerformerDTO performerDTO1 = new PerformerDTO();
        performerDTO1.setId(1L);
        PerformerDTO performerDTO2 = new PerformerDTO();
        assertThat(performerDTO1).isNotEqualTo(performerDTO2);
        performerDTO2.setId(performerDTO1.getId());
        assertThat(performerDTO1).isEqualTo(performerDTO2);
        performerDTO2.setId(2L);
        assertThat(performerDTO1).isNotEqualTo(performerDTO2);
        performerDTO1.setId(null);
        assertThat(performerDTO1).isNotEqualTo(performerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(performerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(performerMapper.fromId(null)).isNull();
    }
}
