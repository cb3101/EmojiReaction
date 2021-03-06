/*
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package amplify.utils;


import amplify.helpers.BaseTest;
import com.example.amplify.lib.utils.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * StringUtilsTest extends BaseTest
 */
@SuppressWarnings("ConstantConditions")
public class StringUtilsTest extends BaseTest {

    private static final String DEFAULT_STRING = "any other string";
    private static final String DEFAULT_IF_BLANK = "defaultIfBlank should have returned the default string";

    @Test
    public void testThatDefaultIfBlankReturnsPrimaryStringIfItContainsAtLeastOneNonWhitespaceCharacter() {
        // Arrange
        final String primaryString = "any string containing at least one non-whitespace character";
        assert !primaryString.equals(DEFAULT_STRING);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(primaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                DEFAULT_IF_BLANK,
                primaryString,
                sanitizedString);
    }

    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsWhitespaceOnly() {
        // Arrange
        final String whitespaceOnlyPrimaryString = "    ";
        assert !DEFAULT_STRING.equals(whitespaceOnlyPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(whitespaceOnlyPrimaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                DEFAULT_IF_BLANK,
                DEFAULT_STRING,
                sanitizedString);
    }

    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsEmpty() {
        // Arrange
        final String emptyPrimaryString = "";
        assert !DEFAULT_STRING.equals(emptyPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(emptyPrimaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                DEFAULT_IF_BLANK,
                DEFAULT_STRING,
                sanitizedString);
    }

    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsNull() {
        // Arrange
        final String nullPrimaryString = null;
        assert !DEFAULT_STRING.equals(nullPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(nullPrimaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                DEFAULT_IF_BLANK,
                DEFAULT_STRING,
                sanitizedString);
    }

}
