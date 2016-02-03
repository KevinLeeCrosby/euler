package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Collections;
import java.util.List;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static net.euler.utils.MathUtils.log2;
import static net.euler.utils.MathUtils.pow;

/**
 * If we calculate a^2 mod 6 for 0 ≤ a ≤ 5 we get: 0,1,4,3,4,1.
 *
 * The largest value of a such that a^2 ≡ a mod 6 is 4.
 * Let's call M(n) the largest value of a < n such that a^2 ≡ a (mod n).
 * So M(6) = 4.
 *
 * Find ∑M(n) for 1 ≤ n ≤ 10^7.
 *
 * @author Kevin Crosby
 */
public class P407 {
  private static NewPrimes primes;

  private static boolean isPrimePower(final long n) {
    if(primes.isPrime(n)) {
      return true;
    }
    for(long k = 2; k <= log2(n); ++k) {
      long m = (long) floor(Math.pow(n, 1.0 / k));
      if(primes.isPrime(m) && pow(m, k) == n) {
        return true;
      }
    }
    return false;
  }

  private static long largest(final long n) {
    assert n > 0 : "Number must be positive!";
    List<Long> factors = primes.factor(n);
    long pk = 0;
    for(final long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      pk = max(pk, pow(factor, exponent));
    }
    for(long u = n / pk * pk; u >= n / 2; u -= pk) {
      if(u == n) {
        continue;
      }
      if((u * (u + 1)) % n == 0) {
        return u + 1;
      } else if((u * (u - 1)) % n == 0) {
        return u;
      }
    }
    return 1;
  }

  private static long m(final long n) {
    if(n == 1) {
      return 0;
    }
    if(isPrimePower(n)) {
      return 1;
    }
    return largest(n);
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 10000000L;
    primes = NewPrimes.getInstance(limit);

    long sum = 0;
    for(long n = 1; n <= limit; ++n) {
      long m = m(n);
      sum += m;
    }
    System.out.format("Sum of M(n) for 1 <= n <= %d is %d.\n", limit, sum);
  }
}
