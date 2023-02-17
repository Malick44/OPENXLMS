package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Assessment;
import com.opnlms.app.repository.AssessmentRepository;
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
 * Integration tests for the {@link AssessmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AssessmentResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMBER_OF_QUESTIONS = 1;
    private static final Integer UPDATED_NUMBER_OF_QUESTIONS = 2;

    private static final Integer DEFAULT_TIME_LIMIT = 1;
    private static final Integer UPDATED_TIME_LIMIT = 2;

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String ENTITY_API_URL = "/api/assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Assessment assessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assessment createEntity(EntityManager em) {
        Assessment assessment = new Assessment()
            .userId(DEFAULT_USER_ID)
            .courseId(DEFAULT_COURSE_ID)
            .title(DEFAULT_TITLE)
            .sectionId(DEFAULT_SECTION_ID)
            .examDate(DEFAULT_EXAM_DATE)
            .numberOfQuestions(DEFAULT_NUMBER_OF_QUESTIONS)
            .timeLimit(DEFAULT_TIME_LIMIT)
            .score(DEFAULT_SCORE);
        return assessment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assessment createUpdatedEntity(EntityManager em) {
        Assessment assessment = new Assessment()
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .title(UPDATED_TITLE)
            .sectionId(UPDATED_SECTION_ID)
            .examDate(UPDATED_EXAM_DATE)
            .numberOfQuestions(UPDATED_NUMBER_OF_QUESTIONS)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);
        return assessment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Assessment.class).block();
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
        assessment = createEntity(em);
    }

    @Test
    void createAssessment() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().collectList().block().size();
        // Create the Assessment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAssessment.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testAssessment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAssessment.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testAssessment.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testAssessment.getNumberOfQuestions()).isEqualTo(DEFAULT_NUMBER_OF_QUESTIONS);
        assertThat(testAssessment.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testAssessment.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void createAssessmentWithExistingId() throws Exception {
        // Create the Assessment with an existing ID
        assessment.setId("existing_id");

        int databaseSizeBeforeCreate = assessmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().collectList().block().size();
        // set the field null
        assessment.setUserId(null);

        // Create the Assessment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCourseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().collectList().block().size();
        // set the field null
        assessment.setCourseId(null);

        // Create the Assessment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().collectList().block().size();
        // set the field null
        assessment.setExamDate(null);

        // Create the Assessment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAssessmentsAsStream() {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        List<Assessment> assessmentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Assessment.class)
            .getResponseBody()
            .filter(assessment::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(assessmentList).isNotNull();
        assertThat(assessmentList).hasSize(1);
        Assessment testAssessment = assessmentList.get(0);
        assertThat(testAssessment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAssessment.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testAssessment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAssessment.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testAssessment.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testAssessment.getNumberOfQuestions()).isEqualTo(DEFAULT_NUMBER_OF_QUESTIONS);
        assertThat(testAssessment.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testAssessment.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void getAllAssessments() {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        // Get all the assessmentList
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
            .value(hasItem(assessment.getId()))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].sectionId")
            .value(hasItem(DEFAULT_SECTION_ID))
            .jsonPath("$.[*].examDate")
            .value(hasItem(DEFAULT_EXAM_DATE.toString()))
            .jsonPath("$.[*].numberOfQuestions")
            .value(hasItem(DEFAULT_NUMBER_OF_QUESTIONS))
            .jsonPath("$.[*].timeLimit")
            .value(hasItem(DEFAULT_TIME_LIMIT))
            .jsonPath("$.[*].score")
            .value(hasItem(DEFAULT_SCORE));
    }

    @Test
    void getAssessment() {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        // Get the assessment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, assessment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(assessment.getId()))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.sectionId")
            .value(is(DEFAULT_SECTION_ID))
            .jsonPath("$.examDate")
            .value(is(DEFAULT_EXAM_DATE.toString()))
            .jsonPath("$.numberOfQuestions")
            .value(is(DEFAULT_NUMBER_OF_QUESTIONS))
            .jsonPath("$.timeLimit")
            .value(is(DEFAULT_TIME_LIMIT))
            .jsonPath("$.score")
            .value(is(DEFAULT_SCORE));
    }

    @Test
    void getNonExistingAssessment() {
        // Get the assessment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAssessment() throws Exception {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();

        // Update the assessment
        Assessment updatedAssessment = assessmentRepository.findById(assessment.getId()).block();
        updatedAssessment
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .title(UPDATED_TITLE)
            .sectionId(UPDATED_SECTION_ID)
            .examDate(UPDATED_EXAM_DATE)
            .numberOfQuestions(UPDATED_NUMBER_OF_QUESTIONS)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAssessment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAssessment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAssessment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testAssessment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssessment.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testAssessment.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testAssessment.getNumberOfQuestions()).isEqualTo(UPDATED_NUMBER_OF_QUESTIONS);
        assertThat(testAssessment.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testAssessment.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void putNonExistingAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();
        assessment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assessment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();
        assessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();
        assessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAssessmentWithPatch() throws Exception {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();

        // Update the assessment using partial update
        Assessment partialUpdatedAssessment = new Assessment();
        partialUpdatedAssessment.setId(assessment.getId());

        partialUpdatedAssessment
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .title(UPDATED_TITLE)
            .examDate(UPDATED_EXAM_DATE)
            .numberOfQuestions(UPDATED_NUMBER_OF_QUESTIONS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssessment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAssessment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testAssessment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssessment.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testAssessment.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testAssessment.getNumberOfQuestions()).isEqualTo(UPDATED_NUMBER_OF_QUESTIONS);
        assertThat(testAssessment.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testAssessment.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void fullUpdateAssessmentWithPatch() throws Exception {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();

        // Update the assessment using partial update
        Assessment partialUpdatedAssessment = new Assessment();
        partialUpdatedAssessment.setId(assessment.getId());

        partialUpdatedAssessment
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .title(UPDATED_TITLE)
            .sectionId(UPDATED_SECTION_ID)
            .examDate(UPDATED_EXAM_DATE)
            .numberOfQuestions(UPDATED_NUMBER_OF_QUESTIONS)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssessment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessmentList.get(assessmentList.size() - 1);
        assertThat(testAssessment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAssessment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testAssessment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssessment.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testAssessment.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testAssessment.getNumberOfQuestions()).isEqualTo(UPDATED_NUMBER_OF_QUESTIONS);
        assertThat(testAssessment.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testAssessment.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void patchNonExistingAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();
        assessment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, assessment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();
        assessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAssessment() throws Exception {
        int databaseSizeBeforeUpdate = assessmentRepository.findAll().collectList().block().size();
        assessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assessment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Assessment in the database
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAssessment() {
        // Initialize the database
        assessment.setId(UUID.randomUUID().toString());
        assessmentRepository.save(assessment).block();

        int databaseSizeBeforeDelete = assessmentRepository.findAll().collectList().block().size();

        // Delete the assessment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, assessment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Assessment> assessmentList = assessmentRepository.findAll().collectList().block();
        assertThat(assessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
