package se.yrgo.libraryapp.validators;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameTest {
    /**
     *
     * A username should be at least four characters long and only
     * contain ASCII letters, numbers and the characters @, ., _ and -.
     * It should not contain any other letters, not even whitespace.
     *
     */

    @ParameterizedTest
    @ValueSource(strings = {"bosse", "b osse", "   ", "@@-.", "bos", "abed", "userÅ", "こんにちは", "name😊", "a$b", "1234"})
    @EmptySource
    void correctUsername(String name) {
        boolean result = Username.validate(name);
        assertThat(result).as("Expected valid username for input: '%s'", name).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bosse", "b osse", "   ", "@@-.", "bos", "abed", "userÅ", "こんにちは", "name😊", "a$b", "1234"})
    @EmptySource
    void incorrectUsername(String name) {
        boolean result = Username.validate(name);
        assertThat(result).as("Expected invalid username for input: '%s'", name).isFalse();
    }
}
