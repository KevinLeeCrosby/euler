package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.Primes;

import java.math.BigInteger;
import java.util.Set;

/**
 * A unit fraction contains 1 in the numerator. The decimal representation of the unit fractions with denominators 2 to
 * 10 are given:
 *
 * 1/2  = 0.5
 * 1/3  = 0.(3)
 * 1/4  = 0.25
 * 1/5  = 0.2
 * 1/6  = 0.1(6)
 * 1/7  = 0.(142857)
 * 1/8  = 0.125
 * 1/9  = 0.(1)
 * 1/10 = 0.1
 * Where 0.1(6) means 0.166666..., and has a 1-digit recurring cycle. It can be seen that 1/7 has a 6-digit recurring
 * cycle.
 *
 * Find the value of d < 1000 for which 1/d contains the longest recurring cycle in its decimal fraction part.
 *
 * @author Kevin Crosby
 */
public class P026 {
  private static final Primes P = Primes.getInstance();

  private static long order(long a, long n) { // for a = 10, called "digital logarithm"
    if (isTerminating(n)) {
      return 0;
    }
    BigInteger base = BigInteger.valueOf(a);
    BigInteger modulus = BigInteger.valueOf(n);
    long k = 1;
    BigInteger t = base;
    while (!t.mod(modulus).equals(BigInteger.ONE)) {
      k++;
      t = t.multiply(base);
    }
    return k;
  }

  private static boolean isFullRepetendPrime(long p) {
    return P.isPrime(p) && order(10L, p) == p - 1L;
  }

  private static boolean isTerminating(long n) {
    Set<Long> factors = Sets.newHashSet(P.factor(n));
    factors.removeAll(Sets.newHashSet(2L, 5L));
    return factors.isEmpty();
  }

  private static long period(long n) { // a.k.a. repetend length
    if (isTerminating(n)) {
      return 0;
    }
    BigInteger en = BigInteger.valueOf(n);
    BigInteger p1 = BigInteger.ONE;
    for (long e1 = 1; ; e1++) {
      BigInteger p2 = p1;
      p1 = p1.multiply(BigInteger.TEN);
      for (long e2 = e1 - 1; e2 >= 0; e2--) {
        if (p1.subtract(p2).mod(en).equals(BigInteger.ZERO)) {
          return e1 - e2;
        }
        p2 = p2.divide(BigInteger.TEN);
      }
    }
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 1000L;

    long period = 0;
    long d = 1;

    int i = 0;
    long n = -1;
    while (n < limit) {
      n = P.get(i++);
      if (!isFullRepetendPrime(n)) {
        continue;
      }
      long t = n - 1;
      if (t > period) {
        period = t;
        d = n;
        System.out.println("Found 1/" + d + " with a period of " + period);
      }
    }
    System.out.println("The value of d < " + limit
        + " for which 1/d contains the longest recurring cycle in its decimal fraction part is " + d
        + " with period of " + period);
  }
}
