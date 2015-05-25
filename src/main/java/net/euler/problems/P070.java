package net.euler.problems;

import com.google.common.collect.Lists;
import net.euler.utils.Primes;

import java.util.List;

import static java.lang.Double.POSITIVE_INFINITY;
import static net.euler.utils.MathUtils.log10;
import static net.euler.utils.MathUtils.sqrt;

/**
 * Euler's Totient function, φ(n) [sometimes called the phi function], is used to determine the number of positive
 * numbers less than or equal to n which are relatively prime to n. For example, as 1, 2, 4, 5, 7, and 8, are all less
 * than nine and relatively prime to nine, φ(9)=6.
 * The number 1 is considered to be relatively prime to every positive number, so φ(1)=1.
 *
 * Interestingly, φ(87109)=79180, and it can be seen that 87109 is a permutation of 79180.
 *
 * Find the value of n, 1 < n < 10^7, for which φ(n) is a permutation of n and the ratio n/φ(n) produces a minimum.
 *
 * @author Kevin Crosby
 */
public class P070 {
  private static final Primes PRIMES = Primes.getInstance();

  private static int hashCode(long number) {
    int hash = 1;
    while (number > 0) {
      int digit = new Long(number % 10).intValue();
      hash *= PRIMES.get(digit);
      number /= 10;
    }
    return hash;
  }

  private static boolean isPermutation(final long a, final long b) {
    return log10(a) == log10(b) && hashCode(a) == hashCode(b);
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 10000000;
    PRIMES.generate(limit);

    final long root = sqrt(limit);
    final double bound = Math.pow(limit, 2.0 / 3.0); // TODO:  find better bound?

    long bestSemiprime = 0;
    long bestΦ = 0;
    double bestRatio = POSITIVE_INFINITY;

    List<Long> primes = Lists.newLinkedList();
    primes.add(2L);
    for (final long q : PRIMES) {
      if (q > bound) break;
      if (q == 2) continue;
      for (final long p : primes) {
        if (p > root) break;
        long semiprime = p * q;
        if (semiprime > limit) break;
        long φ = (p - 1) * (q - 1);
        double ratio = (double) semiprime / φ;
        if (bestRatio > ratio && isPermutation(semiprime, φ)) {
          System.out.println(String.format("%d\t%d\t%.6f", semiprime, φ, ratio));
          bestRatio = ratio;
          bestSemiprime = semiprime;
          bestΦ = φ;
        }
      }
      primes.add(q);
      if (primes.size() > 1000) {
        primes.remove(0);
      }
    }

    System.out.println("The value of n, 1 < n < " + limit
        + ", for which φ(n) is a permutation of n and the ratio n/φ(n) produces a minimum is φ(" + bestSemiprime + ") = " + bestΦ);
  }
}
