package net.euler.problems;

import java.math.BigInteger;

import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;
import static net.euler.utils.MathUtils.sqrt;

/**
 * It is well known that if the square root of a natural number is not an integer, then it is irrational. The decimal
 * expansion of such square roots is infinite without any repeating pattern at all.
 *
 * The square root of two is 1.41421356237309504880..., and the digital sum of the first one hundred decimal digits is
 * 475.
 *
 * For the first one hundred natural numbers, find the total of the digital sums of the first one hundred decimal
 * digits for all the irrational square roots.
 *
 * @author Kevin Crosby
 */
public class P080 {
  private static long sumDigits(final BigInteger number, final int limit) {
    BigInteger n = new BigInteger(number.toString().substring(0, limit));
    long sum = 0;
    while (n.compareTo(ZERO) == 1) {
      BigInteger[] divMod = n.divideAndRemainder(TEN);
      n = divMod[0];
      sum += divMod[1].longValue();
    }
    return sum;
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 100;
    final int digits = args.length > 1 ? Integer.parseInt(args[1]) : 100;

    BigInteger multiplier = TEN.pow(2 * digits);
    long sum = 0;
    for (int n = 2; n <= limit; n++) {
      long r = sqrt(n);
      if (r * r == n) { continue; }
      BigInteger product = multiplier.multiply(BigInteger.valueOf(n));
      BigInteger root = sqrt(product);
      sum += sumDigits(root, digits);
    }

    System.out.println("For the first " + limit + " natural numbers, the total of the digital sums of the first "
        + digits + " decimal digits for all the irrational square roots is " + sum);
  }
}
