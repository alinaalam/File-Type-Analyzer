
import analyzer.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;
import org.junit.BeforeClass;

import java.util.List;

public class MainTest extends BaseStageTest<Clue> {
    static String largeContent;

    String pdf = "PDF document";
    String doc = "DOC document";
    String unknown = "Unknown file type";
    String file = "doc.pdf";

    public MainTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            new TestCase<Clue>()
                .addArguments(new String[] {
                        "--naive",
                        file,
                        "%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF-",
                        pdf
                    })
                .addFile(file, largeContent)
                .setAttach(new Clue(pdf)),

            new TestCase<Clue>()
                .addArguments(new String[] {
                        "--KMP",
                        file,
                        "%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF-",
                        doc
                    })
                .addFile(file, largeContent)
                .setAttach(new Clue(doc)),

            new TestCase<Clue>()
                .addArguments(new String[] {
                        "--naive",
                        file,
                        "%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF+",
                        unknown
                    })
                .addFile(file, largeContent)
                .setAttach(new Clue(unknown)),

            new TestCase<Clue>()
                .addArguments(new String[] {
                        "--KMP",
                        file,
                        "%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF%PDF+",
                        unknown
                    })
                .addFile(file, largeContent)
                .setAttach(new Clue(unknown))
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        reply = reply.strip();

        if (reply.contains(doc) && reply.contains(pdf)) {
            return CheckResult.FALSE("There are both \"" + doc + "\" and \"" + pdf + "\"" +
                " in output, should be only one of them");
        } else if (reply.contains(doc) && reply.contains(unknown)) {
            return CheckResult.FALSE("There are both \"" + doc + "\" and \"" + unknown + "\"" +
                " in output, should be only one of them");
        } else if (reply.contains(pdf) && reply.contains(unknown)) {
            return CheckResult.FALSE("There are both \"" + pdf + "\" and \"" + unknown + "\"" +
                " in output, should be only one of them");
        }

        if (!reply.contains(clue.response)) {
            return CheckResult.FALSE("Your output does not contain \"" + clue.response + "\", but should." +
                "\n\nYour output:\n" + reply);
        }

        if (!reply.contains("seconds")) {
            return CheckResult.FALSE("Your output does not contain \"seconds\", but should." +
                "\n\nYour output:\n" + reply);
        }

        return CheckResult.TRUE;
    }

    @BeforeClass
    public static void setLargeContent() {

        StringBuilder content = new StringBuilder();

        for (int i = 0; i< 6000; i++) {
            content.append("%PDF");
        }

        content.append("%PDF-");
        largeContent = content.toString();
    }
}
