package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Day5Part1 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/resources/Day5.txt"));
        var stacks = new HashMap<Integer, ArrayList<Character>>();

        var lineNum = 0;
        for (var l : lines) {
            if (l.equals("")) break;

            var i = 0;
            var pos = 1;
            while (i < l.length()) {
                if (l.charAt(i) == '[') {
                    var c = l.charAt(i + 1);
                    stacks.putIfAbsent(pos, new ArrayList<>());
                    stacks.get(pos).add(c);
                }
                i += 4;
                pos += 1;
            }
            lineNum += 1;
        }
        System.out.printf("stopped reading at line %s\n", lineNum);
        System.out.printf("got: %s\n", stacks);

        for (var i = lineNum + 1; i < lines.size(); i++) {
            var l = lines.get(i);
            var parts = l.split(" ");
            var count = Integer.valueOf(parts[1]);
            var from = Integer.valueOf(parts[3]);
            var to = Integer.valueOf(parts[5]);

            for (var j = 0; j < count; j++) {
                var popped = stacks.get(from).remove(0);
                stacks.get(to).add(0, popped);
            }
        }
        System.out.printf("got: %s\n", stacks);

        for (var pos = 1; pos <= 9; pos++) {
            System.out.printf("%s", stacks.get(pos).get(0));
        }
        System.out.println();
    }
}
