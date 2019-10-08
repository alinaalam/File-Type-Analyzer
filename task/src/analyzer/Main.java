package analyzer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Some arguments are missing");
        }
        else {
            String algorithm = args[0];
            String file = args[1];
            String pattern = args[2];
            String type = args[3];
            String extension = file.split("\\.")[1];

            // open the file and find the pattern
            long startTime = System.nanoTime();
            if (extension.equals("pdf") && checkIfPatternExistsInFile(algorithm, file, pattern)) {
                System.out.println(type);
            }
            else {
                System.out.println("Unknown file type");
            }
            System.out.println("It took: " + (System.nanoTime() - startTime) / 1000000000 + " seconds");
        }
    }

    private static boolean checkIfPatternExistsInFile(String algorithm, String filename, String pattern) {
        try {
            String fileContent = Files.readString(Path.of(filename));
            switch (algorithm) {
                case "--naive":
                    return naiveApproach(fileContent, pattern);
                case "--KMP":
                    return KMPApproach(fileContent, pattern);
            }
        } catch (IOException e) {
            System.out.println("Error in opening the file");
        }
        return false;
    }

    private static boolean naiveApproach(String text, String pattern) {
        return text.contains(pattern);
    }

    private static boolean KMPApproach(String text, String pattern) {
        return isMatch(text, pattern, prefixFunction(text));
    }

    private static boolean isMatch(String text, String pattern, int[] prefixArray) {
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefixArray[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == pattern.length()) {
                return true;
            }
        }
        return false;
    }

    private static int[] prefixFunction(String str) {
        /* 1 */
        int[] prefixFunc = new int[str.length()];

        /* 2 */
        for (int i = 1; i < str.length(); i++) {
            /* 3 */
            int j = prefixFunc[i - 1];

            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefixFunc[j - 1];
            }

            /* 4 */
            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }

            /* 5 */
            prefixFunc[i] = j;
        }

        /* 6 */
        return prefixFunc;
    }
}

