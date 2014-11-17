package net.euler;

import lcc.util.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kevin on 11/14/14.
 */
public class Primes implements Iterable<Integer> {
  private static Primes instance = null;
  private List<Integer> primes = new ArrayList<>();

  private Primes() {
    primes.add(2);
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
  public Iterator<Integer> iterator() {
    return new PrimeIterator();
  }

  public int get(int index) {
    assert index >= 0 : "index must be...";
    if (index >= primes.size()) {
      synchronized (this) {
        if (index >= primes.size()) {
          for (int i = primes.size(); i <= index; i++) {
            int nextOdd = (primes.get(primes.size() - 1) + 1) | 1; // get next odd (works for number 2)
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

  public boolean isPrime(int number) {
    int i = 0;
    int prime;
    do {
      prime = primes.get(i++);
      if (number % prime == 0) {
        return false;
      }
    } while (i < primes.size() && prime <= number / prime); // i.e. if prime <= sqrt(number)
    return true;
  }

  public static void main(String[] args) {
    for (int limit : Lists.of(10, 20, 30, 15)) {
      Primes primes = Primes.getInstance();
      int i = 0;
      for (Integer prime : primes) {
        System.out.print(prime + " ");
        if (i++ == limit) {
          System.out.println("");
          break;
        }
      }
    }

    Primes primes = Primes.getInstance();
    System.out.println("100th prime is " + primes.get(100));

    for (int n : Lists.of(1003, 1009)) { // false, true
      System.out.println("Is " + n + " prime? " + primes.isPrime(n));
    }
  }

  private class PrimeIterator implements Iterator<Integer> {
    private int index;

    public PrimeIterator() {
      index = 0;
    }

    public boolean hasNext() { return true; } // always has a next prime

    public Integer next() {
      return Primes.this.get(index++);
    }

    public void remove() { throw new UnsupportedOperationException(); }
  }
}
