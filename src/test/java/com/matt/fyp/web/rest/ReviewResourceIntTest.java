package com.matt.fyp.web.rest;

import com.matt.fyp.FyPv6App;

import com.matt.fyp.domain.Review;
import com.matt.fyp.repository.ReviewRepository;
import com.matt.fyp.repository.search.ReviewSearchRepository;
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
 * Test class for the ReviewResource REST controller.
 *
 * @see ReviewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FyPv6App.class)
public class ReviewResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 5;
    private static final Integer UPDATED_RATING = 4;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewSearchRepository reviewSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReviewMockMvc;

    private Review review;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReviewResource reviewResource = new ReviewResource(reviewRepository, reviewSearchRepository);
        this.restReviewMockMvc = MockMvcBuilders.standaloneSetup(reviewResource)
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
    public static Review createEntity(EntityManager em) {
        Review review = new Review()
            .title(DEFAULT_TITLE)
            .rating(DEFAULT_RATING)
            .message(DEFAULT_MESSAGE);
        return review;
    }

    @Before
    public void initTest() {
        reviewSearchRepository.deleteAll();
        review = createEntity(em);
    }

    @Test
    @Transactional
    public void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review
        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testReview.getMessage()).isEqualTo(DEFAULT_MESSAGE);

        // Validate the Review in Elasticsearch
        Review reviewEs = reviewSearchRepository.findOne(testReview.getId());
        assertThat(reviewEs).isEqualToComparingFieldByField(testReview);
    }

    @Test
    @Transactional
    public void createReviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review with an existing ID
        review.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        reviewSearchRepository.save(review);
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findOne(review.getId());
        updatedReview
            .title(UPDATED_TITLE)
            .rating(UPDATED_RATING)
            .message(UPDATED_MESSAGE);

        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReview)))
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReview.getMessage()).isEqualTo(UPDATED_MESSAGE);

        // Validate the Review in Elasticsearch
        Review reviewEs = reviewSearchRepository.findOne(testReview.getId());
        assertThat(reviewEs).isEqualToComparingFieldByField(testReview);
    }

    @Test
    @Transactional
    public void updateNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Create the Review

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        reviewSearchRepository.save(review);
        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Get the review
        restReviewMockMvc.perform(delete("/api/reviews/{id}", review.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reviewExistsInEs = reviewSearchRepository.exists(review.getId());
        assertThat(reviewExistsInEs).isFalse();

        // Validate the database is empty
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        reviewSearchRepository.save(review);

        // Search the review
        restReviewMockMvc.perform(get("/api/_search/reviews?query=id:" + review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = new Review();
        review1.setId(1L);
        Review review2 = new Review();
        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);
        review2.setId(2L);
        assertThat(review1).isNotEqualTo(review2);
        review1.setId(null);
        assertThat(review1).isNotEqualTo(review2);
    }
}
