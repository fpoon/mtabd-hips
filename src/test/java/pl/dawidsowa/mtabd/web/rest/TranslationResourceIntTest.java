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
import pl.dawidsowa.mtabd.domain.Translation;
import pl.dawidsowa.mtabd.repository.TranslationRepository;
import pl.dawidsowa.mtabd.service.TranslationService;
import pl.dawidsowa.mtabd.service.dto.TranslationDTO;
import pl.dawidsowa.mtabd.service.mapper.TranslationMapper;
import pl.dawidsowa.mtabd.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dawidsowa.mtabd.web.rest.TestUtil.createFormattingConversionService;

/**
 * Test class for the TranslationResource REST controller.
 *
 * @see TranslationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtabdApp.class)
public class TranslationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private TranslationMapper translationMapper;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranslationMockMvc;

    private Translation translation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TranslationResource translationResource = new TranslationResource(translationService);
        this.restTranslationMockMvc = MockMvcBuilders.standaloneSetup(translationResource)
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
    public static Translation createEntity(EntityManager em) {
        Translation translation = new Translation()
            .name(DEFAULT_NAME);
        return translation;
    }

    @Before
    public void initTest() {
        translation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranslation() throws Exception {
        int databaseSizeBeforeCreate = translationRepository.findAll().size();

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);
        restTranslationMockMvc.perform(post("/api/translations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translationDTO)))
            .andExpect(status().isCreated());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeCreate + 1);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTranslationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = translationRepository.findAll().size();

        // Create the Translation with an existing ID
        translation.setId(1L);
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranslationMockMvc.perform(post("/api/translations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTranslations() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList
        restTranslationMockMvc.perform(get("/api/translations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(translation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get the translation
        restTranslationMockMvc.perform(get("/api/translations/{id}", translation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(translation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranslation() throws Exception {
        // Get the translation
        restTranslationMockMvc.perform(get("/api/translations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation
        Translation updatedTranslation = translationRepository.findOne(translation.getId());
        // Disconnect from session so that the updates on updatedTranslation are not directly saved in db
        em.detach(updatedTranslation);
        updatedTranslation
            .name(UPDATED_NAME);
        TranslationDTO translationDTO = translationMapper.toDto(updatedTranslation);

        restTranslationMockMvc.perform(put("/api/translations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translationDTO)))
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTranslationMockMvc.perform(put("/api/translations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translationDTO)))
            .andExpect(status().isCreated());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);
        int databaseSizeBeforeDelete = translationRepository.findAll().size();

        // Get the translation
        restTranslationMockMvc.perform(delete("/api/translations/{id}", translation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Translation.class);
        Translation translation1 = new Translation();
        translation1.setId(1L);
        Translation translation2 = new Translation();
        translation2.setId(translation1.getId());
        assertThat(translation1).isEqualTo(translation2);
        translation2.setId(2L);
        assertThat(translation1).isNotEqualTo(translation2);
        translation1.setId(null);
        assertThat(translation1).isNotEqualTo(translation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranslationDTO.class);
        TranslationDTO translationDTO1 = new TranslationDTO();
        translationDTO1.setId(1L);
        TranslationDTO translationDTO2 = new TranslationDTO();
        assertThat(translationDTO1).isNotEqualTo(translationDTO2);
        translationDTO2.setId(translationDTO1.getId());
        assertThat(translationDTO1).isEqualTo(translationDTO2);
        translationDTO2.setId(2L);
        assertThat(translationDTO1).isNotEqualTo(translationDTO2);
        translationDTO1.setId(null);
        assertThat(translationDTO1).isNotEqualTo(translationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(translationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(translationMapper.fromId(null)).isNull();
    }
}
