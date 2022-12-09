package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day1.txt"));

        List<Integer> counts = new ArrayList<>();
        var count = 0;
        for (var l : lines) {
            if ("".equals(l)) {
                counts.add(count);
                count = 0;
            } else {
                count += Integer.parseInt(l);
            }
        }
        Collections.sort(counts);
        Collections.reverse(counts);
        var topCount = 0;
        for (var i = 0; i < 3; i++) {
            System.out.printf("%s: %s\n", i, counts.get(i));
            topCount += counts.get(i);
        }
        System.out.printf("Top 3: %s\n", topCount);
    }
}
