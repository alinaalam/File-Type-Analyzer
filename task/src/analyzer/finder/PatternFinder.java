package analyzer.finder;

import analyzer.domain.KMP;
import analyzer.infra.cmd.ParamsParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PatternFinder {

    private ParamsParser paramsParser;

    public PatternFinder(ParamsParser paramsParser) {
        this.paramsParser = paramsParser;
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
                    if (KMP.isPatternExists(fileContent, paramsParser.getPattern())) {
                        return path.getFileName() + ": " + paramsParser.getType();
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
}
