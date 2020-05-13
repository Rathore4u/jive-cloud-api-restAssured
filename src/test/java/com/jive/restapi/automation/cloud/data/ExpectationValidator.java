package com.jive.restapi.automation.cloud.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class ExpectationValidator {

    private ExpectationValidator() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static void assertKeyEquals(String keyName, Object expected, Object actual) {
        String message = assertEqualsMessage(keyName, expected, actual);
        assertEquals(message, expected, actual);
    }

    public static void assertKeyNotEquals(String keyName, Object expected, Object actual) {
        String message = assertNotEqualsMessage(keyName, expected, actual);
        assertNotEquals(message, expected, actual);
    }

    public static void assertKeyTrue(String keyName, boolean actual) {
        String message = booleanMessage(keyName, true, actual);
        assertTrue(message, actual);
    }

    public static void assertHttpResponseEquals(Object expected, Object actual) {
        String message = assertEqualsHTTPMessage(expected, actual);
        assertEquals(message, expected, actual);
    }

    public static void assertKeyFalse(String keyName, boolean actual) {
        String message = booleanMessage(keyName, false, actual);
        assertFalse(message, actual);
    }

    public static void assertKeyNotNull(String keyName, Object actual) {
        String message = assertNotNullMessage(keyName, actual);
        assertNotNull(message, actual);
    }

    private static String assertEqualsMessage(String keyName, Object expected, Object actual) {
        return "Key " + keyName + ": Value " + expected + " should be equal to " + actual;
    }

    private static String assertNotEqualsMessage(String keyName, Object expected, Object actual) {
        return "Key " + keyName + ": Value " + expected + " should not equal to " + actual;
    }

    public static String booleanMessage(String keyName, boolean expected, boolean actual) {
        return "Key " + keyName + ": Expected to be " + expected + " but was " + actual;
    }

    private static String assertEqualsHTTPMessage(Object expected, Object actual) {
        return "Invalid HTTP status code: Expected " + expected + " but was " + actual;
    }

    private static String assertNotNullMessage(String keyName, Object actual) {
        return "Key " + keyName + ": Value " + "should not be NULL but was " + actual;
    }
}
