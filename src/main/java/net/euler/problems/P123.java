package net.euler.problems;

import net.euler.utils.NewPrimes;

import static net.euler.utils.MathUtils.pow;

/**
 * Let pn be the nth prime: 2, 3, 5, 7, 11, ..., and let r be the remainder when (pn−1)^n + (pn+1)^n is divided by
 * pn^2.
 *
 * For example, when n = 3, p3 = 5, and 4^3 + 6^3 = 280 ≡ 5 mod 25.
 *
 * The least value of n for which the remainder first exceeds 10^9 is 7037.
 *
 * Find the least value of n for which the remainder first exceeds 10^10.
 *
 * @author Kevin Crosby
 */
public class P123 {
  private static final NewPrimes primes = NewPrimes.getInstance(300000);

  public static void main(String[] args) {
    final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 10;
    long remainder = pow(10, exponent);

    int n = 0;
    for (final long p : primes) {
      if ((++n & 1) == 0) continue; // i.e. is even
      long modulo = 2 * p * n;
      if (modulo > remainder) break;
    }

    System.out.printf("The least value of n for which the remainder first exceeds 10^%d is %d.", exponent, n);
  }
}
