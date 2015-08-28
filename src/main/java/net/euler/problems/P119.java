package net.euler.problems;


import com.google.common.collect.Iterables;

import java.util.Set;
import java.util.TreeSet;

import static net.euler.utils.MathUtils.logBase;
import static net.euler.utils.MathUtils.sqrt;

/**
 * The number 512 is interesting because it is equal to the sum of its digits raised to some power: 5 + 1 + 2 = 8, and
 * 8^3 = 512. Another example of a number with this property is 614656 = 28^4.
 *
 * We shall define an to be the nth term of this sequence and insist that a number must contain at least two digits to
 * have a sum.
 *
 * You are given that a2 = 512 and a10 = 614656.
 *
 * Find a30.
 *
 * @author Kevin Crosby
 */
public class P119 {
  private static boolean isDigitPowerSum(final long base, final long power) {
    long n = power, sum = 0;
    while (n > 0) {
      sum += n % 10;
      n /= 10;
    }
    return sum == base;
  }

  public static void main(String[] args) {
    final int N = args.length > 0 ? Integer.parseInt(args[0]) : 30;
    Set<Long> set = new TreeSet<Long>() {{
      add(1L);
    }};

    for (long base = 2, power = base * base; base < sqrt(Integer.MAX_VALUE); ++base, power = base * base) {
      for (int exponent = 2; exponent <= logBase(Long.MAX_VALUE, base); ++exponent, power *= base) {
        if (isDigitPowerSum(base, power)) set.add(power);
      }
    }

    int i = 0;
    for (final long power : set) {
      System.out.printf("a%d : %d\n", i++, power);
    }

    System.out.printf("\na%d is %d.\n", N, Iterables.get(set, N));
  }
}