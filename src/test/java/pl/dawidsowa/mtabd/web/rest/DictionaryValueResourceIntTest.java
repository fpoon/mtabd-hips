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
import pl.dawidsowa.mtabd.domain.DictionaryValue;
import pl.dawidsowa.mtabd.repository.DictionaryValueRepository;
import pl.dawidsowa.mtabd.service.DictionaryValueService;
import pl.dawidsowa.mtabd.service.dto.DictionaryValueDTO;
import pl.dawidsowa.mtabd.service.mapper.DictionaryValueMapper;
import pl.dawidsowa.mtabd.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dawidsowa.mtabd.web.rest.TestUtil.createFormattingConversionService;

/**
 * Test class for the DictionaryValueResource REST controller.
 *
 * @see DictionaryValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtabdApp.class)
public class DictionaryValueResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LONG_NAME = "BBBBBBBBBB";

    @Autowired
    private DictionaryValueRepository dictionaryValueRepository;

    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;

    @Autowired
    private DictionaryValueService dictionaryValueService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDictionaryValueMockMvc;

    private DictionaryValue dictionaryValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DictionaryValueResource dictionaryValueResource = new DictionaryValueResource(dictionaryValueService);
        this.restDictionaryValueMockMvc = MockMvcBuilders.standaloneSetup(dictionaryValueResource)
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
    public static DictionaryValue createEntity(EntityManager em) {
        DictionaryValue dictionaryValue = new DictionaryValue()
            .name(DEFAULT_NAME)
            .longName(DEFAULT_LONG_NAME);
        return dictionaryValue;
    }

    @Before
    public void initTest() {
        dictionaryValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createDictionaryValue() throws Exception {
        int databaseSizeBeforeCreate = dictionaryValueRepository.findAll().size();

        // Create the DictionaryValue
        DictionaryValueDTO dictionaryValueDTO = dictionaryValueMapper.toDto(dictionaryValue);
        restDictionaryValueMockMvc.perform(post("/api/dictionary-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaryValueDTO)))
            .andExpect(status().isCreated());

        // Validate the DictionaryValue in the database
        List<DictionaryValue> dictionaryValueList = dictionaryValueRepository.findAll();
        assertThat(dictionaryValueList).hasSize(databaseSizeBeforeCreate + 1);
        DictionaryValue testDictionaryValue = dictionaryValueList.get(dictionaryValueList.size() - 1);
        assertThat(testDictionaryValue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDictionaryValue.getLongName()).isEqualTo(DEFAULT_LONG_NAME);
    }

    @Test
    @Transactional
    public void createDictionaryValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dictionaryValueRepository.findAll().size();

        // Create the DictionaryValue with an existing ID
        dictionaryValue.setId(1L);
        DictionaryValueDTO dictionaryValueDTO = dictionaryValueMapper.toDto(dictionaryValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDictionaryValueMockMvc.perform(post("/api/dictionary-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaryValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DictionaryValue in the database
        List<DictionaryValue> dictionaryValueList = dictionaryValueRepository.findAll();
        assertThat(dictionaryValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDictionaryValues() throws Exception {
        // Initialize the database
        dictionaryValueRepository.saveAndFlush(dictionaryValue);

        // Get all the dictionaryValueList
        restDictionaryValueMockMvc.perform(get("/api/dictionary-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dictionaryValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].longName").value(hasItem(DEFAULT_LONG_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDictionaryValue() throws Exception {
        // Initialize the database
        dictionaryValueRepository.saveAndFlush(dictionaryValue);

        // Get the dictionaryValue
        restDictionaryValueMockMvc.perform(get("/api/dictionary-values/{id}", dictionaryValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dictionaryValue.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.longName").value(DEFAULT_LONG_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDictionaryValue() throws Exception {
        // Get the dictionaryValue
        restDictionaryValueMockMvc.perform(get("/api/dictionary-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDictionaryValue() throws Exception {
        // Initialize the database
        dictionaryValueRepository.saveAndFlush(dictionaryValue);
        int databaseSizeBeforeUpdate = dictionaryValueRepository.findAll().size();

        // Update the dictionaryValue
        DictionaryValue updatedDictionaryValue = dictionaryValueRepository.findOne(dictionaryValue.getId());
        // Disconnect from session so that the updates on updatedDictionaryValue are not directly saved in db
        em.detach(updatedDictionaryValue);
        updatedDictionaryValue
            .name(UPDATED_NAME)
            .longName(UPDATED_LONG_NAME);
        DictionaryValueDTO dictionaryValueDTO = dictionaryValueMapper.toDto(updatedDictionaryValue);

        restDictionaryValueMockMvc.perform(put("/api/dictionary-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaryValueDTO)))
            .andExpect(status().isOk());

        // Validate the DictionaryValue in the database
        List<DictionaryValue> dictionaryValueList = dictionaryValueRepository.findAll();
        assertThat(dictionaryValueList).hasSize(databaseSizeBeforeUpdate);
        DictionaryValue testDictionaryValue = dictionaryValueList.get(dictionaryValueList.size() - 1);
        assertThat(testDictionaryValue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDictionaryValue.getLongName()).isEqualTo(UPDATED_LONG_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDictionaryValue() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryValueRepository.findAll().size();

        // Create the DictionaryValue
        DictionaryValueDTO dictionaryValueDTO = dictionaryValueMapper.toDto(dictionaryValue);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDictionaryValueMockMvc.perform(put("/api/dictionary-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaryValueDTO)))
            .andExpect(status().isCreated());

        // Validate the DictionaryValue in the database
        List<DictionaryValue> dictionaryValueList = dictionaryValueRepository.findAll();
        assertThat(dictionaryValueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDictionaryValue() throws Exception {
        // Initialize the database
        dictionaryValueRepository.saveAndFlush(dictionaryValue);
        int databaseSizeBeforeDelete = dictionaryValueRepository.findAll().size();

        // Get the dictionaryValue
        restDictionaryValueMockMvc.perform(delete("/api/dictionary-values/{id}", dictionaryValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DictionaryValue> dictionaryValueList = dictionaryValueRepository.findAll();
        assertThat(dictionaryValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DictionaryValue.class);
        DictionaryValue dictionaryValue1 = new DictionaryValue();
        dictionaryValue1.setId(1L);
        DictionaryValue dictionaryValue2 = new DictionaryValue();
        dictionaryValue2.setId(dictionaryValue1.getId());
        assertThat(dictionaryValue1).isEqualTo(dictionaryValue2);
        dictionaryValue2.setId(2L);
        assertThat(dictionaryValue1).isNotEqualTo(dictionaryValue2);
        dictionaryValue1.setId(null);
        assertThat(dictionaryValue1).isNotEqualTo(dictionaryValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DictionaryValueDTO.class);
        DictionaryValueDTO dictionaryValueDTO1 = new DictionaryValueDTO();
        dictionaryValueDTO1.setId(1L);
        DictionaryValueDTO dictionaryValueDTO2 = new DictionaryValueDTO();
        assertThat(dictionaryValueDTO1).isNotEqualTo(dictionaryValueDTO2);
        dictionaryValueDTO2.setId(dictionaryValueDTO1.getId());
        assertThat(dictionaryValueDTO1).isEqualTo(dictionaryValueDTO2);
        dictionaryValueDTO2.setId(2L);
        assertThat(dictionaryValueDTO1).isNotEqualTo(dictionaryValueDTO2);
        dictionaryValueDTO1.setId(null);
        assertThat(dictionaryValueDTO1).isNotEqualTo(dictionaryValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dictionaryValueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dictionaryValueMapper.fromId(null)).isNull();
    }
}
