package net.euler;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;

/**
 * A googol (10^100) is a massive number: one followed by one-hundred zeros; 100^100 is almost unimaginably large:
 * one followed by two-hundred zeros. Despite their size, the sum of the digits in each number is only 1.
 * <p/>
 * Considering natural numbers of the form, a^b, where a, b < 100, what is the maximum digital sum?
 *
 * @author Kevin Crosby
 */
public class P056 {
  private static Integer digitSum(BigInteger integer) {
    String string = integer.toString();
    int sum = 0;

    for (int i = 0; i < string.length(); i++) {
      int digit = Character.getNumericValue(string.charAt(i));
      sum += digit;
    }
    return sum;
  }

  public static void main(String[] args) {
    int maxSum = 0;
    for (BigInteger a = valueOf(90); a.compareTo(valueOf(100)) == -1; a = a.add(ONE)) {
      for (int b = 90; b < 100; b++) {
        int digitSum = digitSum(a.pow(b));
        if (maxSum < digitSum) {
          maxSum = digitSum;
          System.out.println(a + "^" + b + " => " + digitSum);
        }
      }
    }
    System.out.println("Considering natural numbers of the form, a^b, where a, b < 100, the maximum digital sum is " + maxSum);
  }
}
