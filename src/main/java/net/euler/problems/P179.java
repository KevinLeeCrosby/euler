package net.euler.problems;

/**
 * Find the number of integers 1 < n < 10^7, for which n and n + 1 have the same number of positive divisors. For
 * example, 14 has the positive divisors 1, 2, 7, 14 while 15 has 1, 3, 5, 15.
 *
 * @author Kevin Crosby
 */
public class P179 {
  private final int limit;
  private final int[] divisors;

  private P179(final int limit) {
    this.limit = limit;
    divisors = new int[limit + 1];
    for(int i = 1; i < limit; ++i) {
      for(int j = i; j <= limit; j += i) {
        ++divisors[j];
      }
    }
  }

  private long count() {
    long count = 0;
    for(int n = 2; n < limit; ++n) {
      if(divisors[n] == divisors[n + 1]) {
        ++count;
      }
    }
    return count;
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 10000000;

    P179 problem = new P179(limit);
    long count = problem.count();

    System.out.format("The number of integers 1 < n < %d, for which n and n + 1 have the same number of positive divisors is %d.\n", limit, count);
  }
}
