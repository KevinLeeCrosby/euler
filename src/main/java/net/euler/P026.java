package net.euler;

import com.google.common.collect.Sets;

import java.math.BigInteger;
import java.util.Set;

/**
 * Created by Kevin on 11/28/2014.
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
    long limit;
    if (args.length > 0) {
      limit = Long.parseLong(args[0]);
    } else {
      limit = 1000;
    }

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
