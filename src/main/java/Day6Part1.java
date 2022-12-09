package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Day6Part1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day6.txt"));
        var line = lines.get(0);
        for (var i = 0; i < line.length(); i++) {
            if (i < 3) continue;
            var s = new HashSet<Character>();
            s.add(line.charAt(i));
            s.add(line.charAt(i - 1));
            s.add(line.charAt(i - 2));
            s.add(line.charAt(i - 3));
            if (s.size() == 4) {
                System.out.println(i + 1);
                break;
            }
        }
    }
}
