package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class Day16Part1 {
    public record Valve(String id, Integer flow, String[] outputs) {}

    public record StateAndMax(State state, Integer maxPossibleScore) {}

    public static class State {
        private final Integer minute;
        private final String position;
        private final Integer score;
        private final HashMap<String, Boolean> open;

        public State(HashMap<String, Valve> valves) {
            minute = 1;
            position = "AA";
            score = 0;
            open = new HashMap<>();
            for (var valve : valves.keySet()) {
                open.put(valve, false);
            }
        }

        private State(Integer _minute, String _position, Integer _score, HashMap<String, Boolean> _open) {
            minute = _minute;
            position = _position;
            score = _score;
            open = _open;
        }

        public State step(Step s, HashMap<String, Valve> valves) {
            if (s instanceof Open o) {
                var remaining = 30 - minute;
                var newOpen = new HashMap<>(open);
                newOpen.put(o.valve, true);
                return new State(minute + 1, position, score + valves.get(o.valve).flow * remaining, newOpen);
            }
            var m = (Move) s;
            return new State(minute + 1, m.valve, score, new HashMap<>(open));
        }

        public Integer maxPossibleScore(HashMap<String, Valve> valves) {
            var flows = open.entrySet().stream()
                    .map(e -> e.getValue() ? 0 : valves.get(e.getKey()).flow)
                    .filter(f -> f > 0)
                    .sorted(Comparator.reverseOrder())
                    .toList();
            var maxScore = score;
            for (var i = 0; i < flows.size(); i++) {
                maxScore += (30 - minute - i * 2) * flows.get(i);
            }
            return maxScore;
        }
    }

    public interface Step {}

    public record Move(String valve) implements Step {}

    public record Open(String valve) implements Step {}

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day16.txt"));
        var pattern = Pattern.compile("Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]+)");

        var valves = new HashMap<String, Valve>();
        for (var l : lines) {
            var m = pattern.matcher(l);
            m.find();
            var v = m.group(1);
            var f = Integer.parseInt(m.group(2));
            var outputs = m.group(3).split((", "));
            valves.put(v, new Valve(v, f, outputs));
        }

        var stack = new ArrayList<StateAndMax>();
        var start = new State(valves);

        stack.add(new StateAndMax(start, start.maxPossibleScore(valves)));
        var best = 0;
        var c = 0;
        while (stack.size() > 0) {
            c += 1;
            if (c % 10000 == 0) System.out.printf("stack (%s), best %s\n", stack.size(), best);

            stack.sort(Comparator.comparingInt(e -> e.maxPossibleScore));

            var s = stack.remove(0);

            if (s.state.minute > 30) {
                continue;
            }

            if (s.state.score > best) {
                best = s.state.score;
            } else if (s.maxPossibleScore <= best) {
                continue;
            }

            if (Objects.equals(s.maxPossibleScore, s.state.score)) continue;

            if (!s.state.open.get(s.state.position) && valves.get(s.state.position).flow > 0) {
                var nextStep = s.state.step(new Open(s.state.position), valves);
                stack.add(new StateAndMax(nextStep, nextStep.maxPossibleScore(valves)));
            }

            for (var p : valves.get(s.state.position).outputs) {
                var nextStep = s.state.step(new Move(p), valves);
                stack.add(new StateAndMax(nextStep, nextStep.maxPossibleScore(valves)));
            }
        }
        System.out.printf("best score: %s\n", best);
    }
}
