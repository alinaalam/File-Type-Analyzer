package analyzer.infra.cmd;

public class ParamsParser {
    private String pattern;
    private String type;
    private String directoryPath;

    public ParamsParser(String[] args) {
        parse(args);
    }

    private void parse(String[] args) {
        directoryPath = args[0];
        pattern = args[1];
        type = args[2];
    }

    public String getPattern() {
        return pattern;
    }

    public String getType() {
        return type;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }
}
