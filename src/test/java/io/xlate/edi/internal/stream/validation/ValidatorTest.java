package io.xlate.edi.internal.stream.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import io.xlate.edi.schema.EDIReference;
import io.xlate.edi.schema.EDISchemaException;
import io.xlate.edi.schema.Schema;
import io.xlate.edi.schema.SchemaFactory;
import io.xlate.edi.stream.EDIStreamConstants.Standards;

class ValidatorTest {

    @Test
    void testValidatorRootAttributes() throws EDISchemaException {
        SchemaFactory schemaFactory = SchemaFactory.newFactory();
        Schema schema = schemaFactory.getControlSchema(Standards.X12, new String[] { "00801" });
        Validator validator = new Validator(schema, true, null);
        EDIReference interchangeReference = validator.root.getLink();

        assertSame(schema.getStandard(), interchangeReference.getReferencedType());
        assertEquals(1, interchangeReference.getMinOccurs());
        assertEquals(1, interchangeReference.getMaxOccurs());
        // Title value specified in v00704.xml schema
        assertEquals("X12 Interchange (minimum version 00704)", interchangeReference.getTitle());
        // No description specified in schema
        assertNull(interchangeReference.getDescription());
    }

}
