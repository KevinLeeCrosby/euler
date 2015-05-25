package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.MathUtils;

import java.util.Set;

/**
 * Take the number 192 and multiply it by each of 1, 2, and 3:
 *
 * 192 × 1 = 192
 * 192 × 2 = 384
 * 192 × 3 = 576
 * By concatenating each product we get the 1 to 9 pandigital, 192384576. We will call 192384576 the concatenated
 * product of 192 and (1,2,3)
 *
 * The same can be achieved by starting with 9 and multiplying by 1, 2, 3, 4, and 5, giving the pandigital, 918273645,
 * which is the concatenated product of 9 and (1,2,3,4,5).
 *
 * What is the largest 1 to 9 pandigital 9-digit number that can be formed as the concatenated product of an integer
 * with (1,2, ... , n) where n > 1?
 *
 * @author Kevin Crosby
 */
public class P038 {
  private static boolean isPandigital(final int number) {
    if (MathUtils.log10(number) + 1 != 9) {
      return false;
    }
    Set<Integer> set = Sets.newHashSet(0);
    int n = number;
    int digit;
    while (n > 0) {
      digit = n % 10;
      n /= 10;
      if (set.contains(digit)) {
        return false;
      }
      set.add(digit);
    }
    return true;
  }


  public static void main(String[] args) {
    int number = 9;
    for (int n = 9487; n > 9234; n--) {
      number = 100002 * n;
      if (isPandigital(number)) {
        break;
      }
    }
    System.out.println("The largest 1 to 9 pandigital 9-digit number that can be formed as the concatenated product of an integer * with (1,2, ... , n) where n > 1, is "
        + number);
  }
}
