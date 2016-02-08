package net.euler.problems;

import net.euler.utils.Counter;

import java.util.Map.Entry;

/**
 * Many numbers can be expressed as the sum of a square and a cube. Some of them in more than one way.
 *
 * Consider the palindromic numbers that can be expressed as the sum of a square and a cube, both greater than 1, in
 * exactly 4 different ways.
 *
 * For example, 5229225 is a palindromic number and it can be expressed in exactly 4 different ways:
 *
 * 2285^2 + 20^3
 * 2223^2 + 66^3
 * 1810^2 + 125^3
 * 1197^2 + 156^3
 *
 * Find the sum of the five smallest such palindromic numbers.
 *
 * @author Kevin Crosby
 */
public class P348 {
  private static boolean isPalindrome(final long n) {
    long r = 0;
    for(long d = n; d > 0; d /= 10) {
      r = r * 10 + d % 10;
    }
    return r == n;
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 1000000000L;


    Counter<Long> counter = new Counter<>();
    for(long om = 1, sm = 1; sm < limit; om += 2, sm += om) {  // m = (om + 1) / 2
      for(long n = 1, dn = 1, cn = 1, number = sm + cn; number < limit; dn += 6 * n++, cn += dn, number = sm + cn) {
        if(isPalindrome(number)) {
          counter.increment(number);
        }
      }
    }
    long sum = counter.descendingSortByCount().stream().filter(e -> e.getValue() == 4).limit(5).mapToLong(Entry::getKey).sum();

    System.out.format("The sum of the five smallest palindromic numbers that can be expressed in 4 different ways is %d.\n", sum);
  }
}
