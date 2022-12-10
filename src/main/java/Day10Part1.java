package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

public class Day10Part1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day10.txt"));

        var cycle = 1;
        var addAt = new HashMap<Integer, Integer>();
        for (var l : lines) {
            var parts = l.split(" ");

            if (parts[0].equals("noop")) {
                cycle += 1;
            } else {
                var c = Integer.parseInt(parts[1]);
                cycle += 2;
                addAt.put(cycle, c);
            }
        }

        var strength = 0;
        var X = 1;
        var interesting = Set.of(20, 60, 100, 140, 180, 220);
        for (cycle = 1; cycle <= 220; cycle++) {
            if (addAt.containsKey(cycle)) {
                X += addAt.get(cycle);
            }
            if (interesting.contains(cycle)) {
                strength += cycle * X;
            }
        }
        System.out.println(strength);
    }
}
