package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

public class Day10Part2 {
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

        var X = 1;
        for (cycle = 1; cycle <= 240; cycle++) {
            if (addAt.containsKey(cycle)) {
                X += addAt.get(cycle);
            }
            var pos = (cycle - 1) % 40;
            if (pos == 0) System.out.println();
            if (pos == X - 1 || pos == X || pos == X + 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }
        }
        // PLPAFBCL
    }
}
