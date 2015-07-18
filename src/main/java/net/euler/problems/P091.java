package net.euler.problems;

import static net.euler.utils.MathUtils.gcd;

/**
 * The points P (x1, y1) and Q (x2, y2) are plotted at integer co-ordinates and are joined to the origin, O(0,0), to
 * form ΔOPQ.
 *
 * There are exactly fourteen triangles containing a right angle that can be formed when each co-ordinate lies between
 * 0 and 2 inclusive; that is, 0 ≤ x1, y1, x2, y2 ≤ 2.
 *
 * Given that 0 ≤ x1, y1, x2, y2 ≤ 50, how many right triangles can be formed?
 *
 * @author Kevin Crosby
 */
public class P091 {
  public static void main(String[] args) {
    final int SIZE = args.length > 0 ? Integer.parseInt(args[0]) : 50;

    int count = 3 * SIZE * SIZE;  // for right angles aligned with axes
    for(int x = 1; x <= SIZE; ++x) {
      for(int y = 1; y <= SIZE; ++y) {
        int gcd = (int)gcd(x, y), dx = x / gcd, dy = y / gcd;
        count += Math.min((SIZE - y) / dx, x / dy) * 2;
      }
    }

    System.out.println("Given that 0 ≤ x1, y1, x2, y2 ≤ " + SIZE + ", there are "+ count +" right triangles that can be formed.");
  }
}
