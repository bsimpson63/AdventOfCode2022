package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Day3Part1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day3.txt"));
        var s = 0;
        for (var l : lines) {
            var one = new HashSet<Character>();
            var two = new HashSet<Character>();

            for (var i = 0; i < l.length(); i++) {
                var c = l.charAt(i);

                if (i < l.length() / 2) {
                    one.add(c);
                } else {
                    two.add(c);
                }
            }

            var intersection = new HashSet<>(one);
            intersection.retainAll(two);
            var common = intersection.stream().toList().get(0);
            var o = getPriority(common);
            s += o;
        }
        System.out.println(s);
    }

    public static int getPriority(char c) {
        int o;
        if (Character.isUpperCase(c)) {
            o = (int) c - (int) 'A' + 1 + 26;
        } else {
            o = (int) c - (int) 'a' + 1;
        }
        return o;
    }
}
