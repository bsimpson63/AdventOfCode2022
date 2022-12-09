package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class Day8Part2 {

    public record Location (Integer x, Integer y) {}

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day8.txt"));
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
        var highestScore = 0;

        for (var x = 1; x <= xMax - 1; x++) {
            for (var y = 1; y <= yMax - 1; y++) {
                var s = 1;
                var h = grid.get(new Location(x, y));

                // look up
                var dy = 1;
                while (dy <= yMax - y) {
                    var hn = grid.get(new Location(x, y + dy));
                    if (hn >= h) break;
                    dy++;
                }
                if (dy > yMax - y) dy = yMax - y;
                s *= dy;

                // look down
                dy = 1;
                while (dy <= y) {
                    var hn = grid.get(new Location(x, y - dy));
                    if (hn >= h) break;
                    dy++;
                }
                if (dy > y) dy = y;
                s *= dy;

                // look right
                var dx = 1;
                while (dx <= xMax - x) {
                    var hn = grid.get(new Location(x + dx, y));
                    if (hn >= h) break;
                    dx++;
                }
                if (dx > xMax - x) dx = xMax - x;
                s *= dx;

                // look left
                dx = 1;
                while (dx <= x) {
                    var hn = grid.get(new Location(x - dx, y));
                    if (hn >= h) break;
                    dx++;
                }
                if (dx > x) dx = x;
                s *= dx;

                //System.out.printf("(%s,%s): %s\n", x, y, s);
                if (s > highestScore) highestScore = s;
            }
        }
        System.out.println(highestScore);
    }
}
