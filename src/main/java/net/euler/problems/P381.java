package net.euler.problems;

import com.google.common.collect.Iterables;
import net.euler.utils.NewPrimes;

import java.util.Optional;

/**
 * For a prime p let S(p) = (∑(p-k)!) mod(p) for 1 ≤ k ≤ 5.
 *
 * For example, if p=7,
 * (7-1)! + (7-2)! + (7-3)! + (7-4)! + (7-5)! = 6! + 5! + 4! + 3! + 2! = 720+120+24+6+2 = 872.
 * As 872 mod(7) = 4, S(7) = 4.
 *
 * It can be verified that ∑S(p) = 480 for 5 ≤ p < 100.
 *
 * Find ∑S(p) for 5 ≤ p < 10^8.
 *
 * @author Kevin Crosby
 */
public class P381 {
  private static NewPrimes primes;

  private static long[] update(long[] s, final long quotient) {
    return new long[]{s[1], s[0] - quotient * s[1]};
  }

  private static Optional<Long> invMod(final long a, final long n) {
    long[] t = {0, 1}, r = {n, a};
    while(r[1] != 0) {
      long quotient = r[0] / r[1];
      t = update(t, quotient);
      r = update(r, quotient);
    }
    if(r[0] > 1) {
      return Optional.empty(); // not invertible
    }
    if(t[0] < 0) {
      t[0] += n;
    }
    return Optional.of(t[0]);
  }

  private static long s(final long p) {
    assert primes.isPrime(p) : p + " is NOT prime!";

    long q = 1, sum = p; // Wilson's Theorem (p - 1)! mod p = p - 1  and  (p - 2)! mod p = 1
    for(int k = 2; k < 5; ++k) {
      q = invMod(p - k, p).orElse(0L) * q % p;
      sum = (sum + q) % p;
    }

    return sum;
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 100000000;
    primes = NewPrimes.getInstance(limit);

    long sum = 0;
    for(long p : Iterables.skip(primes, 2)) {
      if(p >= limit) {
        break;
      }
      sum += s(p);
    }
    System.out.format("∑S(p) for 5 ≤ p < %d is %d.\n", limit, sum);
  }
}
