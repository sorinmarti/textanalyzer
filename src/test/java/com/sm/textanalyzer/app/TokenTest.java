package com.sm.textanalyzer.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testCreation() {
        Token token = new Token("token"); //NON-NLS
        assertEquals("token", token.getWord());
    }
}