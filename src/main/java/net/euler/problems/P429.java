package net.euler.problems;

import net.euler.utils.NewPrimes;

import static net.euler.utils.MathUtils.modPow;

/**
 * A unitary divisor d of a number n is a divisor of n that has the property gcd(d, n/d) = 1.
 * The unitary divisors of 4! = 24 are 1, 3, 8 and 24.
 * The sum of their squares is 1^2 + 3^2 + 8^2 + 24^2 = 650.
 *
 * Let S(n) represent the sum of the squares of the unitary divisors of n. Thus S(4!)=650.
 *
 * Find S(100 000 000!) modulo 1 000 000 009.
 *
 * @author Kevin Crosby
 */
public class P429 {
  private final NewPrimes primes;

  private P429(final long limit) {
    primes = NewPrimes.getInstance(limit);
  }

  // p-adic order (valuation) of n factorial : vp(n!)  Legendre's formula
  private long vp(final long p, final long n) {
    assert primes.isPrime(p) : String.format("%d is not a prime number!", p);
    long nu = 0;
    for(long pk = p; pk <= n; pk *= p) {
      nu += n / pk;
    }
    return nu;
  }

  // modulus of unitary divisor function (k = 2) of n!
  private long modSigma2Star(final long n, final long modulus) {
    // NOTE:  number of unitary divisors = 2 ^ # unique primes
    long product = 1;
    long count = 0, largest = 2;
    for(final long p : primes) {
      long exponent = vp(p, n);
      if (exponent == 0) break;
      count++;
      largest = p;
      product = product * (1 + modPow(p, 2 * exponent, modulus)) % modulus;
    }
    System.out.format("%d unique primes up to %d\n", count, largest);
    return product;
  }

  public static void main(String[] args) {
    final long number = args.length > 0 ? Long.parseLong(args[0]) : 100000000;
    final long modulus = args.length > 1 ? Long.parseLong(args[1]) : 1000000009;

    P429 p = new P429(number);
    long s = p.modSigma2Star(number, modulus);

    System.out.format("S(%d!) modulo %d is %d.\n", number, modulus, s);
  }
}
