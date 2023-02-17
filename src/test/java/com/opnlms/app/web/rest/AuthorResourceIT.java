package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Author;
import com.opnlms.app.repository.AuthorRepository;
import com.opnlms.app.repository.EntityManager;
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
 * Integration tests for the {@link AuthorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AuthorResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_AVATAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESET_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESET_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESET_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHORITIES = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORITIES = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Author author;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Author createEntity(EntityManager em) {
        Author author = new Author()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .createDate(DEFAULT_CREATE_DATE)
            .avatarUrl(DEFAULT_AVATAR_URL)
            .activated(DEFAULT_ACTIVATED)
            .langKey(DEFAULT_LANG_KEY)
            .resetDate(DEFAULT_RESET_DATE)
            .resetKey(DEFAULT_RESET_KEY)
            .authorities(DEFAULT_AUTHORITIES)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .courseId(DEFAULT_COURSE_ID);
        return author;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Author createUpdatedEntity(EntityManager em) {
        Author author = new Author()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createDate(UPDATED_CREATE_DATE)
            .avatarUrl(UPDATED_AVATAR_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .resetKey(UPDATED_RESET_KEY)
            .authorities(UPDATED_AUTHORITIES)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .courseId(UPDATED_COURSE_ID);
        return author;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Author.class).block();
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
        author = createEntity(em);
    }

    @Test
    void createAuthor() throws Exception {
        int databaseSizeBeforeCreate = authorRepository.findAll().collectList().block().size();
        // Create the Author
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeCreate + 1);
        Author testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAuthor.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAuthor.getAvatarUrl()).isEqualTo(DEFAULT_AVATAR_URL);
        assertThat(testAuthor.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testAuthor.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testAuthor.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testAuthor.getResetKey()).isEqualTo(DEFAULT_RESET_KEY);
        assertThat(testAuthor.getAuthorities()).isEqualTo(DEFAULT_AUTHORITIES);
        assertThat(testAuthor.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAuthor.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAuthor.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAuthor.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
    }

    @Test
    void createAuthorWithExistingId() throws Exception {
        // Create the Author with an existing ID
        author.setId("existing_id");

        int databaseSizeBeforeCreate = authorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().collectList().block().size();
        // set the field null
        author.setFirstName(null);

        // Create the Author, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().collectList().block().size();
        // set the field null
        author.setPassword(null);

        // Create the Author, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().collectList().block().size();
        // set the field null
        author.setCreateDate(null);

        // Create the Author, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().collectList().block().size();
        // set the field null
        author.setActivated(null);

        // Create the Author, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAuthoritiesIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().collectList().block().size();
        // set the field null
        author.setAuthorities(null);

        // Create the Author, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAuthorsAsStream() {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        List<Author> authorList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Author.class)
            .getResponseBody()
            .filter(author::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(authorList).isNotNull();
        assertThat(authorList).hasSize(1);
        Author testAuthor = authorList.get(0);
        assertThat(testAuthor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAuthor.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAuthor.getAvatarUrl()).isEqualTo(DEFAULT_AVATAR_URL);
        assertThat(testAuthor.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testAuthor.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testAuthor.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testAuthor.getResetKey()).isEqualTo(DEFAULT_RESET_KEY);
        assertThat(testAuthor.getAuthorities()).isEqualTo(DEFAULT_AUTHORITIES);
        assertThat(testAuthor.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAuthor.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAuthor.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAuthor.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
    }

    @Test
    void getAllAuthors() {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        // Get all the authorList
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
            .value(hasItem(author.getId()))
            .jsonPath("$.[*].firstName")
            .value(hasItem(DEFAULT_FIRST_NAME))
            .jsonPath("$.[*].lastName")
            .value(hasItem(DEFAULT_LAST_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD))
            .jsonPath("$.[*].createDate")
            .value(hasItem(DEFAULT_CREATE_DATE.toString()))
            .jsonPath("$.[*].avatarUrl")
            .value(hasItem(DEFAULT_AVATAR_URL))
            .jsonPath("$.[*].activated")
            .value(hasItem(DEFAULT_ACTIVATED.booleanValue()))
            .jsonPath("$.[*].langKey")
            .value(hasItem(DEFAULT_LANG_KEY))
            .jsonPath("$.[*].resetDate")
            .value(hasItem(DEFAULT_RESET_DATE.toString()))
            .jsonPath("$.[*].resetKey")
            .value(hasItem(DEFAULT_RESET_KEY))
            .jsonPath("$.[*].authorities")
            .value(hasItem(DEFAULT_AUTHORITIES))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID));
    }

    @Test
    void getAuthor() {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        // Get the author
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, author.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(author.getId()))
            .jsonPath("$.firstName")
            .value(is(DEFAULT_FIRST_NAME))
            .jsonPath("$.lastName")
            .value(is(DEFAULT_LAST_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD))
            .jsonPath("$.createDate")
            .value(is(DEFAULT_CREATE_DATE.toString()))
            .jsonPath("$.avatarUrl")
            .value(is(DEFAULT_AVATAR_URL))
            .jsonPath("$.activated")
            .value(is(DEFAULT_ACTIVATED.booleanValue()))
            .jsonPath("$.langKey")
            .value(is(DEFAULT_LANG_KEY))
            .jsonPath("$.resetDate")
            .value(is(DEFAULT_RESET_DATE.toString()))
            .jsonPath("$.resetKey")
            .value(is(DEFAULT_RESET_KEY))
            .jsonPath("$.authorities")
            .value(is(DEFAULT_AUTHORITIES))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID));
    }

    @Test
    void getNonExistingAuthor() {
        // Get the author
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAuthor() throws Exception {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();

        // Update the author
        Author updatedAuthor = authorRepository.findById(author.getId()).block();
        updatedAuthor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createDate(UPDATED_CREATE_DATE)
            .avatarUrl(UPDATED_AVATAR_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .resetKey(UPDATED_RESET_KEY)
            .authorities(UPDATED_AUTHORITIES)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .courseId(UPDATED_COURSE_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAuthor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAuthor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        Author testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAuthor.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testAuthor.getAvatarUrl()).isEqualTo(UPDATED_AVATAR_URL);
        assertThat(testAuthor.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testAuthor.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testAuthor.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testAuthor.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testAuthor.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testAuthor.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAuthor.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAuthor.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAuthor.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
    }

    @Test
    void putNonExistingAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();
        author.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, author.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();
        author.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();
        author.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAuthorWithPatch() throws Exception {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();

        // Update the author using partial update
        Author partialUpdatedAuthor = new Author();
        partialUpdatedAuthor.setId(author.getId());

        partialUpdatedAuthor
            .lastName(UPDATED_LAST_NAME)
            .activated(UPDATED_ACTIVATED)
            .resetDate(UPDATED_RESET_DATE)
            .resetKey(UPDATED_RESET_KEY)
            .authorities(UPDATED_AUTHORITIES)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAuthor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        Author testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAuthor.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAuthor.getAvatarUrl()).isEqualTo(DEFAULT_AVATAR_URL);
        assertThat(testAuthor.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testAuthor.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testAuthor.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testAuthor.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testAuthor.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testAuthor.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAuthor.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAuthor.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAuthor.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
    }

    @Test
    void fullUpdateAuthorWithPatch() throws Exception {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();

        // Update the author using partial update
        Author partialUpdatedAuthor = new Author();
        partialUpdatedAuthor.setId(author.getId());

        partialUpdatedAuthor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createDate(UPDATED_CREATE_DATE)
            .avatarUrl(UPDATED_AVATAR_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .resetDate(UPDATED_RESET_DATE)
            .resetKey(UPDATED_RESET_KEY)
            .authorities(UPDATED_AUTHORITIES)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .courseId(UPDATED_COURSE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAuthor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        Author testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAuthor.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testAuthor.getAvatarUrl()).isEqualTo(UPDATED_AVATAR_URL);
        assertThat(testAuthor.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testAuthor.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testAuthor.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testAuthor.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testAuthor.getAuthorities()).isEqualTo(UPDATED_AUTHORITIES);
        assertThat(testAuthor.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAuthor.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAuthor.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAuthor.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
    }

    @Test
    void patchNonExistingAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();
        author.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, author.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();
        author.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().collectList().block().size();
        author.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(author))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAuthor() {
        // Initialize the database
        author.setId(UUID.randomUUID().toString());
        authorRepository.save(author).block();

        int databaseSizeBeforeDelete = authorRepository.findAll().collectList().block().size();

        // Delete the author
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, author.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Author> authorList = authorRepository.findAll().collectList().block();
        assertThat(authorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
