package src.main.java;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Day15Part2 {
    public record Position(int x, int y) {
    }

    public static class Sensor {
        private final Position p;
        private final Integer d;

        public Sensor(Position _p, Integer _d) {
            p = _p;
            d = _d;
        }

        public Interval projectionAt(Integer y) {
            var w = d - abs(p.y - y);
            if (w <= 0) return null;
            return new Interval(p.x - w, p.x + w);
        }
    }

    public static class Sensors {
        private final ArrayList<Sensor> sensors;

        public Sensors(ArrayList<Sensor> _sensors) {
            sensors = _sensors;
        }

        public boolean isCoveredAt(Integer y) {
            var collapsed = Interval.collapse(
                    sensors.stream()
                            .map(s -> s.projectionAt(y))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
            return collapsed.size() == 1;
        }

        public Integer getUncoveredAt(Integer y) {
            var collapsed = Interval.collapse(
                    sensors.stream()
                            .map(s -> s.projectionAt(y))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
            if (collapsed.size() > 1) {
                System.out.printf("collapsed to: %s\n", collapsed);
                return collapsed.get(0).max + 1;
            }
            return null;
        }
    }

    private record Interval(Integer min, Integer max) {
        public static ArrayList<Interval> collapse(ArrayList<Interval> intervals) {
            intervals.sort(Comparator.comparing(a -> a.min));
            var ret = new ArrayList<Interval>();

            Interval current = null;
            for (var i : intervals) {
                if (current == null) {
                    current = i;
                    continue;
                }

                if (i.min <= current.max) {
                    if (i.max > current.max) {
                        current = new Interval(current.min, i.max);
                    }
                    continue;
                }
                ret.add(current);
                current = i;
            }
            ret.add(current);
            return ret;
        }
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day15.txt"));
        var pattern = Pattern.compile("Sensor at x=([-\\d]+), y=([-\\d]+): closest beacon is at x=([-\\d]+), y=([-\\d]+)");

        var sensorList = new ArrayList<Sensor>();

        for (var l : lines) {
            var m = pattern.matcher(l);
            m.find();
            var x1 = Integer.parseInt(m.group(1));
            var y1 = Integer.parseInt(m.group(2));
            var x2 = Integer.parseInt(m.group(3));
            var y2 = Integer.parseInt(m.group(4));
            var d = abs(x1 - x2) + abs(y1 - y2);

            sensorList.add(new Sensor(new Position(x1, y1), d));
        }

        var sensors = new Sensors(sensorList);

        var max = 4000000;
        for (var y = 0; y <= max; y++) {
            if (y % 1000 == 0) System.out.printf("y: %s\n", y);
            if (!sensors.isCoveredAt(y)) {
                var x = sensors.getUncoveredAt(y);
                var answer = new BigInteger(String.valueOf(x)).multiply(BigInteger.valueOf(4000000)).add(BigInteger.valueOf(y));
                System.out.printf("beacon is at %s,%s:%s\n", x, y, answer);
                return;
            }
        }
    }
}
