package net.euler.problems;

import net.euler.utils.Primes;

/**
 * We shall say that an n-digit number is pandigital if it makes use of all the digits 1 to n exactly once. For example, 2143 is a 4-digit pandigital and is also prime.
 * <p/>
 * What is the largest n-digit pandigital prime that exists?
 *
 * @author Kevin Crosby
 */
public class P041 {
  private static Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    String digits = "7654321";

    long prime = 0;
    for (String permutation : P024.permute(digits)){
      long number = Long.parseLong(permutation);
      if (primes.isPrime(number)) {
        prime = number;
        break;
      }
    }
    System.out.println("The largest n-digit pandigital prime that exists is " + prime);
  }
}
