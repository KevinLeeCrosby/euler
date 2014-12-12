package net.euler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

import static net.euler.MathUtils.pow;

/**
 * 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
 *
 * Find the sum of all numbers which are equal to the sum of the factorial of their digits.
 *
 * Note: as 1! = 1 and 2! = 2 are not sums they are not included.
 *
 * @author Kevin Crosby
 */
public class P034 {
  private static List<Integer> factorial = Lists.newArrayList(1);

  private static boolean isFactorion(final int number, final int base) {
    List<Integer> digits = Lists.newArrayList();
    int sum = 0;
    int n = number;
    while (n > 0) {
      int digit = n % base;
      digits.add(digit);
      n /= base;
      sum += factorial(digit);
    }
    if (number != sum) {
      return false;
    }
    System.out.println("Found factorion " + number + " = " + Joiner.on("! + ").join(Lists.reverse(digits)) + "!" );
    return true;
  }

  private static int factorial(final int n) {
    if (factorial.size() <= n) {
      factorial.add(n * factorial(n - 1));
    }
    return factorial.get(n);
  }

  public static void main(String[] args) {
    int base;
    if (args.length > 0) {
      base = Integer.parseInt(args[0]);
    } else {
      base = 10;
    }

    long maxFactorial = factorial(base - 1);

    int sum = 0;
    for (int length = 2; pow(base, length - 1) < length * maxFactorial; length++) {
      for (int number = pow(base, length - 1).intValue(); number < pow(base, length); number++) {
        if (isFactorion(number, base)) {
          sum += number;
        }
      }
    }
    System.out.println("The sum of all numbers which are equal to the sum of the factorial of their digits is " + sum);
  }
}
