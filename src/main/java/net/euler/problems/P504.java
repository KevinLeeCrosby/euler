package net.euler.problems;

import com.google.common.collect.Sets;

import java.util.Set;

import static net.euler.utils.MathUtils.gcd;

/**
 * Let ABCD be a quadrilateral whose vertices are lattice points lying on the coordinate axes as follows:
 *
 * A(a, 0), B(0, b), C(−c, 0), D(0, −d), where 1 ≤ a, b, c, d ≤ m and a, b, c, d, m are integers.
 *
 * It can be shown that for m = 4 there are exactly 256 valid ways to construct ABCD. Of these 256 quadrilaterals, 42
 * of them strictly contain a square number of lattice points.
 *
 * How many quadrilaterals ABCD strictly contain a square number of lattice points for m = 100?
 *
 * @author Kevin Crosby
 */
public class P504 {
  private final Set<Integer> squares;

  private P504(final int n) {
    squares = Sets.newHashSet();
    for(int o = 1, s = 1; s < 2 * n * n; o += 2, s += o) {
      squares.add(s);
    }
  }

  private int twiceArea(final int a, final int b, final int c, final int d) {
    return (a + c) * (b + d);
  }

  private int boundary(final int a, final int b, final int c, final int d) {
    int[] p = {a, b, c, d, a};
    int count = 0;
    for(int i = 0; i < 4; ++i) {
      count += (int) gcd(p[i], p[i + 1]);
    }
    return count;
  }

  private int interior(final int a, final int b, final int c, final int d) {
    return (twiceArea(a, b, c, d) - boundary(a, b, c, d)) / 2 + 1;
  }

  private boolean isSquare(final int n) {
    return squares.contains(n);
  }

  public static void main(String[] args) {
    final int m = args.length > 0 ? Integer.parseInt(args[0]) : 100;

    P504 p = new P504(m);

    int count = 0;
    for(int a = 1; a <= m; ++a) {
      for(int b = 1; b <= m; ++b) {
        for(int c = 1; c <= m; ++c) {
          for(int d = 1; d <= m; ++d) {
            // Pick's theorem Area = interior + boundary / 2 - 1
            if(p.isSquare(p.interior(a, b, c, d))) {
              count++;
            }
          }
        }
      }
    }

    System.out.format("There are %d quadrilaterals ABCD that strictly contain a square number of lattice points for m = %d.", count, m);
  }
}
