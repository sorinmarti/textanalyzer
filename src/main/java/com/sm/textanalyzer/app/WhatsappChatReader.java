package com.sm.textanalyzer.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WhatsappChatReader implements CorpusFileReader {

    public CorpusFile readFile(String name, Path savedSource) throws IOException {
        CorpusFile file = new CorpusFile(name, savedSource);      // TODO

        try (Stream<String> stream = Files.lines(savedSource, StandardCharsets.UTF_8)) {
            stream.forEach((string) -> {
                //27.11.14, 10:43 - Dominique Massmünster: Ich nit.
                TokenChain chain = file.addTokenChain();
                String[] parts;

                parts = string.split(" - ");
                chain.setDate( parts[0] );

                parts = parts[1].split(": ");
                chain.setAuthor( parts[0] );

                StringBuilder builder = new StringBuilder();
                for(int i=1;i<parts.length;i++) {
                    builder.append(parts[i]);
                }
                String tokens = builder.toString();

                parts = tokens.split(" ");
                for(String part : parts) {
                    chain.addToken( new Token( part ));
                }
            });
        }
        return file;
    }

}
