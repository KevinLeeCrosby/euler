package net.euler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

  public Long get(int index) {  // TODO:  switch to Sieve of Atkin for up to Prime n??? -- make Long generate(long number)?
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
    if (number < 2L) {
      return factors;
    }

    int i = 0;
    long prime;
    do {
      prime = get(i++);
      while (number % prime == 0) {
        number /= prime;
        factors.add(prime);
      }
    } while (prime <= number / prime); // i.e. if prime <= sqrt(number), p <= n/p
    if (number > 1L) {
      factors.add(number);
    }

    return factors;
  }

  public List<Long> divisors(long number) {
    // factor number
    List<Long> factors = factor(number);
    if (factors.isEmpty()) {
      return number == 1 ? Lists.newArrayList(1L) : Lists.<Long>newArrayList();
    }

    // construct lists of powers
    List<List<Long>> lists = Lists.newArrayList();
    for (long factor : Sets.newHashSet(factors)) {
      List<Long> powers = Lists.newArrayList();
      for (int exponent = 1; exponent <= Collections.frequency(factors, factor); exponent++) {
        powers.add(MathUtils.pow(factor, exponent));
      }
      lists.add(powers);
    }

    // take Kronecker product of lists of powers
    List<Long> divisors = Lists.newArrayList(1L);
    for (List<Long> powers : lists) {
      List<Long> products = Lists.newArrayList();
      for (long power : powers) {
        for (long divisor : divisors) {
          products.add(power * divisor);
        }
      }
      divisors.addAll(products);
    }
    Collections.sort(divisors);
    return divisors;
  }

  public Long countDivisors(long number) { // Highly composite number formula
    if (number < 1L) {
      return 0L;
    }
    long count = 1;
    List<Long> factors = factor(number);
    for (Long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      count *= exponent + 1;
    }
    return count;
  }

  public Long sumDivisors(long number) {
    if (number < 1L) {
      return 0L;
    }
    long sum = 1;
    List<Long> factors = factor(number);
    for (Long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      long numerator = MathUtils.pow(factor, exponent + 1) - 1;
      long denominator = factor - 1;
      sum *= numerator / denominator;
    }
    return sum;
  }

  public Long aliquotSum(long number) { // a.k.a. sum proper divisors
    return sumDivisors(number) - number; // make it proper
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
