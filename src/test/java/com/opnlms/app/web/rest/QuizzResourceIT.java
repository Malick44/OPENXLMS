package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Quizz;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.QuizzRepository;
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
 * Integration tests for the {@link QuizzResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QuizzResourceIT {

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

    private static final String ENTITY_API_URL = "/api/quizzes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Quizz quizz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quizz createEntity(EntityManager em) {
        Quizz quizz = new Quizz()
            .userId(DEFAULT_USER_ID)
            .courseId(DEFAULT_COURSE_ID)
            .title(DEFAULT_TITLE)
            .sectionId(DEFAULT_SECTION_ID)
            .examDate(DEFAULT_EXAM_DATE)
            .numberOfQuestions(DEFAULT_NUMBER_OF_QUESTIONS)
            .timeLimit(DEFAULT_TIME_LIMIT)
            .score(DEFAULT_SCORE);
        return quizz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quizz createUpdatedEntity(EntityManager em) {
        Quizz quizz = new Quizz()
            .userId(UPDATED_USER_ID)
            .courseId(UPDATED_COURSE_ID)
            .title(UPDATED_TITLE)
            .sectionId(UPDATED_SECTION_ID)
            .examDate(UPDATED_EXAM_DATE)
            .numberOfQuestions(UPDATED_NUMBER_OF_QUESTIONS)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);
        return quizz;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Quizz.class).block();
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
        quizz = createEntity(em);
    }

    @Test
    void createQuizz() throws Exception {
        int databaseSizeBeforeCreate = quizzRepository.findAll().collectList().block().size();
        // Create the Quizz
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeCreate + 1);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testQuizz.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testQuizz.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuizz.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testQuizz.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testQuizz.getNumberOfQuestions()).isEqualTo(DEFAULT_NUMBER_OF_QUESTIONS);
        assertThat(testQuizz.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testQuizz.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void createQuizzWithExistingId() throws Exception {
        // Create the Quizz with an existing ID
        quizz.setId("existing_id");

        int databaseSizeBeforeCreate = quizzRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().collectList().block().size();
        // set the field null
        quizz.setUserId(null);

        // Create the Quizz, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCourseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().collectList().block().size();
        // set the field null
        quizz.setCourseId(null);

        // Create the Quizz, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().collectList().block().size();
        // set the field null
        quizz.setExamDate(null);

        // Create the Quizz, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuizzesAsStream() {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        List<Quizz> quizzList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Quizz.class)
            .getResponseBody()
            .filter(quizz::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(quizzList).isNotNull();
        assertThat(quizzList).hasSize(1);
        Quizz testQuizz = quizzList.get(0);
        assertThat(testQuizz.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testQuizz.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testQuizz.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuizz.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testQuizz.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testQuizz.getNumberOfQuestions()).isEqualTo(DEFAULT_NUMBER_OF_QUESTIONS);
        assertThat(testQuizz.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testQuizz.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void getAllQuizzes() {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        // Get all the quizzList
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
            .value(hasItem(quizz.getId()))
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
    void getQuizz() {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        // Get the quizz
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, quizz.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(quizz.getId()))
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
    void getNonExistingQuizz() {
        // Get the quizz
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQuizz() throws Exception {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();

        // Update the quizz
        Quizz updatedQuizz = quizzRepository.findById(quizz.getId()).block();
        updatedQuizz
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
            .uri(ENTITY_API_URL_ID, updatedQuizz.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedQuizz))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testQuizz.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testQuizz.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizz.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testQuizz.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testQuizz.getNumberOfQuestions()).isEqualTo(UPDATED_NUMBER_OF_QUESTIONS);
        assertThat(testQuizz.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testQuizz.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void putNonExistingQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();
        quizz.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, quizz.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();
        quizz.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();
        quizz.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuizzWithPatch() throws Exception {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();

        // Update the quizz using partial update
        Quizz partialUpdatedQuizz = new Quizz();
        partialUpdatedQuizz.setId(quizz.getId());

        partialUpdatedQuizz.title(UPDATED_TITLE).sectionId(UPDATED_SECTION_ID).timeLimit(UPDATED_TIME_LIMIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuizz.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizz))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testQuizz.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testQuizz.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizz.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testQuizz.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testQuizz.getNumberOfQuestions()).isEqualTo(DEFAULT_NUMBER_OF_QUESTIONS);
        assertThat(testQuizz.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testQuizz.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void fullUpdateQuizzWithPatch() throws Exception {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();

        // Update the quizz using partial update
        Quizz partialUpdatedQuizz = new Quizz();
        partialUpdatedQuizz.setId(quizz.getId());

        partialUpdatedQuizz
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
            .uri(ENTITY_API_URL_ID, partialUpdatedQuizz.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizz))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testQuizz.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testQuizz.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizz.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testQuizz.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testQuizz.getNumberOfQuestions()).isEqualTo(UPDATED_NUMBER_OF_QUESTIONS);
        assertThat(testQuizz.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testQuizz.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void patchNonExistingQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();
        quizz.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, quizz.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();
        quizz.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().collectList().block().size();
        quizz.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(quizz))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuizz() {
        // Initialize the database
        quizz.setId(UUID.randomUUID().toString());
        quizzRepository.save(quizz).block();

        int databaseSizeBeforeDelete = quizzRepository.findAll().collectList().block().size();

        // Delete the quizz
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, quizz.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Quizz> quizzList = quizzRepository.findAll().collectList().block();
        assertThat(quizzList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
