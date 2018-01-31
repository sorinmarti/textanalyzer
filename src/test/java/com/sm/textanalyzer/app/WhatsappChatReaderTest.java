package com.sm.textanalyzer.app;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WhatsappChatReaderTest {

    @Test
    public void testCreation() throws IOException {
        List<Token> chatMessages = WhatsappChatReader.readFile(Paths.get("res/test/whatsapp.txt"));
        assertEquals( 50, chatMessages.size() );

        Token token = chatMessages.get(7);
        assertNotNull(token);

        //assertEquals( token.get);
        //27.11.14, 10:43 - Dominique Massmünster: Ich nit.

    }
}
