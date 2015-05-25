package net.euler.problems;

import java.util.HashMap;
import java.util.Map;

/**
 * Consider the fraction, n/d, where n and d are positive integers. If n<d and HCF(n,d)=1, it is called a reduced
 * proper
 * fraction.
 *
 * If we list the set of reduced proper fractions for d ≤ 8 in ascending order of size, we get:
 *
 * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
 *
 * It can be seen that there are 21 elements in this set.
 *
 * How many elements would be contained in the set of reduced proper fractions for d ≤ 1,000,000?
 *
 * @author Kevin Crosby
 */
public class P072p1 {
  private static Map<Long, Long> lengths = new HashMap<Long, Long>() {{ put(1L, 2L); }};

  private static long length(final long n) {
    if (!lengths.containsKey(n)) {
      long length = (n + 3) * n / 2;
      for (long d = 2; d <= n; d++) {
        length -= length(n / d);
      }
      lengths.put(n, length);
    }
    return lengths.get(n);
  }

  public static void main(String[] args) {
    final int n = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;
    long length = length(n) - 2;

    System.out.println("There are " + length + " elements contained in the set of reduced proper fractions for d ≤ " + n);
  }
}
