package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Instructor;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.InstructorRepository;
import java.time.Duration;
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
 * Integration tests for the {@link InstructorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstructorResourceIT {

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_URL = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_URL = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_THUMBNAIL = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_RATING = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_RATING = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_RATING_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_RATING_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_TOTAL_STUDENTS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_TOTAL_STUDENTS = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTOR_TOTAL_REVIEWS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR_TOTAL_REVIEWS = "BBBBBBBBBB";

    private static final String DEFAULT_RATING_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_RATING_COUNT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/instructors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Instructor instructor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor()
            .courseId(DEFAULT_COURSE_ID)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .instructorUrl(DEFAULT_INSTRUCTOR_URL)
            .instructorThumbnail(DEFAULT_INSTRUCTOR_THUMBNAIL)
            .instructorRating(DEFAULT_INSTRUCTOR_RATING)
            .instructorRatingCount(DEFAULT_INSTRUCTOR_RATING_COUNT)
            .instructorTotalStudents(DEFAULT_INSTRUCTOR_TOTAL_STUDENTS)
            .instructorTotalReviews(DEFAULT_INSTRUCTOR_TOTAL_REVIEWS)
            .ratingCount(DEFAULT_RATING_COUNT);
        return instructor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createUpdatedEntity(EntityManager em) {
        Instructor instructor = new Instructor()
            .courseId(UPDATED_COURSE_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .instructorUrl(UPDATED_INSTRUCTOR_URL)
            .instructorThumbnail(UPDATED_INSTRUCTOR_THUMBNAIL)
            .instructorRating(UPDATED_INSTRUCTOR_RATING)
            .instructorRatingCount(UPDATED_INSTRUCTOR_RATING_COUNT)
            .instructorTotalStudents(UPDATED_INSTRUCTOR_TOTAL_STUDENTS)
            .instructorTotalReviews(UPDATED_INSTRUCTOR_TOTAL_REVIEWS)
            .ratingCount(UPDATED_RATING_COUNT);
        return instructor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Instructor.class).block();
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
        instructor = createEntity(em);
    }

    @Test
    void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().collectList().block().size();
        // Create the Instructor
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInstructor.getInstructorUrl()).isEqualTo(DEFAULT_INSTRUCTOR_URL);
        assertThat(testInstructor.getInstructorThumbnail()).isEqualTo(DEFAULT_INSTRUCTOR_THUMBNAIL);
        assertThat(testInstructor.getInstructorRating()).isEqualTo(DEFAULT_INSTRUCTOR_RATING);
        assertThat(testInstructor.getInstructorRatingCount()).isEqualTo(DEFAULT_INSTRUCTOR_RATING_COUNT);
        assertThat(testInstructor.getInstructorTotalStudents()).isEqualTo(DEFAULT_INSTRUCTOR_TOTAL_STUDENTS);
        assertThat(testInstructor.getInstructorTotalReviews()).isEqualTo(DEFAULT_INSTRUCTOR_TOTAL_REVIEWS);
        assertThat(testInstructor.getRatingCount()).isEqualTo(DEFAULT_RATING_COUNT);
    }

    @Test
    void createInstructorWithExistingId() throws Exception {
        // Create the Instructor with an existing ID
        instructor.setId("existing_id");

        int databaseSizeBeforeCreate = instructorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInstructorsAsStream() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        List<Instructor> instructorList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Instructor.class)
            .getResponseBody()
            .filter(instructor::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(instructorList).isNotNull();
        assertThat(instructorList).hasSize(1);
        Instructor testInstructor = instructorList.get(0);
        assertThat(testInstructor.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInstructor.getInstructorUrl()).isEqualTo(DEFAULT_INSTRUCTOR_URL);
        assertThat(testInstructor.getInstructorThumbnail()).isEqualTo(DEFAULT_INSTRUCTOR_THUMBNAIL);
        assertThat(testInstructor.getInstructorRating()).isEqualTo(DEFAULT_INSTRUCTOR_RATING);
        assertThat(testInstructor.getInstructorRatingCount()).isEqualTo(DEFAULT_INSTRUCTOR_RATING_COUNT);
        assertThat(testInstructor.getInstructorTotalStudents()).isEqualTo(DEFAULT_INSTRUCTOR_TOTAL_STUDENTS);
        assertThat(testInstructor.getInstructorTotalReviews()).isEqualTo(DEFAULT_INSTRUCTOR_TOTAL_REVIEWS);
        assertThat(testInstructor.getRatingCount()).isEqualTo(DEFAULT_RATING_COUNT);
    }

    @Test
    void getAllInstructors() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        // Get all the instructorList
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
            .value(hasItem(instructor.getId()))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].instructorUrl")
            .value(hasItem(DEFAULT_INSTRUCTOR_URL))
            .jsonPath("$.[*].instructorThumbnail")
            .value(hasItem(DEFAULT_INSTRUCTOR_THUMBNAIL))
            .jsonPath("$.[*].instructorRating")
            .value(hasItem(DEFAULT_INSTRUCTOR_RATING))
            .jsonPath("$.[*].instructorRatingCount")
            .value(hasItem(DEFAULT_INSTRUCTOR_RATING_COUNT))
            .jsonPath("$.[*].instructorTotalStudents")
            .value(hasItem(DEFAULT_INSTRUCTOR_TOTAL_STUDENTS))
            .jsonPath("$.[*].instructorTotalReviews")
            .value(hasItem(DEFAULT_INSTRUCTOR_TOTAL_REVIEWS))
            .jsonPath("$.[*].ratingCount")
            .value(hasItem(DEFAULT_RATING_COUNT));
    }

    @Test
    void getInstructor() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        // Get the instructor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(instructor.getId()))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.instructorUrl")
            .value(is(DEFAULT_INSTRUCTOR_URL))
            .jsonPath("$.instructorThumbnail")
            .value(is(DEFAULT_INSTRUCTOR_THUMBNAIL))
            .jsonPath("$.instructorRating")
            .value(is(DEFAULT_INSTRUCTOR_RATING))
            .jsonPath("$.instructorRatingCount")
            .value(is(DEFAULT_INSTRUCTOR_RATING_COUNT))
            .jsonPath("$.instructorTotalStudents")
            .value(is(DEFAULT_INSTRUCTOR_TOTAL_STUDENTS))
            .jsonPath("$.instructorTotalReviews")
            .value(is(DEFAULT_INSTRUCTOR_TOTAL_REVIEWS))
            .jsonPath("$.ratingCount")
            .value(is(DEFAULT_RATING_COUNT));
    }

    @Test
    void getNonExistingInstructor() {
        // Get the instructor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstructor() throws Exception {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findById(instructor.getId()).block();
        updatedInstructor
            .courseId(UPDATED_COURSE_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .instructorUrl(UPDATED_INSTRUCTOR_URL)
            .instructorThumbnail(UPDATED_INSTRUCTOR_THUMBNAIL)
            .instructorRating(UPDATED_INSTRUCTOR_RATING)
            .instructorRatingCount(UPDATED_INSTRUCTOR_RATING_COUNT)
            .instructorTotalStudents(UPDATED_INSTRUCTOR_TOTAL_STUDENTS)
            .instructorTotalReviews(UPDATED_INSTRUCTOR_TOTAL_REVIEWS)
            .ratingCount(UPDATED_RATING_COUNT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedInstructor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedInstructor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInstructor.getInstructorUrl()).isEqualTo(UPDATED_INSTRUCTOR_URL);
        assertThat(testInstructor.getInstructorThumbnail()).isEqualTo(UPDATED_INSTRUCTOR_THUMBNAIL);
        assertThat(testInstructor.getInstructorRating()).isEqualTo(UPDATED_INSTRUCTOR_RATING);
        assertThat(testInstructor.getInstructorRatingCount()).isEqualTo(UPDATED_INSTRUCTOR_RATING_COUNT);
        assertThat(testInstructor.getInstructorTotalStudents()).isEqualTo(UPDATED_INSTRUCTOR_TOTAL_STUDENTS);
        assertThat(testInstructor.getInstructorTotalReviews()).isEqualTo(UPDATED_INSTRUCTOR_TOTAL_REVIEWS);
        assertThat(testInstructor.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
    }

    @Test
    void putNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstructorWithPatch() throws Exception {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();

        // Update the instructor using partial update
        Instructor partialUpdatedInstructor = new Instructor();
        partialUpdatedInstructor.setId(instructor.getId());

        partialUpdatedInstructor
            .name(UPDATED_NAME)
            .instructorUrl(UPDATED_INSTRUCTOR_URL)
            .instructorTotalReviews(UPDATED_INSTRUCTOR_TOTAL_REVIEWS)
            .ratingCount(UPDATED_RATING_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstructor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstructor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInstructor.getInstructorUrl()).isEqualTo(UPDATED_INSTRUCTOR_URL);
        assertThat(testInstructor.getInstructorThumbnail()).isEqualTo(DEFAULT_INSTRUCTOR_THUMBNAIL);
        assertThat(testInstructor.getInstructorRating()).isEqualTo(DEFAULT_INSTRUCTOR_RATING);
        assertThat(testInstructor.getInstructorRatingCount()).isEqualTo(DEFAULT_INSTRUCTOR_RATING_COUNT);
        assertThat(testInstructor.getInstructorTotalStudents()).isEqualTo(DEFAULT_INSTRUCTOR_TOTAL_STUDENTS);
        assertThat(testInstructor.getInstructorTotalReviews()).isEqualTo(UPDATED_INSTRUCTOR_TOTAL_REVIEWS);
        assertThat(testInstructor.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
    }

    @Test
    void fullUpdateInstructorWithPatch() throws Exception {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();

        // Update the instructor using partial update
        Instructor partialUpdatedInstructor = new Instructor();
        partialUpdatedInstructor.setId(instructor.getId());

        partialUpdatedInstructor
            .courseId(UPDATED_COURSE_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .instructorUrl(UPDATED_INSTRUCTOR_URL)
            .instructorThumbnail(UPDATED_INSTRUCTOR_THUMBNAIL)
            .instructorRating(UPDATED_INSTRUCTOR_RATING)
            .instructorRatingCount(UPDATED_INSTRUCTOR_RATING_COUNT)
            .instructorTotalStudents(UPDATED_INSTRUCTOR_TOTAL_STUDENTS)
            .instructorTotalReviews(UPDATED_INSTRUCTOR_TOTAL_REVIEWS)
            .ratingCount(UPDATED_RATING_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstructor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstructor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInstructor.getInstructorUrl()).isEqualTo(UPDATED_INSTRUCTOR_URL);
        assertThat(testInstructor.getInstructorThumbnail()).isEqualTo(UPDATED_INSTRUCTOR_THUMBNAIL);
        assertThat(testInstructor.getInstructorRating()).isEqualTo(UPDATED_INSTRUCTOR_RATING);
        assertThat(testInstructor.getInstructorRatingCount()).isEqualTo(UPDATED_INSTRUCTOR_RATING_COUNT);
        assertThat(testInstructor.getInstructorTotalStudents()).isEqualTo(UPDATED_INSTRUCTOR_TOTAL_STUDENTS);
        assertThat(testInstructor.getInstructorTotalReviews()).isEqualTo(UPDATED_INSTRUCTOR_TOTAL_REVIEWS);
        assertThat(testInstructor.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
    }

    @Test
    void patchNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstructor() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeDelete = instructorRepository.findAll().collectList().block().size();

        // Delete the instructor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
