package com.quasiris.qsf.commons.ai.embedding;

import com.quasiris.qsf.commons.ai.dto.TextVector;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BertAsAServiceEncoderTest {

    @Ignore // require external service for test
    @Test
    public void embed() {
        BertAsAServiceEncoder encoder = new BertAsAServiceEncoder("http://localhost:8125/encode", 30000);
        List<TextVector> textVectors = encoder.embed("convert this text into a vector please", null, false);

        assertNotNull(textVectors);
    }
}