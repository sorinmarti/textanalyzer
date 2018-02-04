package com.sm.textanalyzer.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CorpusTest {

    private Corpus corpus;

    @Before
    public void setUp() throws Exception{
        // Create Corpus
        corpus = new Corpus("Name");

        // Create and Add 2 collections
        FileCollection collection1 = new FileCollection("Collection 1");
        CorpusFile file_1_1 = new CorpusFile();
        TokenChain chain_1_1_1 = new TokenChain();
        chain_1_1_1.setAuthor("Sorin Marti");
        chain_1_1_1.addToken( new Token("Hallo"));
        chain_1_1_1.addToken( new Token("Welt!"));
        TokenChain chain_1_1_2 = new TokenChain();
        chain_1_1_2.setAuthor("Sorin Marti");
        chain_1_1_2.addToken( new Token("Zweite"));
        chain_1_1_2.addToken( new Token("Zeile"));
        file_1_1.addTokenChain( chain_1_1_1 );
        file_1_1.addTokenChain( chain_1_1_2 );
        collection1.addFile( file_1_1 );

        CorpusFile file_1_2 = new CorpusFile();
        TokenChain chain_1_2_1 = new TokenChain();
        chain_1_2_1.setAuthor("Silvia");
        chain_1_2_1.addToken( new Token("Chat"));
        chain_1_2_1.addToken( new Token("Nummer"));
        chain_1_2_1.addToken( new Token("zwei..."));
        TokenChain chain_1_2_2 = new TokenChain();
        chain_1_2_2.setAuthor("Sorin Marti");
        chain_1_2_2.addToken( new Token("Und"));
        chain_1_2_2.addToken( new Token("nun"));
        chain_1_2_2.addToken( new Token("los?"));
        file_1_2.addTokenChain( chain_1_2_1 );
        file_1_2.addTokenChain( chain_1_2_2 );
        collection1.addFile( file_1_2 );

        FileCollection collection2 = new FileCollection("Collection 2");
        CorpusFile file_2_1 = new CorpusFile();
        TokenChain chain_2_1_1 = new TokenChain();
        chain_2_1_1.setAuthor("Sorin Marti");
        chain_2_1_1.addToken( new Token("Dies"));
        chain_2_1_1.addToken( new Token("ist"));
        chain_2_1_1.addToken( new Token("Nummer"));
        chain_2_1_1.addToken( new Token("vier"));
        TokenChain chain_2_1_2 = new TokenChain();
        chain_2_1_2.setAuthor("Silvia");
        chain_2_1_2.addToken( new Token("Ein"));
        chain_2_1_2.addToken( new Token("echter"));
        chain_2_1_2.addToken( new Token("Mist,"));
        chain_2_1_2.addToken( new Token("das"));
        file_2_1.addTokenChain( chain_2_1_1 );
        file_2_1.addTokenChain( chain_2_1_2 );
        collection2.addFile( file_2_1 );

        CorpusFile file_2_2 = new CorpusFile();
        TokenChain chain_2_2_1 = new TokenChain();
        chain_2_2_1.setAuthor("Peter Lustig");
        chain_2_2_1.addToken( new Token("Habe"));
        chain_2_2_1.addToken( new Token("heute"));
        chain_2_2_1.addToken( new Token("ein"));
        chain_2_2_1.addToken( new Token("Smiley"));
        chain_2_2_1.addToken( new Token("geschickt"));
        TokenChain chain_2_2_2 = new TokenChain();
        chain_2_2_2.setAuthor("Silvia");
        chain_2_2_2.addToken( new Token("Und"));
        chain_2_2_2.addToken( new Token("wie"));
        chain_2_2_2.addToken( new Token("war"));
        chain_2_2_2.addToken( new Token("das"));
        chain_2_2_2.addToken( new Token("so?"));
        file_2_2.addTokenChain( chain_2_2_1 );
        file_2_2.addTokenChain( chain_2_2_2 );
        collection2.addFile( file_2_2 );

        corpus.addCollection(collection1);
        corpus.addCollection(collection2);

    }

    @Test
    public void testCreatedCorpus() {
        assertEquals("Name", corpus.getName());
        assertEquals(2, corpus.getNumCollections());

        FileCollection col1 = corpus.getCollection( 0 );
        assertEquals("Collection 1", col1.getName());
        assertEquals(2, col1.getNumFiles());

        CorpusFile file_1_1 = col1.getFile( 0 );
        assertEquals(2, file_1_1.getNumTokenChains());
        assertEquals(4, file_1_1.getNumTokens());

        CorpusFile file_1_2 = col1.getFile( 1 );
        assertEquals(2, file_1_2.getNumTokenChains());
        assertEquals(6, file_1_2.getNumTokens());

        FileCollection col2 = corpus.getCollection( 1 );
        assertEquals("Collection 2", col2.getName());
        assertEquals(2, col2.getNumFiles());
    }
}