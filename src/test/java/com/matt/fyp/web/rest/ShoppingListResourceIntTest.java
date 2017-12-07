package com.matt.fyp.web.rest;

import com.matt.fyp.FyPv6App;

import com.matt.fyp.domain.ShoppingList;
import com.matt.fyp.repository.ShoppingListRepository;
import com.matt.fyp.repository.search.ShoppingListSearchRepository;
import com.matt.fyp.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static com.matt.fyp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ShoppingListResource REST controller.
 *
 * @see ShoppingListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FyPv6App.class)
public class ShoppingListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListSearchRepository shoppingListSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShoppingListMockMvc;

    private ShoppingList shoppingList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShoppingListResource shoppingListResource = new ShoppingListResource(shoppingListRepository, shoppingListSearchRepository);
        this.restShoppingListMockMvc = MockMvcBuilders.standaloneSetup(shoppingListResource)
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
    public static ShoppingList createEntity(EntityManager em) {
        ShoppingList shoppingList = new ShoppingList()
            .name(DEFAULT_NAME)
            .notes(DEFAULT_NOTES);
        return shoppingList;
    }

    @Before
    public void initTest() {
        shoppingListSearchRepository.deleteAll();
        shoppingList = createEntity(em);
    }

    @Test
    @Transactional
    public void createShoppingList() throws Exception {
        int databaseSizeBeforeCreate = shoppingListRepository.findAll().size();

        // Create the ShoppingList
        restShoppingListMockMvc.perform(post("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingList)))
            .andExpect(status().isCreated());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingList testShoppingList = shoppingListList.get(shoppingListList.size() - 1);
        assertThat(testShoppingList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShoppingList.getNotes()).isEqualTo(DEFAULT_NOTES);

        // Validate the ShoppingList in Elasticsearch
        ShoppingList shoppingListEs = shoppingListSearchRepository.findOne(testShoppingList.getId());
        assertThat(shoppingListEs).isEqualToComparingFieldByField(testShoppingList);
    }

    @Test
    @Transactional
    public void createShoppingListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shoppingListRepository.findAll().size();

        // Create the ShoppingList with an existing ID
        shoppingList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingListMockMvc.perform(post("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingList)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShoppingLists() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList
        restShoppingListMockMvc.perform(get("/api/shopping-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);

        // Get the shoppingList
        restShoppingListMockMvc.perform(get("/api/shopping-lists/{id}", shoppingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShoppingList() throws Exception {
        // Get the shoppingList
        restShoppingListMockMvc.perform(get("/api/shopping-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);
        shoppingListSearchRepository.save(shoppingList);
        int databaseSizeBeforeUpdate = shoppingListRepository.findAll().size();

        // Update the shoppingList
        ShoppingList updatedShoppingList = shoppingListRepository.findOne(shoppingList.getId());
        updatedShoppingList
            .name(UPDATED_NAME)
            .notes(UPDATED_NOTES);

        restShoppingListMockMvc.perform(put("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShoppingList)))
            .andExpect(status().isOk());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeUpdate);
        ShoppingList testShoppingList = shoppingListList.get(shoppingListList.size() - 1);
        assertThat(testShoppingList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShoppingList.getNotes()).isEqualTo(UPDATED_NOTES);

        // Validate the ShoppingList in Elasticsearch
        ShoppingList shoppingListEs = shoppingListSearchRepository.findOne(testShoppingList.getId());
        assertThat(shoppingListEs).isEqualToComparingFieldByField(testShoppingList);
    }

    @Test
    @Transactional
    public void updateNonExistingShoppingList() throws Exception {
        int databaseSizeBeforeUpdate = shoppingListRepository.findAll().size();

        // Create the ShoppingList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShoppingListMockMvc.perform(put("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingList)))
            .andExpect(status().isCreated());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);
        shoppingListSearchRepository.save(shoppingList);
        int databaseSizeBeforeDelete = shoppingListRepository.findAll().size();

        // Get the shoppingList
        restShoppingListMockMvc.perform(delete("/api/shopping-lists/{id}", shoppingList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean shoppingListExistsInEs = shoppingListSearchRepository.exists(shoppingList.getId());
        assertThat(shoppingListExistsInEs).isFalse();

        // Validate the database is empty
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);
        shoppingListSearchRepository.save(shoppingList);

        // Search the shoppingList
        restShoppingListMockMvc.perform(get("/api/_search/shopping-lists?query=id:" + shoppingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingList.class);
        ShoppingList shoppingList1 = new ShoppingList();
        shoppingList1.setId(1L);
        ShoppingList shoppingList2 = new ShoppingList();
        shoppingList2.setId(shoppingList1.getId());
        assertThat(shoppingList1).isEqualTo(shoppingList2);
        shoppingList2.setId(2L);
        assertThat(shoppingList1).isNotEqualTo(shoppingList2);
        shoppingList1.setId(null);
        assertThat(shoppingList1).isNotEqualTo(shoppingList2);
    }
}
