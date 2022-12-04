package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class Day3Part2 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day3.txt"));
        var s = 0;
        var counts = new HashMap<Integer, HashSet<Character>>();
        for (var i = 0; i < 3; i++) {
            counts.put(i, new HashSet<>());
        }
        var w = 0;
        for (var l : lines) {
            for (var i = 0; i < l.length(); i++) {
                var c = l.charAt(i);
                counts.get(w).add(c);
            }
            if (w == 2) {
                var intersection = new HashSet<>(counts.get(0));
                intersection.retainAll(counts.get(1));
                intersection.retainAll(counts.get(2));

                var common = intersection.stream().toList().get(0);
                var o = getPriority(common);
                s += o;

                w = 0;
                counts.get(0).clear();
                counts.get(1).clear();
                counts.get(2).clear();
            } else {
                w += 1;
            }
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
