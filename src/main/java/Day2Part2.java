package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Day2Part2 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day2.txt"));
        var score = 0;
        for (var l : lines) {
            var tokens = l.split(" ");
            var them = parsePlay(tokens[0]);
            var outcome = parseOutcome(tokens[1]);
            var me = getMove(them, outcome);
            var s = getScore(them, me);
            System.out.printf("%s : %s (%s) -> %s\n", them, me, outcome, s);
            score += s;
        }
        System.out.printf("final: %s\n", score);
    }

    public static Play parsePlay(String p) {
        if (Objects.equals(p, "A")) return Play.ROCK;
        else if (Objects.equals(p, "B")) return Play.PAPER;
        else return Play.SCISSORS;
    }

    public static Outcome parseOutcome(String p) {
         if (Objects.equals(p, "X")) return Outcome.LOSE;
         else if (Objects.equals(p, "Y")) return Outcome.DRAW;
         else return Outcome.WIN;
    }

    public enum Play {
        ROCK,
        PAPER,
        SCISSORS,
    }

    public enum Outcome {
        LOSE,
        DRAW,
        WIN,
    }

    public static Play getMove(Play them, Outcome outcome) {
        if (outcome == Outcome.DRAW) return them;
        else if (outcome == Outcome.LOSE) {
            if (them == Play.ROCK) return Play.SCISSORS;
            else if (them == Play.SCISSORS) return Play.PAPER;
            else return Play.ROCK;
        } else {
            if (them == Play.ROCK) return Play.PAPER;
            else if (them == Play.SCISSORS) return Play.ROCK;
            else return Play.SCISSORS;
        }
    }

    public static int getScore(Play them, Play me) {
        var score = 0;
        if (me == Play.ROCK) score += 1;
        else if (me == Play.PAPER) score += 2;
        else score += 3;

        if (them == me) {
            score += 3;
            return score;
        }

        if (them == Play.ROCK && me == Play.PAPER
                || them == Play.PAPER && me == Play.SCISSORS
                || them == Play.SCISSORS && me == Play.ROCK) {
            score += 6;
            return score;
        }

        return score;
    }
}
