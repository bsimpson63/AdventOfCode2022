package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Day6Part2 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day6.txt"));
        var line = lines.get(0);
        System.out.println((findStart(line, 14)));
    }

    public static int findStart(String s, int n) {
        for (var i = 0; i < s.length(); i++) {
            if (i < n) continue;
            var chars = new HashSet<Character>();

            for (var j = 0; j < n; j++) {
                chars.add(s.charAt(i - j));
            }

            if (chars.size() == n) {
                return i + 1;
            }
        }
        return 0;
    }
}
