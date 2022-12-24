package src.main.java;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16Part2 {
    public record Valve(String id, Integer flow, String[] outputs) {
    }

    public record StateAndMax(State state, Integer maxPossibleScore) {
    }

    public static class State {
        private final Integer minute;
        private final String position;
        private final String positionE;
        private final Integer score;
        private final HashMap<String, Boolean> open;

        public State(HashMap<String, Valve> valves) {
            minute = 1;
            position = "AA";
            positionE = "AA";
            score = 0;
            open = new HashMap<>();
            for (var valve : valves.keySet()) {
                open.put(valve, false);
            }
        }

        public String hashKey() {
            var s = position + "|" + positionE + "|" + String.valueOf(score) + "|";
            return s + open.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(e -> e.getValue() ? "t" : "f")
                    .reduce("", String::concat);
        }

        private State(Integer _minute, String _position, String _positionE, Integer _score, HashMap<String, Boolean> _open) {
            minute = _minute;
            position = _position;
            positionE = _positionE;
            score = _score;
            open = _open;
        }

        public State step(Step s, Step e, HashMap<String, Valve> valves) {
            var newOpen = new HashMap<>(open);
            var newScore = score;
            var remaining = 26 - minute;
            var newPosition = position;
            var newPositionE = positionE;

            if (s instanceof Open o) {
                if (!newOpen.get(o.valve)) {
                    newOpen.put(o.valve, true);
                    newScore += valves.get(o.valve).flow * remaining;
                }
            }

            if (e instanceof Open o) {
                if (!newOpen.get(o.valve)) {
                    newOpen.put(o.valve, true);
                    newScore += valves.get(o.valve).flow * remaining;
                }
            }

            if (s instanceof Move m) {
                newPosition = m.valve;
            }

            if (e instanceof Move m) {
                newPositionE = m.valve;
            }

            return new State(minute + 1, newPosition, newPositionE, newScore, newOpen);
        }

        public Integer maxPossibleScore(HashMap<String, Valve> valves) {
            var flows = open.entrySet().stream()
                    .map(e -> e.getValue() ? 0 : valves.get(e.getKey()).flow)
                    .filter(f -> f > 0)
                    .sorted(Comparator.reverseOrder())
                    .toList();
            var maxScore = score;
            for (var i = 0; i < flows.size(); i++) {
                maxScore += (26 - minute - (i / 2) * 2) * flows.get(i);
            }
            return maxScore;
        }
    }

    public interface Step {
    }

    public record Move(String valve) implements Step {
    }

    public record Open(String valve) implements Step {
    }

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

        var seenAtMinute = new HashMap<String, Integer>();

        stack.add(new StateAndMax(start, start.maxPossibleScore(valves)));
        var best = 1647; //part1
        var c = BigInteger.valueOf(0);
        while (stack.size() > 0) {
            c = c.add(BigInteger.valueOf(1));
            if (c.mod(BigInteger.valueOf(10000)).equals(BigInteger.valueOf(0)))
                System.out.printf("stack (%s), best %s\n", stack.size(), best);

            // prune from the low scoring end of the stack
            int finalBest = best;
            stack = stack.stream()
                    .filter(e -> e.maxPossibleScore > finalBest)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (stack.size() == 0) break;

            stack.sort(Comparator.comparingInt(e -> -e.state.score));

            var s = stack.remove(0);

            if (seenAtMinute.containsKey(s.state.hashKey()) && seenAtMinute.get(s.state.hashKey()) <= s.state.minute)
                continue;
            seenAtMinute.put(s.state.hashKey(), s.state.minute);

            if (s.state.minute > 26) {
                continue;
            }

            if (s.state.score > best) {
                best = s.state.score;
            } else if (s.maxPossibleScore <= best) {
                continue;
            }

            if (Objects.equals(s.maxPossibleScore, s.state.score)) continue;

            var mySteps = new ArrayList<Step>();
            var elSteps = new ArrayList<Step>();

            if (!s.state.open.get(s.state.position) && valves.get(s.state.position).flow > 0) {
                mySteps.add(new Open(s.state.position));
            }

            if (!s.state.open.get(s.state.positionE) && valves.get(s.state.positionE).flow > 0) {
                elSteps.add(new Open(s.state.positionE));
            }

            for (var p : valves.get(s.state.position).outputs) {
                mySteps.add(new Move(p));
            }

            for (var p : valves.get(s.state.positionE).outputs) {
                elSteps.add(new Move(p));
            }

            for (var my : mySteps) {
                for (var el : elSteps) {
                    if (my instanceof Open && el instanceof Open && ((Open) my).valve == ((Open) el).valve) continue;
                    var nextStep = s.state.step(my, el, valves);
                    stack.add(new StateAndMax(nextStep, nextStep.maxPossibleScore(valves)));
                }
            }
        }
        System.out.printf("best score: %s, (%s evaluations)\n", best, c);
    }
}
