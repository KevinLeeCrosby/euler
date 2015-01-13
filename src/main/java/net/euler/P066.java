package net.euler;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static net.euler.MathUtils.sqrt;

/**
 * Consider quadratic Diophantine equations of the form:
 *
 * x^2 – Dy^2 = 1
 *
 * For example, when D=13, the minimal solution in x is 649^2 – 13×180^2 = 1.
 *
 * It can be assumed that there are no solutions in positive integers when D is square.
 *
 * By finding minimal solutions in x for D = {2, 3, 5, 6, 7}, we obtain the following:
 *
 * 3^2 – 2×2^2 = 1
 * 2^2 – 3×1^2 = 1
 * 9^2 – 5×4^2 = 1
 * 5^2 – 6×2^2 = 1
 * 8^2 – 7×3^2 = 1
 *
 * Hence, by considering minimal solutions in x for D ≤ 7, the largest x is obtained when D=5.
 *
 * Find the value of D ≤ 1000 in minimal solutions of x for which the largest value of x is obtained.
 *
 * @author Kevin Crosby
 */
public class P066 {
  private static Pair<BigInteger, BigInteger> getFundamentalSolution(final int n) { // TODO:  switch to BigInteger
    long a0 = sqrt(n);
    if (a0 * a0 == n) {
      return null;  // i.e. is a square
    }
    long a = a0, m = 0, d = 1;
    BigInteger hm1 = ONE, hm2 = ZERO, h; // numerators
    BigInteger km1 = ZERO, km2 = ONE, k; // denominators
    do {
      h = BigInteger.valueOf(a).multiply(hm1).add(hm2);
      k = BigInteger.valueOf(a).multiply(km1).add(km2);
      //System.out.println(h + ((k.compareTo(ONE) == 0) ? "" : " / " + k) + "     " + a + ":  (" + m + "," + d + ")");
      m = a * d - m;
      d = (n - m * m) / d;
      a = (a0 + m) / d;
      hm2 = hm1;
      hm1 = h;
      km2 = km1;
      km1 = k;
    } while (h.pow(2).subtract(BigInteger.valueOf(n).multiply(k.pow(2))).compareTo(ONE) != 0);
    return Pair.of(h, k);
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

    BigInteger maxX = ZERO, bestY = ZERO;
    long bestD = 0;
    for (int d = 2; d <= limit; d++) {
      long r = MathUtils.sqrt(d);
      if (r * r == d) { continue; } // skip squares
      Pair<BigInteger, BigInteger> pair = getFundamentalSolution(d);
      BigInteger x = pair.getLeft(), y = pair.getRight();
      System.out.println(x + "^2 – " + d + "×" + y + "^2 = 1");
      if (maxX.compareTo(x) == -1) {
        maxX = x;
        bestY = y;
        bestD = d;
      }
    }
    System.out.println();

    System.out.println("The value of D ≤ " + limit + " in minimal solutions of x for which the largest value of x is obtained is " + bestD);
    System.out.println(maxX + "^2 – " + bestD + "×" + bestY + "^2 = 1");
  }
}
