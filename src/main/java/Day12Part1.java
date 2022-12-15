package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Day12Part1 {
    public record Position(Integer x, Integer y) {
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day12.txt"));
        var grid = new HashMap<Position, Character>();

        for (var y = 0; y < lines.size(); y++) {
            for (var x = 0; x < lines.get(y).length(); x++) {
                grid.put(new Position(x, y), lines.get(y).charAt(x));
            }
        }

        var starts = grid.entrySet().stream()
                .filter(e -> e.getValue().equals('S'))
                .map(e -> e.getKey())
                .toList();

        var end = grid.entrySet().stream()
                .filter(e -> e.getValue().equals('E'))
                .findFirst()
                .get()
                .getKey();

        System.out.printf("end is %s\n", end);

        Integer shortestPath = null;
        for (var start : starts) {
            var s = new Solver(grid);
            s.step(start, 0);

            if (shortestPath == null || s.getShortestPath(end) < shortestPath) {
                shortestPath = s.getShortestPath(end);
            }
            System.out.printf("Shortest path: %s\n", shortestPath);
        }



    }

    public static class Solver {
        HashMap<Position, Integer> shortestPath;
        HashMap<Position, Character> grid;

        Solver(HashMap<Position, Character> _grid) {
            shortestPath = new HashMap<>();
            grid = _grid;
        }

        public Integer getShortestPath(Position p) {
            return shortestPath.get(p);
        }

        public void step(Position p, Integer steps) {
            // don't bother stepping if we have seen how to get to that position
            // in fewer or same number of steps
            if (shortestPath.containsKey(p) && shortestPath.get(p) < steps) {
                return;
            }
            shortestPath.put(p, steps);

            if (grid.get(p).equals('E')) {
                //System.out.printf("path is %s steps\n", steps);
                return;
            }

            //System.out.printf("seen %s positions\n", shortestPath.size());

            var options = List.of(
                    new Position(p.x + 1, p.y),
                    new Position(p.x - 1, p.y),
                    new Position(p.x, p.y + 1),
                    new Position(p.x, p.y - 1));

            for (var option : options) {
                if (!grid.containsKey(option)) continue;

                if (shortestPath.containsKey(option) && shortestPath.get(option) <= steps + 1) continue;

                var cur = grid.get(p);
                var next = grid.get(option);

                var hCur = (int) cur;
                if (cur.equals('S')) {
                    hCur = (int) 'a';
                }

                var hNext = (int) next;
                if (next.equals('E')) {
                    hNext = (int) 'z';
                }

                if (hNext - hCur > 1) continue;

                step(option, steps + 1);
            }
        }
    }
}
