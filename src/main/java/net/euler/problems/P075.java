package net.euler.problems;

import com.google.common.collect.Maps;

import java.util.Map;

import static net.euler.utils.MathUtils.gcd;
import static net.euler.utils.MathUtils.sqrt;

/**
 * It turns out that 12 cm is the smallest length of wire that can be bent to form an integer sided right angle
 * triangle
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
  // generalized Euclid's formula
  //private static Triple<Integer, Integer, Integer> triple(final int k, final int m, final int n) {
  //  return Triple.of(k * (m * m - n * n), 2 * k * m * n, k * (m * m + n * n));
  //}

  private static Integer perimeter(final int k, final int m, final int n) {
    return 2 * k * m * (m + n);
  }

  public static void main(String[] args) {
    final int maxPerimeter = args.length > 0 ? Integer.parseInt(args[0]) : 1500000;

    // generate all Pythagorean triples with perimeter up to max perimeter
    Map<Integer, Integer> map = Maps.newHashMap();
    int maxSemiPerimeter = maxPerimeter / 2;
    for (int m = 2; m < sqrt(maxSemiPerimeter); m++) {
      for (int n = 1 + m % 2; n < m; n += 2) { // force m - n to be odd
        if (gcd(m, n) == 1) {
          int p = perimeter(1, m, n); // k = 1 produces primitive Pythagorean triples (k = gcd(a, c))
          for (int k = 1, perimeter = p; perimeter <= maxPerimeter; k++, perimeter += p) {
            //System.out.println(perimeter + ": " + triple(k, m, n));
            int count = map.containsKey(perimeter) ? map.get(perimeter) : 0;
            map.put(perimeter, ++count);
          }
        }
      }
    }

    int total = 0;
    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
      if (entry.getValue() == 1) {
        total++;
      }
    }

    System.out.println("There are " + total + " values of L ≤ " + maxPerimeter
        + " where exactly one integer sided right angle triangle can be formed.");
  }
}
