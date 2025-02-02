package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;

class PasswordCheckerTestPublic {
    ArrayList<String> passwordsArray;
    String password = "Hello";
    String passwordConfirm = "hello";
    String allCaps = "HELLO";
    String withDigit = "Hello6";

    @BeforeEach
    void setUp() throws Exception {
        String[] p = {"334455BB", "Im2cool4U", withDigit};
        passwordsArray = new ArrayList<>();
        passwordsArray.addAll(Arrays.asList(p));
    }

    @AfterEach
    void tearDown() throws Exception {
        passwordsArray = null;
    }

    @Test
    void testComparePasswords() {
        // Test passwords that don't match
        Throwable exception = assertThrows(UnmatchedException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                PasswordCheckerUtility.comparePasswordsWithReturn(password, passwordConfirm);
            }
        });
        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void testComparePasswordsWithReturn() {
        try {
            // Test if passwords match
            assertTrue(PasswordCheckerUtility.comparePasswordsWithReturn(password, password));
            // Test if passwords don't match (this should throw exception)
            assertThrows(UnmatchedException.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    PasswordCheckerUtility.comparePasswordsWithReturn(password, passwordConfirm);
                }
            });
        } catch (UnmatchedException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    void testHasUpperAlpha() {
        // Test with a valid password that contains an uppercase letter
        assertTrue(PasswordCheckerUtility.hasUpperAlpha("Beautiful"));
        
        // Test with a password that does not contain an uppercase letter
        assertFalse(PasswordCheckerUtility.hasUpperAlpha("beautiful"));
    }

    @Test
    void testIsValidLength() {
        // Test for password that is too short
        Throwable exception = assertThrows(LengthException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                PasswordCheckerUtility.isValidLength("Short");
            }
        });
        assertEquals("The password must be at least 6 characters long", exception.getMessage());

        // Test for valid password length
        try {
            PasswordCheckerUtility.isValidLength("ValidLength");
        } catch (LengthException e) {
            fail("Exception should not have been thrown for valid length");
        }
    }

    @Test
    void testGetInvalidPasswords() {
        ArrayList<String> results = PasswordCheckerUtility.getInvalidPasswords(passwordsArray);
        assertEquals(3, results.size());
        assertEquals("334455BB The password must contain at least one lowercase alphabetic character", results.get(0));
        assertEquals("Im2cool4U The password must contain at least one special character", results.get(1));
        assertEquals("Hello6 The password must contain at least one special character", results.get(2));  // Updated message
    }
}
