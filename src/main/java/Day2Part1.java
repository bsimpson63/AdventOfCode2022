package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Day2Part1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day2.txt"));
        var score = 0;
        for (var l : lines) {
            var tokens = l.split(" ");
            var them = parse(tokens[0]);
            var me = parse(tokens[1]);
            var s = getScore(them, me);
            System.out.printf("%s : %s -> %s\n", them, me, s);
            score += s;
        }
        System.out.printf("final: %s\n", score);
    }

    public static Play parse(String p) {
        if (Objects.equals(p, "A") || Objects.equals(p, "X")) return Play.ROCK;
        else if (Objects.equals(p, "B") || Objects.equals(p, "Y")) return Play.PAPER;
        else return Play.SCISSORS;
    }

    public enum Play {
        ROCK,
        PAPER,
        SCISSORS,
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
