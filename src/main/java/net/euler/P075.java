package net.euler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Set;

import static net.euler.MathUtils.gcd;

/**
 * It turns out that 12 cm is the smallest length of wire that can be bent to form an integer sided right angle triangle
 * in exactly one way, but there are many more examples.
 *
 * 12 cm: (3,4,5)
 * 24 cm: (6,8,10)
 * 30 cm: (5,12,13)
 * 36 cm: (9,12,15)
 * 40 cm: (8,15,17)
 * 48 cm: (12,16,20)
 *
 * In contrast, some lengths of wire, like 20 cm, cannot be bent to form an integer sided right angle triangle, and
 * other lengths allow more than one solution to be found; for example, using 120 cm it is possible to form exactly
 * three different integer sided right angle triangles.
 *
 * 120 cm: (30,40,50), (20,48,52), (24,45,51)
 *
 * Given that L is the length of the wire, for how many values of L ≤ 1,500,000 can exactly one integer sided right
 * angle triangle be formed?
 *
 * @author Kevin Crosby
 */
public class P075 {
  private static Primes primes = Primes.getInstance();

  // generalized Euclid's formula
  private static Triple<Integer, Integer, Integer> triple(int k, int m, int n) {
    return Triple.of(k * (m * m - n * n), 2 * k * m * n, k * (m * m + n * n));
  }

  private static Integer perimeter(int k, int m, int n) {
    return 2 * k * m * (m + n);
  }

  public static void main(String[] args) {
    int maxPerimeter;
    if (args.length > 0) {
      maxPerimeter = Integer.parseInt(args[0]);
    } else {
      maxPerimeter = 1000;
    }

    // generate all Pythagorean triples with perimeter up to max perimeter
    Multimap<Integer, Triple<Integer, Integer, Integer>> map = ArrayListMultimap.create();
    int maxSemiPerimeter = maxPerimeter / 2;
    for (int k = 1; k <= maxSemiPerimeter / 2; k++) { // k = 1 produces primitive Pythagorean triples (k = gcd(a, c))
      for (int m = 2; m < maxSemiPerimeter / k; m++) {
        for (int n = 1 + m % 2; n < m; n += 2) { // force m - n to be odd
          int p = perimeter(k, m, n);
          if (gcd(m, n) != 1 || p > maxPerimeter) {
            continue;
          }
          map.put(p, triple(k, m, n));
        }
      }
    }

    int maxSolutions = 0;
    int bestPerimeter = 0;
    for (int p : Sets.newTreeSet(map.keySet())) {
      Set<Triple<Integer, Integer, Integer>> triples = Sets.newTreeSet(map.get(p));
      if (triples.size() > maxSolutions) {
        maxSolutions = triples.size();
        bestPerimeter = p;
      }
      System.out.println(p + ": " + Sets.newTreeSet(map.get(p)));
    }
    System.out.println();
    System.out.println("The best value of p ≤ " + maxPerimeter + ", is " + bestPerimeter + ", where the number of solutions maximised (" + maxSolutions + ").");
  }
}
