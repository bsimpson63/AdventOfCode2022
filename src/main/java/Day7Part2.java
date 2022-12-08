package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day7Part2 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("src/main/java/Day7.txt"));
        var parsed = lines.stream().map(l -> l.split(" ")).toList();
        var pwd = new ArrayList<>();
        var directories = new HashMap<String, Integer>();

        for (var p : parsed) {
            if (p[0].equals("$")) {
                if (p[1].equals("cd")) {
                    if (p[2].equals("..")) {
                        pwd.remove(pwd.size()-1);
                    } else {
                        pwd.add(p[2]);
                    }
                }
            } else {
                if (p[0].equals("dir")) {
                } else {
                    var size = Integer.valueOf(p[0]);
                    var path = "";
                    for (Object part : pwd) {
                        if (part.equals("/") || path.equals("/")) {
                            path += part;
                        } else {
                            path += "/" + part;
                        }
                        directories.merge(path, size, Integer::sum);
                    }
                }
            }
        }

        var maximum = 70000000 - 30000000;
        var used = directories.get("/");

        var answer = directories.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .filter(e -> used - e.getValue() < maximum)
                .findFirst();
        if (answer.isPresent()) {
            var e = answer.get();
            System.out.printf("Currently using %s\n", used);
            System.out.printf("%s: %s\n", e.getKey(), e.getValue());
            System.out.printf("deleting will leave %s free\n", used - e.getValue());
        }
    }
}
