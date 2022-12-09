package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.Math.abs;

public class Day9Part2 {
    public record Move(String direction, Integer count) {
    }

    public record Location(Integer x, Integer y) {

        public Location move(String direction) {
            switch (direction) {
                case "U" -> {
                    return new Location(x, y - 1);
                }
                case "D" -> {
                    return new Location(x, y + 1);
                }
                case "L" -> {
                    return new Location(x - 1, y);
                }
                case "R" -> {
                    return new Location(x + 1, y);
                }
                default -> {
                    return new Location(x, y);
                }
            }
        }

        public Location catchUp(Location other) {
            if (abs(x - other.x) <= 1 && abs(y - other.y) <= 1) return new Location(x, y);

            var newX = x;
            var newY = y;

            if (abs(x - other.x) == 1 && abs(y - other.y) == 2) {
                newX = other.x;
            } else if (abs(x - other.x) > 1) {
                newX = (x + other.x) / 2;
            }

            if (abs(y - other.y) == 1 && abs(x - other.x) == 2) {
                newY = other.y;
            } else if (abs(y - other.y) > 1) {
                newY = (y + other.y) / 2;
            }
            return new Location(newX, newY);
        }
    }

    public static void printVisited(HashSet<Location> visited, Integer minX, Integer maxX, Integer minY, Integer maxY, Location head, Location tail) {
        for (var y = minY; y <= maxY; y++) {
            for (var x = minX; x <= maxX; x++) {
                if (head.x == x && head.y == y) {
                    System.out.print("H");
                } else if (tail.x == x && tail.y == y) {
                    System.out.print("T");
                } else if (visited.contains(new Location(x, y))) {
                    System.out.print("*");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
    public static void printVisited(HashSet<Location> visited, Location head, Location tail) {
        var minX = visited.stream().map(l -> l.x).min(Integer::compareTo).get();
        var maxX = visited.stream().map(l -> l.x).max(Integer::compareTo).get();
        var minY = visited.stream().map(l -> l.y).min(Integer::compareTo).get();
        var maxY = visited.stream().map(l -> l.y).max(Integer::compareTo).get();

        System.out.printf("%s, %s, %s, %s\n", minX, maxX, minY, maxY);

        printVisited(visited, minX, maxX, minY, maxY, head, tail);

    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day9.txt"));

        var moves = lines.stream()
                .map(l -> l.split(" "))
                .map(a -> new Move(a[0], Integer.parseInt(a[1])))
                .toList();

        var visited = new HashSet<Location>();

        var segments = new HashMap<Integer, Location>();
        for (var i = 0; i < 10; i++) {
            segments.put(i, new Location(0, 0));
        }
        visited.add(segments.get(9));

        //printVisited(visited, 0, 4, -3, 0, head, tail);
        for (var move : moves) {
            //System.out.printf("%s\n", move);
            for (var i = 0; i < move.count; i++) {
                var newLocation = segments.get(0).move(move.direction);
                segments.put(0, newLocation);
                //System.out.println(segments.get(0));

                for (var c = 1; c < 10; c++) {
                    var knot = segments.get(c);
                    //System.out.printf("(%s) %s -> (%s) %s\n", c, knot, c-1, segments.get(c-1));
                    knot = knot.catchUp(segments.get(c-1));
                    segments.put(c, knot);
                }
                //System.out.printf("head: %s, tail: %s\n", head, tail);
                visited.add(segments.get(9));
                //printVisited(visited, 0, 4, -3, 0, head, tail);
            }
        }
        System.out.println(visited.size());
        //printVisited(visited, head, tail);
    }
}
