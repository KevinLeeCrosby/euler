package net.euler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by kevin on 11/19/14.
 *
 * Collatz Conjecture
 */
public class P014 {
  private static final long limit = 1000000L;

  private static long next(long n) {
    if (n <= 1) {
      return -1L;
    } else if (n % 2 == 0) { // if even
      return n / 2;
    } else { // if odd
      return 3 * n + 1;
    }
  }

  public static void main(String[] args) {
    int maxLength = 0;
    List<Long> bestSequence = Lists.newArrayList(1L);

    for (long number = limit - 1; number > 1; number--) {
      List<Long> sequence = Lists.newArrayList();
      long n = number;
      do {
        sequence.add(n);
        n = next(n);
      } while (n >= 1);
      int length = sequence.size();
      if (length > maxLength) {
        maxLength = length;
        bestSequence = sequence;
        System.out.println("Found starting number " + number + " with chain length of " + length);
      }
    }

    //System.out.println(Joiner.on(" ->\n").join(bestSequence));
    System.out.println("Starting number " + bestSequence.get(0) + " produces the longest chain of length of " + maxLength);
  }
}
