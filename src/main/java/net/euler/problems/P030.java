package net.euler.problems;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Surprisingly there are only three numbers that can be written as the sum of fourth powers of their digits:
*
* 1634 = 1^4 + 6^4 + 3^4 + 4^4
* 8208 = 8^4 + 2^4 + 0^4 + 8^4
* 9474 = 9^4 + 4^4 + 7^4 + 4^4
* As 1 = 1^4 is not a sum it is not included.
*
* The sum of these numbers is 1634 + 8208 + 9474 = 19316.
*
* Find the sum of all the numbers that can be written as the sum of fifth powers of their digits.
 */
public class P030 {
  private static List<Integer> powers = Lists.newArrayList();

  private static void generatePowers(int power) {
    for (int i = 0; i < 10; i++) {
      int product = 1;
      for (int j = 0; j < power; j++) {
        product *= i;
      }
      powers.add(product);
    }
  }

  public static void main(String[] args) {
    int power;
    if (args.length > 0) {
      power = Integer.parseInt(args[0]);
    } else {
      power = 5;
    }

    generatePowers(power);

    int sum = 0;
    for (int n = power; n <= (power + 1) * powers.get(9); n++) {
      int number = n;
      int sumDigitPowers = 0;
      while (number > 0) {
        int digit = number % 10;
        number /= 10;
        sumDigitPowers += powers.get(digit);
      }
      if (sumDigitPowers == n) {
        sum += n;
        System.out.println("Found " + n);
      }
    }
    System.out.println("The sum of all the numbers that can be written as the sum of power "
        + power + " of their digits is " + sum + ".");
  }
}
