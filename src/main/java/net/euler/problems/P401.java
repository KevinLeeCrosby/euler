package net.euler.problems;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static net.euler.utils.MathUtils.sqrt;

/**
 * The divisors of 6 are 1,2,3 and 6.
 * The sum of the squares of these numbers is 1+4+9+36=50.
 *
 * Let sigma2(n) represent the sum of the squares of the divisors of n. Thus sigma2(6)=50.
 *
 * Let SIGMA2 represent the summatory function of sigma2, that is SIGMA2(n)=∑sigma2(i) for i=1 to n.
 * The first 6 values of SIGMA2 are: 1,6,16,37,63 and 113.
 *
 * Find SIGMA2(10^15) modulo 10^9.
 *
 * @author Kevin Crosby.
 */
public class P401 {
  private static final BigInteger SIX = BigInteger.valueOf(6);

  private static final long MODULUS = 1_000_000_000L;
  private static final BigInteger MOD = BigInteger.valueOf(MODULUS);

  private P401() {
  }

  private long sumsq(final long m, final long n) {
    return (sumsq(n) - sumsq(m) + MODULUS) % MODULUS;
  }

  private long sumsq(final long n) {
    return sumsq(BigInteger.valueOf(n));
  }

  private long sumsq(final BigInteger n) {
    return n
        .multiply(n.add(ONE))
        .multiply(n.shiftLeft(1).add(ONE))
        .divide(SIX)
        .mod(MOD)
        .longValue();
  }

  private long sigma2(final long n) {
    long root = sqrt(n);
    long sum = 0;
    for (long i = 1, o = 1, s = 1, c = n % MODULUS;
         i <= n / (root + 1);
         i++, o += 2, s = (s + o) % MODULUS, c = n / i % MODULUS) {
      sum = (sum + s * c % MODULUS) % MODULUS;
    }

    for (long i = root; i >= 1; i--) {
      sum = (sum + i * sumsq(n / (i + 1), n / i) % MODULUS) % MODULUS;
    }
    return sum;
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 1_000_000_000_000_000L; // 281632621

    P401 problem = new P401();
    System.out.format("SIGMA2(%d) is: %d\n", limit, problem.sigma2(limit));
  }
}
