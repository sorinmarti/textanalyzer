package com.sm.textanalyzer.app;

import java.io.IOException;
import java.nio.file.Path;

public interface CorpusFileReader {

    CorpusFile readFile(String name, Path savedSource) throws IOException;
}
