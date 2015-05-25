package net.euler.problems;

import net.euler.utils.Primes;

/**
 * The number, 1406357289, is a 0 to 9 pandigital number because it is made up of each of the digits 0 to 9 in some order, but it also has a rather interesting sub-string divisibility property.
 * <p/>
 * Let d1 be the 1st digit, d2 be the 2nd digit, and so on. In this way, we note the following:
 * <p/>
 * d2d3d4=406 is divisible by 2
 * d3d4d5=063 is divisible by 3
 * d4d5d6=635 is divisible by 5
 * d5d6d7=357 is divisible by 7
 * d6d7d8=572 is divisible by 11
 * d7d8d9=728 is divisible by 13
 * d8d9d10=289 is divisible by 17
 * Find the sum of all 0 to 9 pandigital numbers with this property.
 *
 * @author Kevin Crosby
 */
public class P043 {
  private static Primes primes = Primes.getInstance();

  private static boolean isSubStringDivisible(String permutation) {
    for (int d = 1; d < 8; d++) {
      long prime = primes.get(d - 1);
      long number = Long.parseLong(permutation.substring(d, d + 3));
      if (!primes.factor(number).contains(prime)) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    String digits = "0123456789";

    long sum = 0;
    for (String permutation : P024.permute(digits)) {
      if (isSubStringDivisible(permutation)) {
        System.out.println("Found " + permutation);
        sum += Long.parseLong(permutation);
      }
    }
    System.out.println("The sum of all 0 to 9 pandigital numbers that are substring divisible by primes is " + sum);
  }
}
