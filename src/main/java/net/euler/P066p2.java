package net.euler;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

import static java.lang.Math.abs;
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
public class P066p2 {
  private static final Primes primes = Primes.getInstance();

  private static Pair<BigInteger, BigInteger> chakravala(final long n) {
    long m = 1, k = 1, root = sqrt(n);
    BigInteger a0 = ONE, b0 = ZERO, a = ZERO, b = ZERO;

    while (k != 1 || b.compareTo(ZERO) == 0) {
      m = k * (m / k + 1) - m;
      m = m - ((m - root) / k) * k;

      a = (BigInteger.valueOf(m).multiply(a0).add(BigInteger.valueOf(n).multiply(b0))).divide(BigInteger.valueOf(abs(k)));
      b = (BigInteger.valueOf(m).multiply(b0).add(a0)).divide(BigInteger.valueOf(abs(k)));
      k = (m * m - n) / k;
      a0 = a;
      b0 = b;
    }
    return Pair.of(a, b);
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
    primes.generate(limit);

    BigInteger maxX = ZERO, bestY = ZERO;
    long bestD = 0;
    for (long d : primes) {
      if (d > limit) { break; }
      Pair<BigInteger, BigInteger> pair = chakravala(d);
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
