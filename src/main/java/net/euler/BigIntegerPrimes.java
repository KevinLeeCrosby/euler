package net.euler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by kevin on 11/25/14.
 */
public class BigIntegerPrimes implements Iterable<BigInteger> {
  private static BigIntegerPrimes instance = null;
  private List<BigInteger> primes = new ArrayList<>();
  private static final BigInteger TWO = BigInteger.valueOf(2L);

  private BigIntegerPrimes() {
    primes.add(TWO);
  }

  public static BigIntegerPrimes getInstance() {
    if (instance == null) {
      synchronized (BigIntegerPrimes.class) {
        if (instance == null) {
          instance = new BigIntegerPrimes();
        }
      }
    }
    return instance;
  }

  /**
   * Returns an iterator over a set of elements of type Integer.
   *
   * @return an Iterator.
   */
  @Override
  public Iterator<BigInteger> iterator() {
    return new BigIntegerPrimeIterator();
  }

  public BigInteger get(int index) {
    assert index >= 0 : "index must be...";
    if (index >= primes.size()) {
      synchronized (this) {
        if (index >= primes.size()) {
          for (int i = primes.size(); i <= index; i++) {
            BigInteger nextOdd = primes.get(primes.size() - 1).add(BigInteger.ONE).or(BigInteger.ONE); // get next odd (works for number 2)
            while (!isPrime(nextOdd)) {
              nextOdd = nextOdd.add(TWO);
            }
            primes.add(nextOdd);
            //System.out.println("Generating prime #" + i + ": " + nextOdd);
          }
        }
      }
    }
    return primes.get(index);
  }

  public boolean isPrime(BigInteger number) {
    if (number.compareTo(primes.get(primes.size() - 1)) != 1) { // gt == 1, le != 1
      return primes.contains(number);
    }
    int i = 0;
    BigInteger prime;
    do {
      prime = get(i++);
      if (number.mod(prime).equals(BigInteger.ZERO)) {
        return false;
      }
    } while (prime.compareTo(number.divide(prime)) != 1); // i.e. if prime <= sqrt(number), p <= n/p
    return true;
  }

  public List<BigInteger> factor(BigInteger number) {
    List<BigInteger> factors = Lists.newArrayList();
    int i = 0;
    BigInteger prime;
    do {
      prime = get(i++);
      while (number.mod(prime).equals(BigInteger.ZERO)) {
        number = number.divide(prime);
        factors.add(prime);
      }
    } while (prime.compareTo(number.divide(prime)) != 1); // i.e. if prime <= sqrt(number), p <= n/p
    if (!number.equals(BigInteger.ONE)) {
      factors.add(number);
    }

    return factors;
  }

  public BigInteger countDivisors(BigInteger number) {
    // Highly composite number formula
    BigInteger count = BigInteger.ONE;
    List<BigInteger> factors = factor(number);
    for (BigInteger factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      count = count.multiply(BigInteger.valueOf(exponent + 1));
    }
    return count;
  }

  public BigInteger sumDivisors(BigInteger number) {
    BigInteger sum = BigInteger.ONE;
    List<BigInteger> factors = factor(number);
    for (BigInteger factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      BigInteger numerator = factor.pow(exponent + 1).subtract(BigInteger.ONE);
      BigInteger denominator = factor.subtract(BigInteger.ONE);
      sum = sum.multiply(numerator).divide(denominator);
    }
    return sum;
  }

  public BigInteger aliquotSum(BigInteger number) { // a.k.a. sum proper divisors
    return sumDivisors(number).subtract(number);  // make it proper
  }


  private class BigIntegerPrimeIterator implements Iterator<BigInteger> {
    private int index;

    public BigIntegerPrimeIterator() {
      index = 0;
    }

    public boolean hasNext() {
      return true;
    } // always has a next prime

    public BigInteger next() {
      return BigIntegerPrimes.this.get(index++);
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    for (int limit : Lists.newArrayList(10, 20, 30, 15)) {
      BigIntegerPrimes primes = BigIntegerPrimes.getInstance();
      int i = 0;
      for (BigInteger prime : primes) {
        System.out.print(prime + " ");
        if (i++ == limit) {
          System.out.println("");
          break;
        }
      }
    }

    BigIntegerPrimes primes = BigIntegerPrimes.getInstance();
    System.out.println("100th prime is " + primes.get(100));

    for (int n : Lists.newArrayList(1003, 1009)) { // false, true
      System.out.println("Is " + n + " prime? " + primes.isPrime(BigInteger.valueOf(n)));
    }
  }
}
