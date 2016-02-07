package net.euler.problems;

import com.google.common.collect.Sets;

import java.util.Set;

import static net.euler.utils.MathUtils.sqrt;

/**
 * The palindromic number 595 is interesting because it can be written as the sum of consecutive squares: 6^2 + 7^2 +
 * 8^2 + 9^2 + 10^2 + 11^2 + 12^2.
 *
 * There are exactly eleven palindromes below one-thousand that can be written as consecutive square sums, and the sum
 * of these palindromes is 4164. Note that 1 = 0^2 + 1^2 has not been included as this problem is concerned with the
 * squares of positive integers.
 *
 * Find the sum of all the numbers less than 10^8 that are both palindromic and can be written as the sum of
 * consecutive squares.
 *
 * @author Kevin Crosby
 */
public class P125 {
  private static boolean isPalindrome(final long n) {
    long r = 0;
    for(long d = n; d > 0; d /= 10) {
      r = r * 10 + d % 10;
    }
    return r == n;
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 100000000;

    Set<Long> set = Sets.newHashSet();
    for(long om = 1, sm = 1; om + 1 < 2 * sqrt(limit); om += 2, sm += om) {  // m = (om + 1) / 2
      for(long on = om + 2, sn = sm + on, number = sm + sn; number < limit; on += 2, sn += on, number += sn) { // n = (on + 1) / 2
        if(isPalindrome(number)) {
          set.add(number);
        }
      }
    }
    long sum = set.stream().mapToLong(Long::longValue).sum();

    System.out.format("The sum of all the numbers less than %d that are both palindromic and can be written as the sum of consecutive squares is %d.\n", limit, sum);
  }
}
