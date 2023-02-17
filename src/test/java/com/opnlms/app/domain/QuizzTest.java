package com.opnlms.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.opnlms.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quizz.class);
        Quizz quizz1 = new Quizz();
        quizz1.setId("id1");
        Quizz quizz2 = new Quizz();
        quizz2.setId(quizz1.getId());
        assertThat(quizz1).isEqualTo(quizz2);
        quizz2.setId("id2");
        assertThat(quizz1).isNotEqualTo(quizz2);
        quizz1.setId(null);
        assertThat(quizz1).isNotEqualTo(quizz2);
    }
}
