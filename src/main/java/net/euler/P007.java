package net.euler;

import com.google.common.collect.Iterators;

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

    BigPrimeIterator p = new BigPrimeIterator();
    BigInteger prime = Iterators.get(p, n - 1);

    System.out.println("Prime number #" + n + " is " + prime);
  }
}
