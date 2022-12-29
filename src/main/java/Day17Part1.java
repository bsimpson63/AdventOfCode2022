package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day17Part1 {
    public record Position(Integer x, Integer y) {
    }

    public static class Cave {
        private HashSet<Position> cave;
        private final String pattern;
        private Integer iJet = 0;

        private final HashSet<Position> LINE = new HashSet<>(
                Arrays.asList(
                        new Position(0, 0),
                        new Position(1, 0),
                        new Position(2, 0),
                        new Position(3, 0)
                )
        );
        private final HashSet<Position> PLUS = new HashSet<>(
                Arrays.asList(
                        new Position(1, 0),
                        new Position(0, 1),
                        new Position(1, 1),
                        new Position(2, 1),
                        new Position(1, 2)
                )
        );
        private final HashSet<Position> ELL = new HashSet<>(
                Arrays.asList(
                        new Position(0, 0),
                        new Position(1, 0),
                        new Position(2, 0),
                        new Position(2, 1),
                        new Position(2, 2)
                )
        );
        private final HashSet<Position> EYE = new HashSet<>(
                Arrays.asList(
                        new Position(0, 0),
                        new Position(0, 1),
                        new Position(0, 2),
                        new Position(0, 3)
                )
        );
        private final HashSet<Position> BOX = new HashSet<>(
                Arrays.asList(
                        new Position(0, 0),
                        new Position(1, 0),
                        new Position(0, 1),
                        new Position(1, 1)
                )
        );

        private final List<HashSet<Position>> ROCKS = Arrays.asList(LINE, PLUS, ELL, EYE, BOX);
        private Integer iRock = 0;

        public Cave(String s) {
            pattern = s;
            cave = new HashSet<>();
        }

        private Integer getJet() {
            var ret = pattern.charAt(iJet) == '<' ? -1 : 1;
            iJet++;
            if (iJet >= pattern.length()) iJet = 0;
            return ret;
        }

        private HashSet<Position> getRock() {
            var ret = ROCKS.get(iRock);
            iRock++;
            if (iRock >= ROCKS.size()) iRock = 0;
            return ret;
        }

        public Integer getHeight() {
            return cave.stream()
                    .map(p -> p.y)
                    .reduce(0, Integer::max) + 1;
        }

        private void print(List<Position> falling) {
            var floor = cave.stream()
                    .map(p -> p.y)
                    .reduce(0, Integer::max);
            for (var f : falling) {
                if (f.y > floor) floor = f.y;
            }
            var fallingSet = new HashSet<>(falling);
            for (var y = floor; y >= 0; y--) {
                System.out.print("|");
                for (var x = 0; x < 7; x++) {
                    var p = new Position(x, y);
                    if (fallingSet.contains(p)) {
                        System.out.print("@");
                    } else if (cave.contains(p)) {
                        System.out.print("#");
                    } else {
                        System.out.print(".");
                    }
                }
                System.out.print("|\n");
            }
            System.out.print("+");
            for (var x = 0; x < 7; x++) {
                System.out.print("-");
            }
            System.out.print("+\n");
        }

        public void dropRock() {
            /*
                The tall, vertical chamber is exactly seven units wide.
                Each rock appears so that its left edge is two units away from the left wall and
                its bottom edge is three units above the highest rock in the room (or the floor,
                if there isn't one).

                After a rock appears, it alternates between being pushed by a jet of hot gas one
                unit (in the direction indicated by the next symbol in the jet pattern) and then
                falling one unit down.
            */

            Integer floor;
            if (cave.size() > 0) {
                floor = cave.stream()
                        .map(p -> p.y)
                        .reduce(0, Integer::max);
            } else {
                floor = -1;
            }

            var rock = getRock();
            var falling = rock.stream()
                    .map(p -> new Position(p.x + 2, p.y + floor + 4))
                    .toList();
            while (true) {
                //print(falling);
                // try to move horizontally
                var dx = getJet();
                if (falling.stream().allMatch(p -> p.x + dx >= 0 && p.x + dx < 7 && !cave.contains(new Position(p.x + dx, p.y)))) {
                    falling = falling.stream().map(p -> new Position(p.x + dx, p.y)).toList();
                }
                // try to move down
                if (falling.stream().allMatch(p -> p.y - 1 >= 0 && !cave.contains(new Position(p.x, p.y - 1)))) {
                    falling = falling.stream().map(p -> new Position(p.x, p.y - 1)).toList();
                } else {
                    cave.addAll(falling);
                    break;
                }
            }
            //print(falling);
        }
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day17.txt"));
        var cave = new Cave(lines.get(0));

        for (var i = 0; i < 2022; i++) cave.dropRock();
        System.out.printf("Height: %s\n", cave.getHeight());
    }
}
