package com.opnlms.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.opnlms.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enrollment.class);
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId("id1");
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(enrollment1.getId());
        assertThat(enrollment1).isEqualTo(enrollment2);
        enrollment2.setId("id2");
        assertThat(enrollment1).isNotEqualTo(enrollment2);
        enrollment1.setId(null);
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }
}
