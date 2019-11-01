package analyzer;

import analyzer.domain.KMP;
import analyzer.domain.PatternAlgorithm;
import analyzer.domain.RabinKarp;
import analyzer.finder.PatternFinder;
import analyzer.infra.cmd.ParamsParser;

public class Main {

    public static void main(String[] args) throws Exception {
        ParamsParser paramsParser = new ParamsParser(args);
        PatternAlgorithm KMP = new KMP();
        PatternAlgorithm RabinKarp = new RabinKarp();
        PatternFinder patternFinder = new PatternFinder(paramsParser, RabinKarp);
        patternFinder.findPatternInFiles();
    }
}
