package net.euler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Set;

import static net.euler.MathUtils.gcd;

/**
 * If p is the perimeter of a right angle triangle with integral length sides, {a,b,c}, there are exactly three
 * solutions for p = 120.
 *
 * {20,48,52}, {24,45,51}, {30,40,50}
 *
 * For which value of p ≤ 1000, is the number of solutions maximised?
 *
 * @author Kevin Crosby
 */
public class P039 {
  // generalized Euclid's formula
  private static Triple<Integer, Integer, Integer> triple(int k, int m, int n) {
    return Triple.of(k * (m * m - n * n), 2 * k * m * n, k * (m * m + n * n));
  }

  private static Integer perimeter(int k, int m, int n) {
    return 2 * k * m * (m + n);
  }

  public static void main(String[] args) {
    final int maxPerimeter = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

    // generate all Pythagorean triples with perimeter up to max perimeter
    Multimap<Integer, Triple<Integer, Integer, Integer>> map = ArrayListMultimap.create();
    int maxSemiPerimeter = maxPerimeter / 2;
    for (int k = 1; k <= maxSemiPerimeter / 2; k++) { // k = 1 produces primitive Pythagorean triples (k = gcd(a, c))
      for (int m = 2; m < maxSemiPerimeter / k; m++) {
        for (int n = 1 + m % 2; n < m; n += 2) { // force m - n to be odd
          int perimeter = perimeter(k, m, n);
          if (gcd(m, n) != 1 || perimeter > maxPerimeter) {
            continue;
          }
          map.put(perimeter, triple(k, m, n));
        }
      }
    }

    int maxSolutions = 0;
    int bestPerimeter = 0;
    for (int perimeter : Sets.newTreeSet(map.keySet())) {
      Set<Triple<Integer, Integer, Integer>> triples = Sets.newTreeSet(map.get(perimeter));
      if (triples.size() > maxSolutions) {
        maxSolutions = triples.size();
        bestPerimeter = perimeter;
      }
      System.out.println(perimeter + ": " + Sets.newTreeSet(map.get(perimeter)));
    }
    System.out.println();
    System.out.println("The best value of p ≤ " + maxPerimeter + ", is " + bestPerimeter + ", where the number of solutions maximised (" + maxSolutions + ").");
  }
}
