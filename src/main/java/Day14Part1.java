package src.main.java;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Day14Part1 {
    public record Position(Integer x, Integer y) {
        public static Position fromString(String s) {
            var nums = Arrays.stream(s.split(",")).map(Integer::valueOf).toList();
            return new Position(nums.get(0), nums.get(1));
        }

        @Override
        public boolean equals(Object other){
            if((other == null) || (getClass() != other.getClass())){
                return false;
            }
            final Position otherPosition = (Position) other;
            return Objects.equals(this.x, otherPosition.x) && Objects.equals(this.y, otherPosition.y);
        }
    }

    public static class Grid {
        private final HashMap<Position, String> grid = new HashMap<>();
        private Integer minX = null;
        private Integer minY = null;
        private Integer maxX = null;
        private Integer maxY = null;

        public Grid(List<String> lines) {
            for (var l : lines) {
                var points = Arrays.stream(l.split(" -> "))
                        .map(Position::fromString).toList();

                var p = points.get(0);
                grid.put(p, "#");
                for (var i = 1; i < points.size(); i++) {
                    var dx = -p.x.compareTo(points.get(i).x);
                    var dy = -p.y.compareTo(points.get(i).y);

                    while (!p.equals(points.get(i))) {
                        p = new Position(p.x + dx, p.y + dy);
                        grid.put(p, "#");
                    }
                    p = points.get(i);
                }
            }

            for (var p: grid.keySet()) {
                if (minX == null) {
                    minX = p.x;
                    maxX = p.x;
                    minY = p.y;
                    maxY = p.y;
                    continue;
                }

                if (p.x < minX) minX = p.x;
                if (p.x > maxX) maxX = p.x;
                if (p.y < minY) minY = p.y;
                if (p.y > maxY) maxY = p.y;
            }
        }

        private @Nullable Position getSandPosition() {
            /*
            A unit of sand always falls down one step if possible. If the tile immediately
            below is blocked (by rock or sand), the unit of sand attempts to instead move
            diagonally one step down and to the left. If that tile is blocked, the unit of
            sand attempts to instead move diagonally one step down and to the right. Sand keeps
            moving as long as it is able to do so, at each step trying to move down, then
            down-left, then down-right. If all three possible destinations are blocked,
            the unit of sand comes to rest and no longer moves
             */
            int x = 500;
            int y = 0;

            while (true) {
                if (y > maxY) return null;

                if (!grid.containsKey(new Position(x, y+1))) {
                    y++;
                    continue;
                }

                if (!grid.containsKey(new Position(x-1, y+1))) {
                    y++;
                    x--;
                    continue;
                }

                if (!grid.containsKey(new Position(x+1, y+1))) {
                    y++;
                    x++;
                    continue;
                }
                return new Position(x, y);
            }
        }
        public boolean dropSand() {
            var p = getSandPosition();
            if (p == null) {
                return false;
            }
            grid.put(p, "o");
            return true;
        }

        public void printGrid() {
            for (var y = minY; y <= maxY; y++) {
                for (var x = minX; x <= maxX; x++) {
                    System.out.printf("%s", grid.getOrDefault(new Position(x, y), "."));
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day14.txt"));

        var grid = new Grid(lines);
        grid.printGrid();
        int c = 0;
        while (true) {
            var success = grid.dropSand();
            if (!success) {
                break;
            }

            c++;
            System.out.printf("Step %s\n", c);
            grid.printGrid();
        }
    }
}
