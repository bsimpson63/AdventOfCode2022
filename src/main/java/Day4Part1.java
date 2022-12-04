package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day4Part1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day4.txt"));
        var c = 0;
        for (var l : lines) {
            var pair = l.split(",");
            var one = Arrays.stream(pair[0].split("-")).map(Integer::valueOf).toList();
            var two = Arrays.stream(pair[1].split("-")).map(Integer::valueOf).toList();

            if (overlapCompletely(one, two)) c += 1;
        }
        System.out.println(c);
    }

    public static boolean overlapCompletely(List<Integer> one, List<Integer> two) {
        return one.get(0) <= two.get(0) && one.get(1) >= two.get(1)
                || two.get(0) <= one.get(0) && two.get(1) >= one.get(1);
    }
}
