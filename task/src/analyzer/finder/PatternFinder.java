package analyzer.finder;

import analyzer.domain.KMP;
import analyzer.domain.Pattern;
import analyzer.domain.PatternAlgorithm;
import analyzer.infra.cmd.ParamsParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PatternFinder {

    private ParamsParser paramsParser;
    private PatternAlgorithm patternAlgorithm;

    public PatternFinder(ParamsParser paramsParser, PatternAlgorithm patternAlgorithm) {
        this.paramsParser = paramsParser;
        this.patternAlgorithm = patternAlgorithm;
    }

    public void findPatternInFiles() throws Exception {
        List<String> files = getSortedFiles();
        ExecutorService executorService = Executors.newFixedThreadPool(files.size());

        List<Future<String>> futures = executorService.invokeAll(getListOfCallables(files));

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executorService.shutdown();
    }

    private List<String> getSortedFiles() throws Exception {
        List<String> filenames = new ArrayList<>();

        if (Files.exists(Paths.get(paramsParser.getDirectoryPath()))) {
            filenames = Files.list(Paths.get(paramsParser.getDirectoryPath()))
                    .filter(Files::isRegularFile)
                    .map(file -> file.toString())
                    .collect(Collectors.toList());

            // sort the filenames alphabetically
            filenames.sort(String::compareToIgnoreCase);
        }

        return filenames;
    }

    /**
     * go through all the files
     * get their content
     * and invoke KMP on them
     * @return boolean to indicate whether pattern matched or not
     */
    private List<Callable<String>> getListOfCallables(List<String> files) {
        List<Callable<String>> listOfCallables = new ArrayList<>();

        for (String file : files) {
            listOfCallables.add(() -> {
                try {
                    Path path = Path.of(file);
                    String fileContent = Files.readString(path);
                    List<Pattern> matchedPatterns = getMatchedPatterns(fileContent);
                    if (!matchedPatterns.isEmpty()) {
                        Collections.sort(matchedPatterns);
                        return path.getFileName() + ": " + matchedPatterns.get(0).getType();
                    }
                    else {
                        return path.getFileName() + ": Unknown file type";
                    }
                } catch (IOException e) {
                    // do nothing
                    throw e;
                }
            });
        }

        return listOfCallables;
    }

    private List<Pattern> getMatchedPatterns(String fileContent) {
        List<Pattern> matchedPatterns = new ArrayList<>();
        Map<String, Pattern> availablePatterns = paramsParser.getPatterns();

        for (String pattern : availablePatterns.keySet()) {
            if(patternAlgorithm.doesPatternExist(fileContent, pattern)) {
                matchedPatterns.add(availablePatterns.get(pattern));
            }
        }

        return matchedPatterns;
    }
}
