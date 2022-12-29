package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day17Part2 {
    public record Position(Integer x, Integer y) {
    }

    public record State(Integer iJet, Integer iRock, Integer storedHeight, String hash) {}

    public record SawAtAndHeight(Long sawAt, Long height) {}

    public static class Cave {
        private ArrayList<ArrayList<Boolean>> cave;
        private Long cleared = 0L;
        private final String pattern;
        private Integer iJet = 0;

        private Long rockCount = 0L;
        private final HashMap<State, SawAtAndHeight> seenStateAt = new HashMap<>();
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

        private State getState() {
            var max = getHeight();
            var filledAtHeight = new HashMap<Integer, Integer>();
            for (var y = max; y >= 0; y--) {
                for (var x = 0; x < 7; x++) {
                    if (filledAtHeight.containsKey(x)) continue;
                    if (cave.get(y).get(x)) {
                        filledAtHeight.put(x, y);
                    }
                }
                if (filledAtHeight.size() == 7) break;
            }
            StringBuilder hash = new StringBuilder();
            for (var x = 0; x < 7; x++) {
                hash.append(filledAtHeight.getOrDefault(x, -1));
                if (x != 6) hash.append("|");
            }
            return new State(iJet, iRock, max, hash.toString());
        }

        public Cave(String s) {
            pattern = s;
            cave = new ArrayList<>();
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

        private Integer getHeight() {
            return cave.size() - 1;
        }

        public Long getTotalHeight() {
            return getHeight() + cleared;
        }

        private boolean isOccupied(Position p) {
            if (p.y > getHeight()) return false;
            return cave.get(p.y).get(p.x);
        }

        private void print(HashSet<Position> falling, int xOffset, int yOffset) {
            var fallingSet = falling.stream()
                    .map(p -> new Position(p.x + xOffset, p.y + yOffset))
                    .collect(Collectors.toSet());
            var max = getHeight();
            for (var f : fallingSet) {
                if (f.y > max) max = f.y;
            }
            for (var y = max; y >= 0; y--) {
                System.out.print("|");
                for (var x = 0; x < 7; x++) {
                    var p = new Position(x, y);
                    if (fallingSet.contains(p)) {
                        System.out.print("@");
                    } else if (isOccupied(p)) {
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
            var floor = getHeight();
            var falling = getRock();
            var xOffset = 2;
            var yOffset = floor + 4;

            rockCount++;

            while (true) {
                //print(falling, xOffset, yOffset);

                // try to move horizontally
                var dx = getJet();
                int finalXOffset = xOffset;
                int finalYOffset = yOffset;

                if (falling.stream().allMatch(p -> p.x + finalXOffset + dx >= 0 && p.x + finalXOffset + dx < 7 && !isOccupied(new Position(p.x + finalXOffset + dx, p.y + finalYOffset)))) {
                    xOffset += dx;
                }

                int finalXOffset2 = xOffset;
                int finalYOffset2 = yOffset;
                // try to move down
                if (falling.stream().allMatch(p -> p.y + finalYOffset2 - 1 >= 0 && !isOccupied(new Position(p.x + finalXOffset2, p.y + finalYOffset2 - 1)))) {
                    yOffset -= 1;
                } else {
                    var maxY = falling.stream()
                            .map(p -> p.y + finalYOffset)
                            .reduce(0, Integer::max);
                    while (getHeight() < maxY) {
                        var row = new ArrayList<Boolean>();
                        row.add(false);
                        row.add(false);
                        row.add(false);
                        row.add(false);
                        row.add(false);
                        row.add(false);
                        row.add(false);
                        cave.add(row);
                    }

                    int finalXOffset3 = xOffset;
                    int finalYOffset3 = yOffset;
                    falling.stream().forEach(p -> cave.get(p.y + finalYOffset3).set(p.x + finalXOffset3, true));

                    // check for tetris
                    var fullRows = falling.stream()
                            .map(p -> p.y + finalYOffset3)
                            .filter(y -> cave.get(y).stream().allMatch((c -> c)))
                            .collect(Collectors.toSet());
                    if (fullRows.size() > 0) {
                        var yClear = Collections.max(fullRows);
                        if (yClear >= 0) {
                            //System.out.printf("tetris at %s\n", yClear);
                            cave.subList(0, yClear + 1).clear();
                        }
                        cleared += yClear + 1;
                    }
                    break;
                }
            }
            var s = getState();
            if (seenStateAt.containsKey(s)) {
                var sawAtAndHeight = seenStateAt.get(s);
                var sawAt = sawAtAndHeight.sawAt;
                var height = sawAtAndHeight.height;
                System.out.printf("Saw %s before (%s ago, delta height: %s)\n", s, (rockCount - sawAt), getTotalHeight() - height);
            }
            seenStateAt.put(s, new SawAtAndHeight(rockCount, getTotalHeight()));
            //print(falling, xOffset, yOffset);
        }
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day17.txt"));
        var cave = new Cave(lines.get(0));

        for (Long i = 0L; i < 1440; i++) {
            if (i % 100000L == 0) {
                System.out.printf("%s: height is %s (stored %s) (states seen: %s)\n", i, cave.getTotalHeight(), cave.getHeight(), cave.seenStateAt.size());
            }
            cave.dropRock();
        }
        System.out.printf("Height: %s\n", cave.getTotalHeight() + 1);
        /*
        repeats every 1720 rocks (adds 2729 to height each cycle)
        1000000000000 / 1720
        M + N * 1720 = 1000000000000

        N = 581_395_348
        M = 1440



        height = height of M + N*2729

        height of M = 2229
        N*2729 = 1586627904692
        height = 1,586,627,906,921
        */
    }
}
