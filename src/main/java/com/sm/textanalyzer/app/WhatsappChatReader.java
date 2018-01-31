package com.sm.textanalyzer.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WhatsappChatReader {


    public static List<TokenChain> readFile(Path savedSource) throws IOException {
        List<TokenChain> chatLines = new ArrayList<>();

        try (Stream<String> stream = Files.lines(savedSource, StandardCharsets.UTF_8)) {
            stream.forEach((string) -> {
                //27.11.14, 10:43 - Dominique Massmünster: Ich nit.
                TokenChain chain = new TokenChain();
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

                chatLines.add(chain);
            });
        }

        return chatLines;
    }

}
