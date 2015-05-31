package net.euler.problems;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * By counting carefully it can be seen that a rectangular grid measuring 3 by 2 contains eighteen rectangles:
 *
 * Although there exists no rectangular grid that contains exactly two million rectangles, find the area of the grid
 * with the nearest solution.
 *
 * @author Kevin Crosby
 */
public class P085 {
  public static long triangle(final long n) {
    return n * (n + 1) / 2;
  }


  public static void main(String[] args) {
    final long limit = args.length > 0 ? Integer.parseInt(args[0]) : 2000000L;

    final long a = (long) pow(4 * limit, 0.25);

    long bestM = 0, bestN = 0, bestRectangles = 0, bestDifference = Long.MAX_VALUE;

    for (long m = a / 2; m <= 2 * a; m++) {
      long p = triangle(m);
      for (long n = m; n <= 2 * a; n++) {
        long q = triangle(n);
        long rectangles = p * q;
        long difference = abs(limit - rectangles);
        if (difference < bestDifference) {
          bestDifference = difference;
          bestM = m;
          bestN = n;
          bestRectangles = rectangles;
          System.out.println(m + " x " + n + " has " + rectangles + " rectangles.");
        }
      }
    }
    System.out.println("Best area is " + bestM + " x " + bestN + " with area " + bestM * bestN +
        " and " + bestRectangles + " rectangles.");
  }
}
