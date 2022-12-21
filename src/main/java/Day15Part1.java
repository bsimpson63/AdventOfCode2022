package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class Day15Part1 {
    public record Position(Integer x, Integer y) {}
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day15.txt"));
        var p = Pattern.compile("Sensor at x=([-\\d]+), y=([-\\d]+): closest beacon is at x=([-\\d]+), y=([-\\d]+)");

        var row = new HashSet<Position>();
        var taken = new HashSet<Position>();

        // short
        //var target = 10;
        var target = 2000000;
        for (var l : lines) {
            var m = p.matcher(l);
            m.find();
            var x1 = Integer.parseInt(m.group(1));
            var y1 = Integer.parseInt(m.group(2));
            var x2 = Integer.parseInt(m.group(3));
            var y2 = Integer.parseInt(m.group(4));
            var d = abs(x1 - x2) + abs(y1 - y2);

            if (y1 == target) taken.add(new Position(x1, y1));
            if (y2 == target) taken.add(new Position(x2, y2));

            for (var dy = 0; dy <= d; dy++) {
                if (y1 + dy == target) {
                    for (var dx = 0; dx <= d - dy; dx++) {
                        row.add(new Position(x1 + dx, y1 + dy));
                        row.add(new Position(x1 - dx, y1 + dy));
                    }
                } else if (y1 - dy == target) {
                    for (var dx = 0; dx <= d - dy; dx++) {
                        row.add(new Position(x1 + dx, y1 - dy));
                        row.add(new Position(x1 - dx, y1 - dy));
                    }
                }
            }
        }
        System.out.printf("%s: %s\n", row, row.size());
        System.out.printf("%s: %s\n", taken, taken.size());
    }
}
