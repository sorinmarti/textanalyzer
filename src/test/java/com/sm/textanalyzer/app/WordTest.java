package com.sm.textanalyzer.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordTest {

    @Test
    public void testCreation() {
        Word word = new Word("word"); //NON-NLS
        assertEquals("word", word.getWord());
    }
}