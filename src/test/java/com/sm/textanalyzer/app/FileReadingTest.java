package com.sm.textanalyzer.app;

import org.junit.Test;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileReadingTest {

    @Test
    public void testFileReading() {
        List<Path> filePaths = FileUtils.collectFilenames("res/test");
        assertEquals(3, filePaths.size());
        assertEquals(true, filePaths.get(0).endsWith("res/test/test.txt"));
    }

    @Test
    public void testFileFormatting() throws NoSuchFileException {
        List<Path> filePaths = FileUtils.collectFilenames("res/test");

        FormattedFile file = new FormattedFile(filePaths.get(0));
        file.readFile();

        // 1.) Assert the path is saved correctly
        assertEquals(filePaths.get(0), file.getPath());
        // 2.) Assert that the test file text is read correctly
        assertEquals("Ein Wòrt Wort durch Lêèrzeîchen\neine zweite Zeile, mit Kômma und und Punkt, ja.", file.getText());
        // 3.) Assert the number of tokens is correct
        assertEquals(14, file.getTokens().size());
        // 4.) Assert that interpunction characters are still present
        assertEquals("Zeile", file.getTokens().get(7).getWord());
        assertEquals("Punkt", file.getTokens().get(12).getWord());
        // 5.) Assert the number of CLEAN tokens is correct
        assertEquals(14, file.getCleanTokens().size());
        // 6.) Assert that interpunction characters were removed
        assertEquals("zeile", file.getCleanTokens().get(7).getWord());
        assertEquals("punkt", file.getCleanTokens().get(12).getWord());
        // 7.) Assert that the test file text is formatted correctly for 'clean text'
        assertEquals("ein wort wort durch lêerzeîchen\neine zweite zeile mit kômma und und punkt ja", file.getCleanText());
        // 8.) Assert that the context of a word gets read correctly
        assertEquals("zweite [Zeile] mit", file.getContext(7, 1, 1));
        assertEquals("eine zweite [Zeile] mit Kômma", file.getContext(7, 2, 2));
        assertEquals("[Kômma]", file.getContext(9, 0, 0));
        assertEquals("[Kômma] und und Punkt", file.getContext(9, 0, 3));
        // 9.) Assert that BeginOfFile/EndOfFile is handled correctly
        assertEquals("[BOF] Ein [Wòrt]", file.getContext(1, 255, 0));
        assertEquals("[Kômma] und und Punkt ja [EOF]", file.getContext(9, 0, 255));
        // 10.) Assert that the CLEAN context of a word gets read correctly
        assertEquals("zweite [zeile] mit", file.getCleanContext(7, 1, 1));
        assertEquals("eine zweite [zeile] mit kômma", file.getCleanContext(7, 2, 2));
        assertEquals("[kômma]", file.getCleanContext(9, 0, 0));
        assertEquals("[kômma] und und punkt", file.getCleanContext(9, 0, 3));
        // 11.) Assert that BeginOfFile/EndOfFile in CLEAN text is handled correctly
        assertEquals("[BOF] ein [wort]", file.getCleanContext(1, 255, 0));
        assertEquals("[kômma] und und punkt ja [EOF]", file.getCleanContext(9, 0, 255));
        // 12.) Assert Type list is correct
        assertEquals(13, file.getTypes().size());
        assertEquals(12, file.getCleanTypes().size());
    }


    @Test
    public void testFileMerging() throws NoSuchFileException {
        List<Path> filePaths = FileUtils.collectFilenames("res/test");

        for (Path path : filePaths) {
            FormattedFile file = new FormattedFile(path);
            file.readFile();
            TextLibrary.getInstance().addFile(file);
        }

        // 1.) Number of files
        assertEquals(3, TextLibrary.getInstance().getNumFiles());
        // 2.) Merge files and check new number of words
        FormattedFile mergedFile = TextLibrary.getInstance().getMergedFile();
        assertEquals(34005, mergedFile.getTokens().size());
        // 3.) Check word order
        assertEquals("Ein", mergedFile.getTokens().get(0).getWord());        // First word in first file
        assertEquals("Eine", mergedFile.getTokens().get(14).getWord());        // First word in second file
    }

    @Test
    public void testGrouping() throws NoSuchFileException {
        List<Path> filePaths = FileUtils.collectFilenames("res/test");

        for (Path path : filePaths) {
            FormattedFile file = new FormattedFile(path);
            file.readFile();
            TextLibrary.getInstance().addFile(file);
        }
        //FormattedFile mergedFile = TextLibrary.getInstance().getMergedFile();
    }
}

