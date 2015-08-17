package net.euler.problems;

import com.google.common.collect.Lists;

import java.util.List;

import static net.euler.utils.MathUtils.modPow;

/**
 * @author Kevin.
 */
public class P111 {
  private static final long PRIMALITY_LIMIT = 341550071728321L;
  private static final List<Long> BASE_PRIMES = Lists.newArrayList(2L,3L,5L,7L,11L,13L,17L,19L,23L);

  /**
   * Test if a number is prime using the Miller-Rabin Primality Test, which is guaranteed to correctly distinguish
   * composites and primes up to 341,550,071,728,321 using the first 9 prime numbers.
   *
   * @param n Number to be tested.
   * @return True only if prime.
   */
  public static boolean isPrime(final long n) {
    if (n > PRIMALITY_LIMIT) {
      System.err.println("WARNING!  Primality check not guaranteed for number " + n);
    }
    if (BASE_PRIMES.contains(n)) {
      return true;
    }
    if (n < 2) {
      return false;
    }
    long d = n - 1;
    int s = 0;
    while (d % 2 == 0) {
      d >>= 1;
      s++;
    }
    for (final long a : BASE_PRIMES) {
      if (modPow(a, d, n) != 1) {
        boolean composite = true;
        for (long r = 0, p = 1; r < s; r++, p <<= 1) { // p = 2^r
          if (modPow(a, p * d, n) == n - 1) {
            composite = false;
            break; // inconclusive
          }
        }
        if (composite) {
          return false;
        }
      }
    }
    return true;
  }
}
