package net.euler;

import java.math.BigInteger;

/**
 * Created by kevin on 11/26/14.
 */
public class P019 {
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
