package net.euler.problems;

import net.euler.utils.Primes;

/**
 * Euler's Totient function, φ(n) [sometimes called the phi function], is used to determine the number of numbers less
 * than n which are relatively prime to n. For example, as 1, 2, 4, 5, 7, and 8, are all less than nine and relatively
 * prime to nine, φ(9)=6.
 *
 * n  Relatively Prime  φ(n)  n/φ(n)
 * 2  1                 1     2
 * 3  1,2               2     1.5
 * 4  1,3               2     2
 * 5  1,2,3,4           4     1.25
 * 6  1,5               2     3
 * 7  1,2,3,4,5,6       6     1.1666...
 * 8  1,3,5,7           4     2
 * 9  1,2,4,5,7,8       6     1.5
 * 10 1,3,7,9           4     2.5
 *
 * It can be seen that n=6 produces a maximum n/φ(n) for n ≤ 10.
 *
 * Find the value of n ≤ 1,000,000 for which n/φ(n) is a maximum.
 *
 * @author Kevin Crosby
 */
public class P069p1 {
  private static Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;

    primes.generate(limit);

    long primorial = 1;
    for (long prime : primes) {
      if (primorial * prime > limit) break;
      primorial *= prime;
    }

    System.out.println("The value of n ≤ " + limit + " for which n/φ(n) is a maximum is " + primorial);
  }
}
