package net.euler.problems;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * It is easily proved that no equilateral triangle exists with integral length sides and integral area. However, the
 * almost equilateral triangle 5-5-6 has an area of 12 square units.
 *
 * We shall define an almost equilateral triangle to be a triangle for which two sides are equal and the third differs
 * by no more than one unit.
 *
 * Find the sum of the perimeters of all almost equilateral triangles with integral side lengths and area and whose
 * perimeters do not exceed one billion (1,000,000,000).
 *
 * @author Kevin Crosby
 */
public class P094 {
  // See: http://www.had2know.com/academics/nearly-equilateral-heronian-triangles.html (A103974, A103975)
  // Isosceles Heronian triangles

  private static List<Long> A103974 = Lists.newArrayList(5L, 65L, 901L);
  private static List<Long> A103975 = Lists.newArrayList(16L, 240L, 3360L);

  private static long v(final int n) {
    if (n >= A103974.size()) {
      A103974.add(15 * (v(n - 1) - v(n - 2)) + v(n - 3));
    }
    return A103974.get(n);
  }

  private static long w(final int n) {
    if (n >= A103975.size()) {
      A103975.add(15 * (w(n - 1) - w(n - 2)) + w(n - 3));
    }
    return A103975.get(n);
  }

  private static long sumPerimeters(final long maxPerimeter) {
    long sum = 0, perimeter = 3 * v(0) + 1;
    for (int i = 0; perimeter < maxPerimeter; perimeter = 3 * v(++i) + 1) {
      sum += perimeter;
    }
    perimeter = 3 * w(0) + 2;
    for (int i = 0; perimeter < maxPerimeter; perimeter = 3 * w(++i) + 2) {
      sum += perimeter;
    }

    return sum;
  }

  public static void main(String[] args) {
    final long LIMIT = args.length > 0 ? Long.parseLong(args[0]) : 1000000000;

    System.out.println("The sum of the perimeters of all almost equilateral triangles with integral side lengths and area and whose perimeters do not exceed "
        + LIMIT + " is " + sumPerimeters(LIMIT));
  }
}
