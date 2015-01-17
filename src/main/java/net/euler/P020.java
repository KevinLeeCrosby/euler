package net.euler;

import java.math.BigInteger;

/**
 * n! means n × (n − 1) × ... × 3 × 2 × 1
 *
 * For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800,
 * and the sum of the digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.
 *
 * Find the sum of the digits in the number 100!
 *
 * @author Kevin Crosby
 */
public class P020 {
  public static BigInteger factorial(long n) {
    if (n < 2) {
      return BigInteger.ONE;
    } else {
      return BigInteger.valueOf(n).multiply(factorial(n - 1));
    }
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 100;
    }

    int sum = P016.sumDigits(factorial(limit));

    System.out.println("The sum of the digits in the number " + limit + "! is " + sum);
  }
}
