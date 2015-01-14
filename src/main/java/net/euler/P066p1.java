package net.euler;

import static net.euler.MathUtils.sqrt;

/**
 * Consider quadratic Diophantine equations of the form:
 *
 * x^2 – Dy^2 = 1
 *
 * For example, when D=13, the minimal solution in x is 649^2 – 13×180^2 = 1.
 *
 * It can be assumed that there are no solutions in positive integers when D is square.
 *
 * By finding minimal solutions in x for D = {2, 3, 5, 6, 7}, we obtain the following:
 *
 * 3^2 – 2×2^2 = 1
 * 2^2 – 3×1^2 = 1
 * 9^2 – 5×4^2 = 1
 * 5^2 – 6×2^2 = 1
 * 8^2 – 7×3^2 = 1
 *
 * Hence, by considering minimal solutions in x for D ≤ 7, the largest x is obtained when D=5.
 *
 * Find the value of D ≤ 1000 in minimal solutions of x for which the largest value of x is obtained.
 *
 * @author Kevin Crosby
 */
public class P066p1 {
  private static final Primes primes = Primes.getInstance();

  // algorithm adapted from http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Continued_fraction_expansion
  private static long period(final long n) {
    long a0 = sqrt(n);
    long period = 0;
    if (a0 * a0 == n) {
      return period;  // i.e. is a square
    }
    long a = a0, m = 0, d = 1;
    while (a != 2 * a0) {
      m = a * d - m;
      d = (n - m * m) / d;
      a = (a0 + m) / d;
      period++;
    }
    return period;
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
    primes.generate(limit);

    long maxPeriod = 0;
    long bestD = 0;
    for (long d : primes) {
      if (d > limit) { break; }
      long period = period(d);
      if (period % 2 == 1) {
        period *= 2;
      }
      if (maxPeriod <= period) {
        maxPeriod = period;
        bestD = d;
      }
    }

    System.out.println("The value of D ≤ " + limit + " in minimal solutions of x for which the largest value of x is obtained is " + bestD);
  }
}
