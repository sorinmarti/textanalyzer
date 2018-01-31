package com.sm.textanalyzer.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class WhatsappChatReader {


    public static List<Token> readFile(Path savedSource) throws IOException {
        try (Stream<String> stream = Files.lines(savedSource, StandardCharsets.ISO_8859_1)) {
            stream.forEach((string) -> {
                System.out.println(string);
            });
        }
        return null;
    }

}
