import os
import os.path
import json
import functools

def compare(l1, l2):
    if isinstance(l1, list) and isinstance(l2, list):
        for i1, i2 in zip(l1 ,l2):
            c = compare(i1, i2)
            if c != 0:
                return c

        if len(l1) == len(l2):
            return 0

        if len(l2) > len(l1):
            # l1 ran out of items, l2 is bigger
            return -1

        # l2 ran out of items, l1 is bigger
        return 1

    if isinstance(l1, list) and isinstance(l2, int):
        return compare(l1, [l2])

    if isinstance(l1, int) and isinstance(l2, list):
        return compare([l1], l2)

    if isinstance(l1, int) and isinstance(l2, int):
        if l1 == l2:
            return 0
        if l1 < l2:
            return -1
        return 1


def part2():
    with open(os.path.join(os.path.dirname(__file__), "Day13.txt")) as f:
        d = f.read().strip()

    packets = list(map(json.loads, filter(None, d.split('\n'))))
    packets.append([[2]])
    packets.append([[6]])
    s = sorted(packets, key=functools.cmp_to_key(compare))
    for i, p in enumerate(s):
        print(f"{i+1}: {p}")
    i1 = s.index([[2]]) + 1
    i2 = s.index([[6]]) + 1
    print(f"{i1} * {i2} = {i1*i2}")

def part1():
    with open(os.path.join(os.path.dirname(__file__), "Day13.txt")) as f:
        d = f.read().strip()

    parts = d.split('\n\n')
    s = 0
    for i, p in enumerate(parts):
        l1, l2 = map(json.loads, p.split('\n'))
        if compare(l1, l2) == -1: s += i + 1
        print(f"{i+1}: {l1} vs {l2} - {compare(l1, l2)}")

    print(s)


if __name__ == "__main__":
    part2()