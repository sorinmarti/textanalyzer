package com.sm.textanalyzer.app;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WhatsappChatReaderTest {

    @Test
    public void testCreation() throws IOException {
        List<TokenChain> chatMessages = WhatsappChatReader.readFile(Paths.get("res/test/whatsapp_test.txt"));
        assertEquals( 50, chatMessages.size() );

        TokenChain tokenChain = chatMessages.get(7);
        assertEquals( 2, tokenChain.getNumTokens() );
        assertEquals("Dominique Massmünster", tokenChain.getAuthor());
        assertEquals("27.11.14, 10:43", tokenChain.getDate());

        Token token1 = tokenChain.getToken( 0 );
        assertEquals("Ich", token1.getWord() );

        Token token2 = tokenChain.getToken( 1 );
        assertEquals("nit.", token2.getWord() );
    }
}
