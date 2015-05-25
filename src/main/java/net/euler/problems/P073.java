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
 * It can be seen that there are 3 fractions between 1/3 and 1/2.
 *
 * How many fractions lie between 1/3 and 1/2 in the sorted set of reduced proper fractions for d ≤ 12,000?
 *
 * @author Kevin Crosby
 */
public class P073 {
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
    final int n = args.length > 0 ? Integer.parseInt(args[0]) : 12000;

    // Farey sequence
    long a = 1, b = 2, c = 1, d = 3;  // i.e. 1/3 and 1/2
    long k = (n + b) / d;
    a = k * c - a; // i.e. new fraction a/b immediately to left of 1/3.
    b = k * d - b;
    //List<String> fractions = Lists.newArrayList("... ");

    long count = -1;
    while (c != 1 || d != 2) {
      k = (n + b) / d;
      long p = c, q = d;
      c = k * c - a;
      d = k * d - b;
      a = p;
      b = q;
      //fractions.add(a + "/" + b);
      count++;
    }
    //fractions.add(c + "/" + d);
    //System.out.println("Found " + Joiner.on(", ").join(fractions) + ", ...");

    System.out.println("There are " + count + " fractions that lie between 1/3 and 1/2 in the sorted set of reduced proper fractions for d ≤ " + n);
  }
}
