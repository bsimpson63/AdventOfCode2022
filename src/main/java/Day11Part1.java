package src.main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11Part1 {

    public static class Monkey {
        private static final Pattern p = Pattern.compile("Monkey (\\d):\\n\\s\\sStarting items: ([\\d, ]+)\\n\\s\\sOperation: new = old ([\\+\\*] [\\dold]+)\\n\\s\\sTest: divisible by ([\\d]+)\\n\\s\\s\\s\\sIf true: throw to monkey (\\d)\\n\\s\\s\\s\\sIf false: throw to monkey (\\d)");
        public Integer count = 0;
        Integer id;
        List<Integer> items;
        Operation op;
        Test test;

        Monkey(Integer _id, List<Integer> _items, Operation _op, Test _test) {
            id = _id;
            items = _items;
            op = _op;
            test = _test;
        }

        interface Operation {
            public Integer transform(Integer original);
        }

        public record Add(Integer x) implements Operation {
            @Override
            public Integer transform(Integer original) {
                return original + x;
            }
        }

        public record Multiply(Integer x) implements Operation {
            @Override
            public Integer transform(Integer original) {
                return original * x;
            }
        }

        public record MultiplyIdentity() implements Operation {
            @Override
            public Integer transform(Integer original) {
                return original * original;
            }
        }

        public record Test(Integer divisible, Integer passToIfTrue, Integer passToIfFalse) {
            public Integer passTo(Integer n) {
                if (n % divisible == 0) {
                    return passToIfTrue;
                } else {
                    return passToIfFalse;
                }
            }
        }

        public static Monkey fromString(String s) {
            var m = p.matcher(s);
            m.find();
            var id = Integer.parseInt(m.group(1));
            var items = Arrays.stream(m.group(2).split(", ")).map(Integer::parseInt).collect(Collectors.toList());
            var expression = m.group(3).split(" ");

            Operation op;
            if (expression[0].equals("+")) {
                op = new Add(Integer.parseInt(expression[1]));
            } else if (expression[1].equals("old")) {
                op = new MultiplyIdentity();
            } else {
                op = new Multiply(Integer.parseInt(expression[1]));
            }

            var test = new Test(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
            return new Monkey(id, items, op, test);
        }
    }

    public static void printMonkeys(Integer round, Map<Integer, Monkey> monkeys) {
        System.out.printf("Round %s\n", round);
        for (var i = 0; i < monkeys.size(); i++) {
            var m = monkeys.get(i);
            System.out.printf("Monkey %s turn (%s) count: %s\n", i, m.items, m.count);
        }
    }

    public static void doRound(Map<Integer, Monkey> monkeys) {
        // After each monkey inspects an item but before it tests your worry level, your
        // relief that the monkey's inspection didn't damage the item causes your worry level
        // to be divided by three and rounded down to the nearest integer.

        // The monkeys take turns inspecting and throwing items. On a single monkey's turn,
        // it inspects and throws all of the items it is holding one at a time and in the order
        // listed. Monkey 0 goes first, then monkey 1, and so on until each monkey has had one turn.
        // The process of each monkey taking a single turn is called a round.

        // When a monkey throws an item to another monkey, the item goes on the end of the
        // recipient monkey's list. A monkey that starts a round with no items could end up
        // inspecting and throwing many items by the time its turn comes around. If a monkey
        // is holding no items at the start of its turn, its turn ends.
        for (var i = 0; i < monkeys.size(); i++) {
            var m = monkeys.get(i);
            while (m.items.size() > 0) {
                m.count++;
                var originalWorry = m.items.remove(0);
                var newWorry = m.op.transform(originalWorry);
                var adjustedWorry = newWorry / 3;
                var dest = m.test.passTo(adjustedWorry);
                monkeys.get(dest).items.add(adjustedWorry);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        var text = Files.readString(Path.of("src/main/resources/Day11.txt"));
        var blocks = text.split("\n\n");
        var monkeys = new HashMap<Integer, Monkey>();
        for (String block : blocks) {
            var m = Monkey.fromString(block);
            monkeys.put(m.id, m);
            System.out.println(m);
        }

        printMonkeys(0, monkeys);
        for (var i = 1; i <= 20; i++) {
            doRound(monkeys);
            printMonkeys(i, monkeys);
        }

        var top = monkeys.values().stream()
                .map(m -> m.count)
                .sorted(Collections.reverseOrder())
                .limit(2)
                .toList();
        System.out.println(top.get(0) * top.get(1));

    }


}
