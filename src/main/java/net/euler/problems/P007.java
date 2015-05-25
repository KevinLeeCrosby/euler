package net.euler.problems;

import net.euler.utils.BigIntegerPrimes;

import java.math.BigInteger;

/**
 * Created by kevin on 11/12/14.
 */
public class P007 {
  public static void main(String[] args) {
    int n;
    if (args.length > 0) {
      n = Integer.parseInt(args[0]);
    } else {
      n = 10001;
    }

    BigIntegerPrimes bip = BigIntegerPrimes.getInstance();
    BigInteger prime = bip.get(n - 1); // index is 0 based

    System.out.println("Prime number #" + n + " is " + prime);
  }
}
