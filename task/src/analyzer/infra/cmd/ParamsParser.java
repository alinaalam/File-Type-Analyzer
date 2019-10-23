package analyzer.infra.cmd;

import analyzer.domain.Pattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ParamsParser {
    private Map<String, Pattern> patterns = new HashMap<>();
    private String directoryPath;

    public ParamsParser(String[] args) {
        parse(args);
    }

    public Map<String, Pattern> getPatterns() {
        return patterns;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    private void parse(String[] args) {
        directoryPath = args[0];
        String patternFile = args[1];
        populatePatterns(patternFile);
    }

    private void populatePatterns(String patternFile) {
        try {
            String content = Files.readString(Path.of(patternFile));
            String[] variousPatterns = content.split("\\n");

            for (String pattern : variousPatterns) {
                String[] parsedPattern = pattern.split(";");
                Pattern currentPattern = new Pattern(parsedPattern[1], Integer.parseInt(parsedPattern[0]), parsedPattern[2]);
                patterns.put(currentPattern.getName(), currentPattern);
            }
        } catch (IOException e) {
            // do nothing
        }
    }
}
