package io.xlate.edi.internal.schema.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.xlate.edi.internal.schema.ElementPosition;
import io.xlate.edi.schema.EDIElementPosition;
import io.xlate.edi.schema.implementation.Discriminator;

class DiscriminatorImplTest {

    DiscriminatorImpl target;

    @BeforeEach
    void setUp() throws Exception {
        target = new DiscriminatorImpl(position(1, 2), Collections.singleton("50"));
    }

    EDIElementPosition position(int e, int c) {
        return new ElementPosition(e, c);
    }

    @Test
    void testHashCode() {
        int expected = new DiscriminatorImpl(position(1, 2), Collections.singleton("50")).hashCode();
        assertEquals(expected, target.hashCode());
    }

    @Test
    void testEquals_Same() {
        assertEquals(target, target);
    }

    @Test
    void testEquals_Identical() {
        Discriminator identical = new DiscriminatorImpl(position(1, 2), Collections.singleton("50"));
        assertEquals(target, identical);
    }

    @Test
    void testEquals_NotInstance() {
        for (Object other : Arrays.asList(new Object(), null)) {
            assertNotEquals(target, other);
        }
    }

    @Test
    void testEquals_Different() {
        assertNotEquals(new DiscriminatorImpl(position(1, 2), Collections.singleton("60")), target);
        assertNotEquals(new DiscriminatorImpl(position(2, 2), Collections.singleton("50")), target);
        assertNotEquals(new DiscriminatorImpl(position(1, 3), Collections.singleton("50")), target);
    }

    @Test
    void testToString() {
        String expected = new DiscriminatorImpl(position(1, 2), Collections.singleton("50")).toString();
        assertEquals(expected, target.toString());
    }

}
