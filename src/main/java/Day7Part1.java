package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day7Part1 {
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

        var s = 0;
        for (var e : directories.entrySet()) {
            if (e.getValue() <= 100000) {
                s += e.getValue();
            }
        }
        System.out.println(s);
    }
}
