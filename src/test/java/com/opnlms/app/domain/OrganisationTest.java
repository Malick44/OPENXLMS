package com.opnlms.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.opnlms.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = new Organisation();
        organisation1.setId("id1");
        Organisation organisation2 = new Organisation();
        organisation2.setId(organisation1.getId());
        assertThat(organisation1).isEqualTo(organisation2);
        organisation2.setId("id2");
        assertThat(organisation1).isNotEqualTo(organisation2);
        organisation1.setId(null);
        assertThat(organisation1).isNotEqualTo(organisation2);
    }
}
