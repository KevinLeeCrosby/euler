package net.euler;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by kevin on 11/21/14.
 *
 * Collatz Conjecture
 */
public class P014p1 {
  private static final long limit = 1000000L;

  private static Map<Long, Integer> lengths = Maps.newHashMap();

  private static long next(final long n) {
    if (n % 2 == 0) { // if even
      return n / 2;
    } else { // if odd
      return 3 * n + 1;
    }
  }

  private static int count(final long n) {
    if (!lengths.containsKey(n)) {
      lengths.put(n, count(next(n)) + 1);
    }
    return lengths.get(n);
  }

  public static void main(String[] args) {
    long bestNumber = 1L;
    int maxLength = 0;
    lengths.put(1L, 1); // initialize

    for (long n = limit - 1; n > 1; n--) {
      int length = count(n);
      if (length > maxLength) {
        maxLength = length;
        bestNumber = n;
        System.out.println("Found starting number " + n + " with chain length of " + length);
      }
    }

    System.out.println("Starting number " + bestNumber + " produces the longest chain of length of " + maxLength);
  }
}
