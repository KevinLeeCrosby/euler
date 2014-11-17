package net.euler;

import lcc.util.Counter;

import java.math.BigInteger;

/**
 * Created by kevin on 11/13/14.
 */
public class P012p1 {
  private static int countDivisors(int t) {
    // Highly composite number formula
    Counter<Integer> count = new Counter<>();
    BigPrimeIterator p = new BigPrimeIterator();

    BigInteger number = BigInteger.valueOf(t);
    BigInteger prime;
    do {
      prime = p.next();
      while (number.mod(prime).equals(BigInteger.ZERO)) {
        number = number.divide(prime);
        count.increment(prime.intValue());
      }
    } while (prime.compareTo(number.divide(prime)) != 1); // i.e. if prime <= sqrt(number), p <= n/p
    if (!number.equals(BigInteger.ONE)) {
      count.increment(number.intValue());
    }

    int product = 1;
    for (int factor : count.getKeys()) {
      product *= count.getIntCount(factor) + 1;
    }
    return product;
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 500;
    }

    int n = 1;
    int t = (n - 1) * n / 2; // count all previous up to this point
    int noDivisors;
    do {
      t += n;
      noDivisors = countDivisors(t);
      System.out.println(n++ + "\t" + noDivisors + "\t" + t);
    } while (noDivisors <= limit);

    System.out.println("The value of the first triangle number to have over " + limit + " divisors is " + t);
  }
}
