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

    BigIntegerPrimes bip = BigIntegerPrimes.getInstance();
    BigInteger sum = BigInteger.ZERO;
    int i = 0;
    BigInteger prime = bip.get(i++);
    while (prime.compareTo(n) == -1) { // i.e. prime < n
      sum = sum.add(prime);
      prime = bip.get(i++);
    }

    System.out.println("The sum of all the primes below " + n + " is " + sum);
  }
}
