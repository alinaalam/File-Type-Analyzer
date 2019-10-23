package analyzer.domain;

import org.jetbrains.annotations.NotNull;

public class Pattern implements Comparable {

    private final String name;
    private final int priority;
    private final String type;

    public Pattern(String name, int priority, String type) {
        this.name = name.replace("\"", "");
        this.priority = priority;
        this.type = type.replace("\"", "");
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return ((Pattern) o).priority - priority;
    }
}
