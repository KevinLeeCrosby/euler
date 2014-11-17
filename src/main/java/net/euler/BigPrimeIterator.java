package net.euler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kevin on 11/12/14.
 */
public class BigPrimeIterator implements Iterator<BigInteger> {
  private static final BigInteger TWO = BigInteger.valueOf(2);
  private static List<BigInteger> primes = new ArrayList<>();
  private static int index;

  public BigPrimeIterator() {
    index = 0;
    if (primes.isEmpty()) {
      primes.add(TWO);
    }
  }

  public boolean hasNext() { return true; } // always has a next prime

  public BigInteger next() {
    if (index < primes.size()) {
      return retrieveNextPrime();
    } else {
      return generateNextPrime();
    }
  }

  private BigInteger retrieveNextPrime() {
    assert index < primes.size() : "Cannot retrieve ungenerated prime!";
    return primes.get(index++);
  }

  public BigInteger generateNextPrime() {
    BigInteger nextOdd = primes.get(primes.size() - 1).add(BigInteger.ONE).or(BigInteger.ONE); // get next odd (works for number 2)
    while (!isPrime(nextOdd)) {
      nextOdd = nextOdd.add(TWO);
    }
    primes.add(nextOdd);
    index++;
    return nextOdd;
  }

  private static boolean isPrime(final BigInteger number) {
    int i = 0;
    BigInteger prime;
    do {
      prime = primes.get(i++);
      if (number.mod(prime).equals(BigInteger.ZERO)) {
        return false;
      }
    } while (i < primes.size() && prime.compareTo(number.divide(prime)) != 1); // i.e. if prime <= sqrt(number)
    return true;
  }

  public void remove() { throw new UnsupportedOperationException(); }

  public static void main(String[] args) {
    BigPrimeIterator a = new BigPrimeIterator();
    for (int i = 0; i < 10; i++) {
      System.out.print(a.next() + " ");
    }
    System.out.println("");

    BigPrimeIterator b = new BigPrimeIterator();
    for (int i = 0; i < 20; i++) {
      System.out.print(b.next() + " ");
    }
    System.out.println("");

    BigPrimeIterator c = new BigPrimeIterator();
    for (int i = 0; i < 30; i++) {
      System.out.print(c.next() + " ");
    }
    System.out.println("");

    BigPrimeIterator d = new BigPrimeIterator();
    for (int i = 0; i < 15; i++) {
      System.out.print(c.next() + " ");
    }
    System.out.println("");
  }
}
