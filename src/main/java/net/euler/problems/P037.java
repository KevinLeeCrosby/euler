package net.euler.problems;

import net.euler.utils.MathUtils;
import net.euler.utils.Primes;

/**
 * The number 3797 has an interesting property. Being prime itself, it is possible to continuously remove digits from
 * left to right, and remain prime at each stage: 3797, 797, 97, and 7. Similarly we can work from right to left: 3797,
 * 379, 37, and 3.
 *
 * Find the sum of the only eleven primes that are both truncatable from left to right and right to left.
 *
 * NOTE: 2, 3, 5, and 7 are not considered to be truncatable primes.
 *
 * @author Kevin Crosby
 */
public class P037 {
  private static Primes primes = Primes.getInstance();

  private static Long reverse(final long n) {
    long r = 0;
    for (long d = n; d > 0; d /= 10) {
      r = r * 10 + d % 10;
    }
    return r;
  }

  private static boolean isTruncatablePrime(final long prime) {
    if (MathUtils.log10(prime) == 0) return false; // single digits
    boolean result = primes.isPrime(prime);
    long forward = prime / 10;          // right to left
    long reverse = reverse(prime) / 10; // left to right
    while (forward > 0 && result) {
      result = primes.isPrime(forward) && primes.isPrime(reverse(reverse));
      forward /= 10;
      reverse /= 10;
    }
    return result;
  }

  public static void main(String[] args) {
    int count = 0;
    long sum = 0;
    for (long prime : primes) {
      if (count >= 11) { break; }
      if (isTruncatablePrime(prime)) {
        System.out.print(prime + ",");
        sum += prime;
        count++;
      }
    }
    System.out.println();

    System.out.println("The sum of the only eleven primes that are both truncatable from left to right and right to left, is " + sum);
  }
}
