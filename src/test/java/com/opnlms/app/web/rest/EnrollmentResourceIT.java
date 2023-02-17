package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Enrollment;
import com.opnlms.app.repository.EnrollmentRepository;
import com.opnlms.app.repository.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EnrollmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EnrollmentResourceIT {

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ENROLLED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENROLLED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_COMPLETION_RATE = 1;
    private static final Integer UPDATED_COMPLETION_RATE = 2;

    private static final LocalDate DEFAULT_COMPLETED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMPLETED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/enrollments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Enrollment enrollment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment()
            .courseId(DEFAULT_COURSE_ID)
            .userId(DEFAULT_USER_ID)
            .enrolledDate(DEFAULT_ENROLLED_DATE)
            .completionRate(DEFAULT_COMPLETION_RATE)
            .completedDate(DEFAULT_COMPLETED_DATE);
        return enrollment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createUpdatedEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment()
            .courseId(UPDATED_COURSE_ID)
            .userId(UPDATED_USER_ID)
            .enrolledDate(UPDATED_ENROLLED_DATE)
            .completionRate(UPDATED_COMPLETION_RATE)
            .completedDate(UPDATED_COMPLETED_DATE);
        return enrollment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Enrollment.class).block();
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
        enrollment = createEntity(em);
    }

    @Test
    void createEnrollment() throws Exception {
        int databaseSizeBeforeCreate = enrollmentRepository.findAll().collectList().block().size();
        // Create the Enrollment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeCreate + 1);
        Enrollment testEnrollment = enrollmentList.get(enrollmentList.size() - 1);
        assertThat(testEnrollment.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testEnrollment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testEnrollment.getEnrolledDate()).isEqualTo(DEFAULT_ENROLLED_DATE);
        assertThat(testEnrollment.getCompletionRate()).isEqualTo(DEFAULT_COMPLETION_RATE);
        assertThat(testEnrollment.getCompletedDate()).isEqualTo(DEFAULT_COMPLETED_DATE);
    }

    @Test
    void createEnrollmentWithExistingId() throws Exception {
        // Create the Enrollment with an existing ID
        enrollment.setId("existing_id");

        int databaseSizeBeforeCreate = enrollmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCourseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = enrollmentRepository.findAll().collectList().block().size();
        // set the field null
        enrollment.setCourseId(null);

        // Create the Enrollment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = enrollmentRepository.findAll().collectList().block().size();
        // set the field null
        enrollment.setUserId(null);

        // Create the Enrollment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEnrolledDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = enrollmentRepository.findAll().collectList().block().size();
        // set the field null
        enrollment.setEnrolledDate(null);

        // Create the Enrollment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEnrollmentsAsStream() {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        List<Enrollment> enrollmentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Enrollment.class)
            .getResponseBody()
            .filter(enrollment::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(enrollmentList).isNotNull();
        assertThat(enrollmentList).hasSize(1);
        Enrollment testEnrollment = enrollmentList.get(0);
        assertThat(testEnrollment.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testEnrollment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testEnrollment.getEnrolledDate()).isEqualTo(DEFAULT_ENROLLED_DATE);
        assertThat(testEnrollment.getCompletionRate()).isEqualTo(DEFAULT_COMPLETION_RATE);
        assertThat(testEnrollment.getCompletedDate()).isEqualTo(DEFAULT_COMPLETED_DATE);
    }

    @Test
    void getAllEnrollments() {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        // Get all the enrollmentList
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
            .value(hasItem(enrollment.getId()))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].enrolledDate")
            .value(hasItem(DEFAULT_ENROLLED_DATE.toString()))
            .jsonPath("$.[*].completionRate")
            .value(hasItem(DEFAULT_COMPLETION_RATE))
            .jsonPath("$.[*].completedDate")
            .value(hasItem(DEFAULT_COMPLETED_DATE.toString()));
    }

    @Test
    void getEnrollment() {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        // Get the enrollment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, enrollment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(enrollment.getId()))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.enrolledDate")
            .value(is(DEFAULT_ENROLLED_DATE.toString()))
            .jsonPath("$.completionRate")
            .value(is(DEFAULT_COMPLETION_RATE))
            .jsonPath("$.completedDate")
            .value(is(DEFAULT_COMPLETED_DATE.toString()));
    }

    @Test
    void getNonExistingEnrollment() {
        // Get the enrollment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEnrollment() throws Exception {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();

        // Update the enrollment
        Enrollment updatedEnrollment = enrollmentRepository.findById(enrollment.getId()).block();
        updatedEnrollment
            .courseId(UPDATED_COURSE_ID)
            .userId(UPDATED_USER_ID)
            .enrolledDate(UPDATED_ENROLLED_DATE)
            .completionRate(UPDATED_COMPLETION_RATE)
            .completedDate(UPDATED_COMPLETED_DATE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEnrollment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEnrollment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
        Enrollment testEnrollment = enrollmentList.get(enrollmentList.size() - 1);
        assertThat(testEnrollment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testEnrollment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEnrollment.getEnrolledDate()).isEqualTo(UPDATED_ENROLLED_DATE);
        assertThat(testEnrollment.getCompletionRate()).isEqualTo(UPDATED_COMPLETION_RATE);
        assertThat(testEnrollment.getCompletedDate()).isEqualTo(UPDATED_COMPLETED_DATE);
    }

    @Test
    void putNonExistingEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();
        enrollment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, enrollment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();
        enrollment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();
        enrollment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnrollmentWithPatch() throws Exception {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();

        // Update the enrollment using partial update
        Enrollment partialUpdatedEnrollment = new Enrollment();
        partialUpdatedEnrollment.setId(enrollment.getId());

        partialUpdatedEnrollment
            .courseId(UPDATED_COURSE_ID)
            .userId(UPDATED_USER_ID)
            .enrolledDate(UPDATED_ENROLLED_DATE)
            .completionRate(UPDATED_COMPLETION_RATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEnrollment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEnrollment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
        Enrollment testEnrollment = enrollmentList.get(enrollmentList.size() - 1);
        assertThat(testEnrollment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testEnrollment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEnrollment.getEnrolledDate()).isEqualTo(UPDATED_ENROLLED_DATE);
        assertThat(testEnrollment.getCompletionRate()).isEqualTo(UPDATED_COMPLETION_RATE);
        assertThat(testEnrollment.getCompletedDate()).isEqualTo(DEFAULT_COMPLETED_DATE);
    }

    @Test
    void fullUpdateEnrollmentWithPatch() throws Exception {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();

        // Update the enrollment using partial update
        Enrollment partialUpdatedEnrollment = new Enrollment();
        partialUpdatedEnrollment.setId(enrollment.getId());

        partialUpdatedEnrollment
            .courseId(UPDATED_COURSE_ID)
            .userId(UPDATED_USER_ID)
            .enrolledDate(UPDATED_ENROLLED_DATE)
            .completionRate(UPDATED_COMPLETION_RATE)
            .completedDate(UPDATED_COMPLETED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEnrollment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEnrollment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
        Enrollment testEnrollment = enrollmentList.get(enrollmentList.size() - 1);
        assertThat(testEnrollment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testEnrollment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEnrollment.getEnrolledDate()).isEqualTo(UPDATED_ENROLLED_DATE);
        assertThat(testEnrollment.getCompletionRate()).isEqualTo(UPDATED_COMPLETION_RATE);
        assertThat(testEnrollment.getCompletedDate()).isEqualTo(UPDATED_COMPLETED_DATE);
    }

    @Test
    void patchNonExistingEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();
        enrollment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, enrollment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();
        enrollment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().collectList().block().size();
        enrollment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(enrollment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEnrollment() {
        // Initialize the database
        enrollment.setId(UUID.randomUUID().toString());
        enrollmentRepository.save(enrollment).block();

        int databaseSizeBeforeDelete = enrollmentRepository.findAll().collectList().block().size();

        // Delete the enrollment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, enrollment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Enrollment> enrollmentList = enrollmentRepository.findAll().collectList().block();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
