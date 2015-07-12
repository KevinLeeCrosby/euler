package net.euler.problems;

import net.euler.utils.Counter;

import java.util.Iterator;
import java.util.Map.Entry;

import static net.euler.utils.MathUtils.gcd;

/**
 * A spider, S, sits in one corner of a cuboid room, measuring 6 by 5 by 3, and a fly, F, sits in the opposite corner.
 * By travelling on the surfaces of the room the shortest "straight line" distance from S to F is 10 and the path is
 * shown on the diagram.
 *
 * However, there are up to three "shortest" path candidates for any given cuboid and the shortest route doesn't always
 * have integer length.
 *
 * It can be shown that there are exactly 2060 distinct cuboids, ignoring rotations, with integer dimensions, up to a
 * maximum size of M by M by M, for which the shortest route has integer length when M = 100. This is the least value
 * of M for which the number of solutions first exceeds two thousand; the number of solutions when M = 99 is 1975.
 *
 * Find the least value of M such that the number of solutions first exceeds one million.
 *
 * @author Kevin Crosby
 */
public class P086 {
  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;

    Counter<Integer> counter = new Counter<>();

    // generate all Pythagorean triples
    for (int m = 2; 2 * m * m < 10000; m++) {
      for (int n = 1 + m % 2; n < m; n += 2) { // force m - n to be odd
        if (gcd(m, n) == 1) {
          int a, b = 0;
          for (int k = 1; b < 5000; k++) {
            a = k * (m * m - n * n);
            b = 2 * k * m * n;
            // int c = k * (m * m + n * n);
            if (a > b) {
              b = (a ^= (b ^= a)) ^ b; // swap
            }
            if (a + 1 > (b + 1) / 2) {
              counter.increment(a, a + 1 - (b + 1) / 2);
            }
            counter.increment(b, a / 2);
          }
        }
      }
    }

    int count = 0, size = 0;
    Iterator<Entry<Integer, Integer>> entries = counter.ascendingSortByKey().iterator();
    while (count <= limit && entries.hasNext()) {
      Entry<Integer, Integer> entry = entries.next();
      size = entry.getKey();
      count += entry.getValue();
    }

    System.out.println("The least value of M such that the number of solutions first exceeds " + limit + " is " + size
        + " with " + count + " solutions");
  }
}
