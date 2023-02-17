package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Rating;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.RatingRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link RatingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RatingResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRACTOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSTRACTOR_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ratings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Rating rating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {
        Rating rating = new Rating()
            .userId(DEFAULT_USER_ID)
            .courseId(DEFAULT_COURSE_ID)
            .instractorId(DEFAULT_INSTRACTOR_ID)
            .value(DEFAULT_VALUE)
            .comment(DEFAULT_COMMENT)
            .timestamp(DEFAULT_TIMESTAMP);
        return rating;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createUpdatedEntity(EntityManager em) {
        Rating rating = new Rating()
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .instractorId(UPDATED_INSTRACTOR_ID)
            .value(UPDATED_VALUE)
            .comment(UPDATED_COMMENT)
            .timestamp(UPDATED_TIMESTAMP);
        return rating;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Rating.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        rating = createEntity(em);
    }

    @Test
    void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().collectList().block().size();
        // Create the Rating
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRating.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testRating.getInstractorId()).isEqualTo(DEFAULT_INSTRACTOR_ID);
        assertThat(testRating.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRating.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRating.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    void createRatingWithExistingId() throws Exception {
        // Create the Rating with an existing ID
        rating.setId("existing_id");

        int databaseSizeBeforeCreate = ratingRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRatingsAsStream() {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        List<Rating> ratingList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Rating.class)
            .getResponseBody()
            .filter(rating::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(ratingList).isNotNull();
        assertThat(ratingList).hasSize(1);
        Rating testRating = ratingList.get(0);
        assertThat(testRating.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRating.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testRating.getInstractorId()).isEqualTo(DEFAULT_INSTRACTOR_ID);
        assertThat(testRating.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRating.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRating.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    void getAllRatings() {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        // Get all the ratingList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(rating.getId()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].instractorId")
            .value(hasItem(DEFAULT_INSTRACTOR_ID))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE))
            .jsonPath("$.[*].comment")
            .value(hasItem(DEFAULT_COMMENT))
            .jsonPath("$.[*].timestamp")
            .value(hasItem(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    void getRating() {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        // Get the rating
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, rating.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(rating.getId()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.instractorId")
            .value(is(DEFAULT_INSTRACTOR_ID))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE))
            .jsonPath("$.comment")
            .value(is(DEFAULT_COMMENT))
            .jsonPath("$.timestamp")
            .value(is(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    void getNonExistingRating() {
        // Get the rating
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRating() throws Exception {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).block();
        updatedRating
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .instractorId(UPDATED_INSTRACTOR_ID)
            .value(UPDATED_VALUE)
            .comment(UPDATED_COMMENT)
            .timestamp(UPDATED_TIMESTAMP);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRating.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRating))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRating.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testRating.getInstractorId()).isEqualTo(UPDATED_INSTRACTOR_ID);
        assertThat(testRating.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRating.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    void putNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();
        rating.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, rating.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();
        rating.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();
        rating.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating.userId(UPDATED_USER_ID).courseId(UPDATED_COURSE_ID).comment(UPDATED_COMMENT).timestamp(UPDATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRating.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRating))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRating.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testRating.getInstractorId()).isEqualTo(DEFAULT_INSTRACTOR_ID);
        assertThat(testRating.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRating.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    void fullUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .instractorId(UPDATED_INSTRACTOR_ID)
            .value(UPDATED_VALUE)
            .comment(UPDATED_COMMENT)
            .timestamp(UPDATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRating.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRating))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRating.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testRating.getInstractorId()).isEqualTo(UPDATED_INSTRACTOR_ID);
        assertThat(testRating.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testRating.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRating.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    void patchNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();
        rating.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, rating.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();
        rating.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().collectList().block().size();
        rating.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(rating))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRating() {
        // Initialize the database
        rating.setId(UUID.randomUUID().toString());
        ratingRepository.save(rating).block();

        int databaseSizeBeforeDelete = ratingRepository.findAll().collectList().block().size();

        // Delete the rating
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, rating.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Rating> ratingList = ratingRepository.findAll().collectList().block();
        assertThat(ratingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
