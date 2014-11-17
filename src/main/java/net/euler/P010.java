package net.euler;

import java.math.BigInteger;

/**
 * Created by kevin on 11/13/14.
 */
public class P010 {
  public static void main(String[] args) {
    BigInteger n;
    if (args.length > 0) {
      n = new BigInteger(args[0]);
    } else {
      n = BigInteger.valueOf(2000000);
    }

    BigPrimeIterator p = new BigPrimeIterator();
    BigInteger sum = BigInteger.ZERO;
    BigInteger prime = p.next();
    while (prime.compareTo(n) == -1) { // i.e. prime less than n
      sum = sum.add(prime);
      prime = p.next();
    }
    System.out.println("The sum of all the primes below " + n + " is " + sum);
  }
}
