package org.tudalgo.algoutils.tutor.general.io.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Defines unit tests for {@link JavaSourceCodeParser}.
 *
 * @author Nhan Huynh
 */
@DisplayName("JavaSourceCodeParser")
public class JavaSourceCodeParserTest {

    /**
     * Defines unit tests for {@link JavaSourceCodeParser#isPackageNameToken(char)}.
     */
    @DisplayName("isPackageNameToken(char)")
    @Nested
    public class IsPackageNameTokenTest {

        /**
         * Tests correct behavior for alphabetic tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Alphabetic token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(chars = {'A', 'f', 'K', 'r', 'U', 'z'})
        public void testAlphabetic(char token) {
            Assertions.assertTrue(JavaSourceCodeParser.isPackageNameToken(token));
        }

        /**
         * Tests correct behavior for digit tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Digit token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,})
        public void testDigit(int token) {
            Assertions.assertTrue(JavaSourceCodeParser.isPackageNameToken(Character.forDigit(token, 10)));
        }

        /**
         * Tests correct behavior for symbol tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Symbol token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(chars = {'.', '_', '$'})
        public void testSymbol(char token) {
            Assertions.assertTrue(JavaSourceCodeParser.isPackageNameToken(token));
        }

        /**
         * Tests correct behavior for invalid tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Invalid token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(chars = {'*', '/', ',', ';', '{', '}', '[', ']'})
        public void testInvalid(char token) {
            Assertions.assertFalse(JavaSourceCodeParser.isPackageNameToken(token));
        }
    }

    /**
     * Defines unit tests for {@link JavaSourceCodeParser#isClassNameToken(char)}.
     */
    @DisplayName("isClassNameToken(char)")
    @Nested
    public class IsClassNameToken {

        /**
         * Tests correct behavior for alphabetic tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Alphabetic token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(chars = {'A', 'f', 'K', 'r', 'U', 'z'})
        public void testAlphabetic(char token) {
            Assertions.assertTrue(JavaSourceCodeParser.isClassNameToken(token));
        }

        /**
         * Tests correct behavior for digit tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Digit token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,})
        public void testDigit(int token) {
            Assertions.assertTrue(JavaSourceCodeParser.isClassNameToken(Character.forDigit(token, 10)));
        }

        /**
         * Tests correct behavior for alphabetic tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Symbol token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(chars = {'_', '$'})
        public void testSymbol(char token) {
            Assertions.assertTrue(JavaSourceCodeParser.isClassNameToken(token));
        }

        /**
         * Tests correct behavior for invalid tokens.
         *
         * @param token the token to test
         */
        @DisplayName("Invalid token")
        @ParameterizedTest(name = "Token = {0}")
        @ValueSource(chars = {'*', '/', ',', ';', '{', '}', '[', ']'})
        public void testInvalid(char token) {
            Assertions.assertFalse(JavaSourceCodeParser.isPackageNameToken(token));
        }
    }
}

