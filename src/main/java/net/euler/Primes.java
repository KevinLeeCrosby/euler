package net.euler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 11/14/14.
 */
public class Primes implements Iterable<Long> {
  private static Primes instance = null;
  private List<Long> primes = new ArrayList<>();

  private Primes() {
    primes.add(2L);
  }

  public static Primes getInstance() {
    if (instance == null) {
      synchronized (Primes.class) {
        if (instance == null) {
          instance = new Primes();
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
  public Iterator<Long> iterator() {
    return new PrimeIterator();
  }

  public long get(int index) {
    assert index >= 0 : "index must be...";
    if (index >= primes.size()) {
      synchronized (this) {
        if (index >= primes.size()) {
          for (int i = primes.size(); i <= index; i++) {
            long nextOdd = (primes.get(primes.size() - 1) + 1L) | 1L; // get next odd (works for number 2)
            while (!isPrime(nextOdd)) {
              nextOdd += 2;
            }
            primes.add(nextOdd);
            //System.out.println("Generating prime #" + i + ": " + nextOdd);
          }
        }
      }
    }
    return primes.get(index);
  }

  public boolean isPrime(long number) {
    if (number <= primes.get(primes.size() - 1)) {
      return primes.contains(number);
    }
    int i = 0;
    long prime;
    do {
      prime = get(i++);
      if (number % prime == 0) {
        return false;
      }
    } while (prime <= number / prime); // i.e. if prime <= sqrt(number), p <= n/p
    return true;
  }

  public List<Long> factor(long number) {
    List<Long> factors = Lists.newArrayList();
    int i = 0;
    long prime;
    do {
      prime = get(i++);
      while (number % prime == 0) {
        number /= prime;
        factors.add(prime);
      }
    } while (prime <= number / prime); // i.e. if prime <= sqrt(number), p <= n/p
    if (number != 1L) {
      factors.add(number);
    }

    return factors;
  }

  public long sumDivisors(long number) {
    Map<Long, Integer> count = Maps.newHashMap();

    List<Long> factors = factor(number);
    for (Long factor : factors) {
      int exponent = count.containsKey(factor) ? count.get(factor) : 0;
      count.put(factor, exponent + 1);
    }

    long sum = 1;
    for (Map.Entry<Long, Integer> entry : count.entrySet()) {
      Long factor = entry.getKey();
      int exponent = entry.getValue();
      long numerator = Double.valueOf(Math.pow(factor, exponent + 1) - 1).intValue();
      long denominator = factor - 1;
      sum *= numerator / denominator;
    }

    return sum;
  }

  public long aliquotSum(long number) { // a.k.a. sum proper divisors
    return sumDivisors(number) - number;  // make it proper
  }

  private class PrimeIterator implements Iterator<Long> {
    private int index;

    public PrimeIterator() {
      index = 0;
    }

    public boolean hasNext() {
      return true;
    } // always has a next prime

    public Long next() {
      return Primes.this.get(index++);
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    for (int limit : Lists.newArrayList(10, 20, 30, 15)) {
      Primes primes = Primes.getInstance();
      int i = 0;
      for (long prime : primes) {
        System.out.print(prime + " ");
        if (i++ == limit) {
          System.out.println("");
          break;
        }
      }
    }

    Primes primes = Primes.getInstance();
    System.out.println("100th prime is " + primes.get(100));

    for (int n : Lists.newArrayList(1003, 1009)) { // false, true
      System.out.println("Is " + n + " prime? " + primes.isPrime(n));
    }
  }
}
