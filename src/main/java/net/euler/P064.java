package net.euler;

import static net.euler.MathUtils.sqrt;

/**
 * All square roots are periodic when written as continued fractions and can be written in the form:
 *
 * √N = a0 + (1 / (a1 + (1 / (a2 + (1 / a3 + ...
 *
 * For example, let us consider √23:
 * √23 = 4 + √23 - 4 = 4 + (1 / (1 / (√23 - 4)) = 4 + (1 / (1 + ((√23 - 3) / 7)).
 *
 * It can be seen that the sequence is repeating. For conciseness, we use the notation √23 = [4;(1,3,1,8)], to indicate
 * that the block (1,3,1,8) repeats indefinitely.
 *
 * The first ten continued fraction representations of (irrational) square roots are:
 *
 * √2=[1;(2)], period=1
 * √3=[1;(1,2)], period=2
 * √5=[2;(4)], period=1
 * √6=[2;(2,4)], period=2
 * √7=[2;(1,1,1,4)], period=4
 * √8=[2;(1,4)], period=2
 * √10=[3;(6)], period=1
 * √11=[3;(3,6)], period=2
 * √12= [3;(2,6)], period=2
 * √13=[3;(1,1,1,1,6)], period=5
 *
 * Exactly four continued fractions, for N ≤ 13, have an odd period.
 *
 * How many continued fractions for N ≤ 10000 have an odd period?
 *
 * @author Kevin Crosby
 */
public class P064 {
  // algorithm adapted from http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Continued_fraction_expansion
  private static int period(final long n) {
    long a0 = sqrt(n);
    int period = 0;
    if (a0 * a0 == n) return period;  // i.e. is a square
    long a = a0, m = 0, d = 1;
    while (a != 2 * a0) {
      m = a * d - m;
      d = (n - m * m) / d;
      a = (a0 + m) / d;
      period++;
    }
    return period;
  }

  private static boolean isOddPeriod(final long n) {
    return period(n) % 2 == 1;
  }

  public static void main(String[] args) {
    final int LIMIT = args.length > 0 ? Integer.parseInt(args[0]) : 10000;

    int count = 0;
    for (long n = 1; n <= LIMIT; n++) {
      long a0 = sqrt(n);
      if (a0 * a0 == n) { continue; }
      System.out.println("√" + n + " has period " + period(n));
      if (isOddPeriod(n)) { count++; }
    }

    System.out.println("There are " + count + " continued fractions for N ≤ 10000 with an odd period.");
  }
}
