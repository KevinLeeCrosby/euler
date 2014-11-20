package net.euler;

import java.math.BigInteger;

/**
 * Created by kevin on 11/20/14.
 */
public class P016 {
  private static final int exponent = 1000;

  public static int sumDigits(BigInteger integer) {
    String string = integer.toString();
    int sum = 0;

    for (int i = 0; i < string.length(); i++) {
      int digit = Character.getNumericValue(string.charAt(i));
      sum += digit;
    }
    return sum;
  }

  public static void main(String[] args) {
    BigInteger integer = BigInteger.valueOf(2L).pow(exponent);

    System.out.println("The sum of the digits of the number 2^" + exponent + " is " + sumDigits(integer));
  }
}
