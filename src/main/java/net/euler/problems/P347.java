package net.euler.problems;

import com.google.common.collect.Iterables;
import net.euler.utils.NewPrimes;

import static net.euler.utils.MathUtils.logBase;
import static net.euler.utils.MathUtils.pow;
import static net.euler.utils.MathUtils.sqrt;

/**
 * The largest integer ≤ 100 that is only divisible by both the primes 2 and 3 is 96, as 96=32*3=2^5*3. For two
 * distinct primes p and q let M(p,q,N) be the largest positive integer ≤N only divisible by both p and q and
 * M(p,q,N)=0 if such a positive integer does not exist.
 *
 * E.g. M(2,3,100)=96.
 * M(3,5,100)=75 and not 90 because 90 is divisible by 2, 3 and 5.
 * Also M(2,73,100)=0 because there does not exist a positive integer ≤ 100 that is divisible by both 2 and 73.
 *
 * Let S(N) be the sum of all distinct M(p,q,N). S(100)=2262.
 *
 * Find S(10 000 000).
 *
 * @author Kevin Crosby
 */
public class P347 {
  private static NewPrimes primes;

  private static long m(final long p, final long q, final long n) {
    if(p == q || !primes.isPrime(p) || !primes.isPrime(q) || p * q > n) {
      return 0;
    }
    long maximum = 0;
    for(long powP = p, b = logBase(n / powP, q), powQ = pow(q, b); n / powP > 1 && b > 0; powP *= p, b = logBase(n / powP, q), powQ = pow(q, b)) {
      long product = powP * powQ;
      if(maximum < product) {
        maximum = product;
      }
    }
    return maximum;
  }

  private static long s(final long n) {
    int i = 0;
    long root = sqrt(n), sum = 0;
    for(long p : primes) {
      if(p > root) break;
      for(long q : Iterables.skip(primes, ++i)) {
        if(p * q > n) break;
        sum += m(p, q, n);
      }
    }
    return sum;
  }

  public static void main(String[] args) {
    long limit = args.length > 0 ? Long.parseLong(args[0]) : 10000000;
    primes = NewPrimes.getInstance(limit / 2);

    System.out.format("S(%d) is %d.\n", limit, s(limit));
  }
}
