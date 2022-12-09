package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class Day8Part1 {

    public record Location (Integer x, Integer y) {}

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day8.txt"));
        var grid = new HashMap<Location, Integer>();
        for (var y = 0; y < lines.size(); y++) {
            var l = lines.get(y);
            for (var x = 0; x < l.length(); x++) {
                var n = Integer.parseInt(String.valueOf(l.charAt(x)));
                grid.put(new Location(x, y), n);
            }
        }
        var yMax = lines.size() - 1;
        var xMax = lines.get(0).length() - 1;

        var isVisible = new HashSet<Location>();

        // check visibility from top row
        for (var x = 0; x <= xMax; x++) {
            var highestSoFar = 0;
            for (var y = 0; y <= yMax; y++) {
                var l = new Location(x, y);
                var h = grid.get(l);

                if (y == 0 || h > highestSoFar) {
                    highestSoFar = h;
                    isVisible.add(l);
                }
            }
        }

        // check visibility from bottom row
        for (var x = 0; x <= xMax; x++) {
            var highestSoFar = 0;
            for (var y = yMax; y >= 0; y--) {
                var l = new Location(x, y);
                var h = grid.get(l);

                if (y == yMax || h > highestSoFar) {
                    highestSoFar = h;
                    isVisible.add(l);
                }
            }
        }

        // check visibility from left column
        for (var y = 0; y <= yMax; y++) {
            var highestSoFar = 0;
            for (var x = 0; x <= xMax; x++) {
                var l = new Location(x, y);
                var h = grid.get(l);

                if (x == 0 || h > highestSoFar) {
                    highestSoFar = h;
                    isVisible.add(l);
                }
            }
        }

        // check visibility from right column
        for (var y = 0; y <= yMax; y++) {
            var highestSoFar = 0;
            for (var x = xMax; x >= 0; x--) {
                var l = new Location(x, y);
                var h = grid.get(l);

                if (x == xMax || h > highestSoFar) {
                    highestSoFar = h;
                    isVisible.add(l);
                }
            }
        }
        System.out.print(isVisible.size());
    }
}
