package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Organisation;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.OrganisationRepository;
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
 * Integration tests for the {@link OrganisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OrganisationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Organisation organisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createEntity(EntityManager em) {
        Organisation organisation = new Organisation().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return organisation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createUpdatedEntity(EntityManager em) {
        Organisation organisation = new Organisation().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return organisation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Organisation.class).block();
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
        organisation = createEntity(em);
    }

    @Test
    void createOrganisation() throws Exception {
        int databaseSizeBeforeCreate = organisationRepository.findAll().collectList().block().size();
        // Create the Organisation
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeCreate + 1);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createOrganisationWithExistingId() throws Exception {
        // Create the Organisation with an existing ID
        organisation.setId("existing_id");

        int databaseSizeBeforeCreate = organisationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationRepository.findAll().collectList().block().size();
        // set the field null
        organisation.setName(null);

        // Create the Organisation, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOrganisationsAsStream() {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        List<Organisation> organisationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Organisation.class)
            .getResponseBody()
            .filter(organisation::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(organisationList).isNotNull();
        assertThat(organisationList).hasSize(1);
        Organisation testOrganisation = organisationList.get(0);
        assertThat(testOrganisation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void getAllOrganisations() {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        // Get all the organisationList
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
            .value(hasItem(organisation.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getOrganisation() {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        // Get the organisation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, organisation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(organisation.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingOrganisation() {
        // Get the organisation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOrganisation() throws Exception {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findById(organisation.getId()).block();
        updatedOrganisation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOrganisation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOrganisation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();
        organisation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, organisation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();
        organisation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();
        organisation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganisation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();
        organisation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, organisation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();
        organisation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().collectList().block().size();
        organisation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organisation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrganisation() {
        // Initialize the database
        organisation.setId(UUID.randomUUID().toString());
        organisationRepository.save(organisation).block();

        int databaseSizeBeforeDelete = organisationRepository.findAll().collectList().block().size();

        // Delete the organisation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, organisation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Organisation> organisationList = organisationRepository.findAll().collectList().block();
        assertThat(organisationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
