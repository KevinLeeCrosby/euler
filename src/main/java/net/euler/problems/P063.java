package net.euler.problems;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static net.euler.utils.MathUtils.log10;

/**
 * The 5-digit number, 16807=7^5, is also a fifth power. Similarly, the 9-digit number, 134217728=8^9, is a ninth
 * power.
 *
 * How many n-digit positive integers exist which are also an nth power?
 *
 * @author Kevin Crosby
 */
public class P063 {
  public static void main(String[] args) {

    boolean loop = true;
    int count = 0;
    for (int exponent = 1; loop; exponent++) {
      loop = false;
      int noDigits = exponent;
      for (BigInteger base = ONE; noDigits <= exponent; base = base.add(ONE)) {
        BigInteger power = base.pow(exponent);
        noDigits = log10(power).intValue() + 1;
        if (exponent == noDigits) {
          System.out.println(base + "^" + exponent + " = " + power);
          count++;
          loop = true;
        }
      }
    }
    System.out.println("There are " + count + " n-digit positive integers exist which are also an nth power.");
  }
}
