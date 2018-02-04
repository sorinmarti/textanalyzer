package com.sm.textanalyzer.app;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class WhatsappChatReaderTest {

    @Test
    public void testCreation() throws IOException {
        WhatsappChatReader reader = new WhatsappChatReader();
        CorpusFile file = reader.readFile("Testfile 1", Paths.get("res/test/whatsapp_test.txt"));
        assertEquals( 50, file.getNumTokenChains() );

        TokenChain tokenChain = file.getTokenChain( 7 );
        assertEquals( 2, tokenChain.getNumTokens() );
        assertEquals("Dominique Massmünster", tokenChain.getAuthor());
        assertEquals("27.11.14, 10:43", tokenChain.getDate());

        Token token1 = tokenChain.getToken( 0 );
        assertEquals("Ich", token1.getWord() );

        Token token2 = tokenChain.getToken( 1 );
        assertEquals("nit.", token2.getWord() );
    }
}
