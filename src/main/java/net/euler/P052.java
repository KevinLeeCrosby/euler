package net.euler;

import static net.euler.MathUtils.log10;
import static net.euler.MathUtils.pow;

/**
 * It can be seen that the number, 125874, and its double, 251748, contain exactly the same digits, but in a different order.
 * <p/>
 * Find the smallest positive integer, x, such that 2x, 3x, 4x, 5x, and 6x, contain the same digits.
 *
 * @author Kevin Crosby
 */
public class P052 {
  private static Primes primes = Primes.getInstance();

  private static int hashCode(int number) {
    int hash = 1;
    while (number > 0) {
      hash *= primes.get(number % 10);
      number /= 10;
    }
    return hash;
  }

  public static void main(String[] args) {
    int firstDigit = 1;
    int restDigits = 2;
    int x = 0;

    boolean loop = true;
    while (loop) {
      loop = false;
      int noDigits = log10(restDigits).intValue() + 1;
      x = firstDigit * pow(10, noDigits++).intValue() + restDigits++;
      int hashCode = hashCode(x);
      for (int multiple = 2; multiple < 7; multiple++) {
        if (hashCode != hashCode(multiple * x)) {
          loop = true;
          break;
        }
      }
    }
    System.out.println("The smallest positive integer, x, such that 2x, 3x, 4x, 5x, and 6x, contain the same digits is " + x);
  }
}
