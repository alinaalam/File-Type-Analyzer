package analyzer;

import analyzer.finder.PatternFinder;
import analyzer.infra.cmd.ParamsParser;

public class Main {

    public static void main(String[] args) throws Exception {
        ParamsParser paramsParser = new ParamsParser(args);
        PatternFinder patternFinder = new PatternFinder(paramsParser);
        patternFinder.findPatternInFiles();
    }
}
