/*******************************************************************************
 * Copyright 2017 xlate.io LLC, http://www.xlate.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package io.xlate.edi.internal.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.xlate.edi.schema.EDISimpleType;

//java:S107 : Constructor has 8 arguments
//java:S2160 : Intentionally inherit 'equals' from superclass
@SuppressWarnings({ "java:S107", "java:S2160" })
class ElementType extends BasicType implements EDISimpleType {

    private static final String TOSTRING_FORMAT = "id: %s, type: %s, base: %s, code: %s, minLength: %d, maxLength: %d, values: %s";

    final Base base;
    final int scale;
    final String code;
    final int number;
    final long minLength;
    final long maxLength;
    final Map<String, String> values;
    final List<Version> versions;

    static class Version extends VersionedProperty {
        final Optional<Long> minLength;
        final Optional<Long> maxLength;
        final Optional<Map<String, String>> values;

        Version(String minVersion, String maxVersion, Long minLength, Long maxLength, Map<String, String> values) {
            super(minVersion, maxVersion);
            this.minLength = Optional.ofNullable(minLength);
            this.maxLength = Optional.ofNullable(maxLength);
            this.values = Optional.ofNullable(values);
        }

        public long getMinLength(ElementType defaultElement) {
            return minLength.orElseGet(defaultElement::getMinLength);
        }

        public long getMaxLength(ElementType defaultElement) {
            return maxLength.orElseGet(defaultElement::getMaxLength);
        }

        public Map<String, String> getValues(ElementType defaultElement) {
            return values.orElseGet(defaultElement::getValues);
        }
    }

    ElementType(String id, Base base, int scale, String code, int number, long minLength, long maxLength, Map<String, String> values, List<Version> versions, String title, String description) {
        super(id, Type.ELEMENT, title, description);
        this.base = base;
        this.scale = scale;
        this.code = code;
        this.number = number;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
        this.versions = Collections.unmodifiableList(new ArrayList<>(versions));
    }

    <T> T getVersionAttribute(String version, BiFunction<Version, ElementType, T> versionedSupplier, Supplier<T> defaultSupplier) {
        for (Version ver : versions) {
            if (ver.appliesTo(version)) {
                return versionedSupplier.apply(ver, this);
            }
        }

        return defaultSupplier.get();
    }

    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, getId(), getType(), base, code, minLength, maxLength, values);
    }

    @Override
    public Base getBase() {
        return base;
    }

    @Override
    public Integer getScale() {
        if (scale > -1) {
            return Integer.valueOf(scale);
        }

        // Use the default value otherwise
        return EDISimpleType.super.getScale();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public boolean hasVersions() {
        return !versions.isEmpty();
    }

    /**
     * @see io.xlate.edi.schema.EDISimpleType#getNumber()
     * @deprecated
     */
    @SuppressWarnings({ "java:S1123", "java:S1133" })
    @Override
    @Deprecated
    public int getNumber() {
        return number;
    }

    @Override
    public long getMinLength() {
        return minLength;
    }

    @Override
    public long getMinLength(String version) {
        return getVersionAttribute(version, Version::getMinLength, this::getMinLength);
    }

    @Override
    public long getMaxLength() {
        return maxLength;
    }

    @Override
    public long getMaxLength(String version) {
        return getVersionAttribute(version, Version::getMaxLength, this::getMaxLength);
    }

    @Override
    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public Map<String, String> getValues(String version) {
        return getVersionAttribute(version, Version::getValues, this::getValues);
    }
}
